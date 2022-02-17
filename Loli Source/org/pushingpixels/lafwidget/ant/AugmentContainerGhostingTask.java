/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 *  org.apache.tools.ant.Task
 *  org.apache.tools.ant.types.FileSet
 *  org.apache.tools.ant.types.Path
 */
package org.pushingpixels.lafwidget.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.pushingpixels.lafwidget.ant.AugmentException;
import org.pushingpixels.lafwidget.ant.ContainerGhostingAugmenter;
import org.pushingpixels.lafwidget.ant.ContainerGhostingType;

public class AugmentContainerGhostingTask
extends Task {
    private boolean m_verbose;
    private Path m_classpath;
    private List<FileSet> m_fileSet;
    private List<ContainerGhostingType> m_delegatesToUpdate;

    public void init() throws BuildException {
        super.init();
        this.m_fileSet = new ArrayList<FileSet>();
        this.m_delegatesToUpdate = new ArrayList<ContainerGhostingType>();
    }

    private String[] getPaths() {
        String[] pathArray = null;
        if (this.m_classpath != null) {
            pathArray = this.m_classpath.list();
        } else {
            pathArray = new String[this.m_fileSet.size()];
            for (int i2 = 0; i2 < this.m_fileSet.size(); ++i2) {
                FileSet fileSet = this.m_fileSet.get(i2);
                File directory = fileSet.getDir(this.getProject());
                pathArray[i2] = directory.getAbsolutePath();
            }
        }
        return pathArray;
    }

    public void execute() throws BuildException {
        try {
            String[] pathArr = this.getPaths();
            ContainerGhostingAugmenter augmenter = new ContainerGhostingAugmenter();
            if (this.m_verbose) {
                augmenter.setVerbose(this.m_verbose);
            }
            for (int i2 = 0; i2 < pathArr.length; ++i2) {
                augmenter.process(pathArr[0], new File(pathArr[0]), this.m_delegatesToUpdate);
            }
        }
        catch (AugmentException ae) {
            throw new BuildException((Throwable)ae);
        }
    }

    public void addClassPathSet(FileSet fSet) {
        this.m_fileSet.add(fSet);
    }

    public void addContainerGhosting(ContainerGhostingType containerGhosting) {
        this.m_delegatesToUpdate.add(containerGhosting);
    }

    public Path getClasspath() {
        return this.m_classpath;
    }

    public void setClasspath(Path classpath) {
        if (this.m_classpath == null) {
            this.m_classpath = classpath;
        } else {
            this.m_classpath.append(classpath);
        }
    }

    public Path createClasspath() {
        if (this.m_classpath == null) {
            this.m_classpath = new Path(this.getProject());
        }
        return this.m_classpath;
    }

    public void setVerbose(boolean bool) {
        this.m_verbose = bool;
    }
}


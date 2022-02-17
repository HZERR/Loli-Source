/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.media;

import javafx.event.EventHandler;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaMarkerEvent;
import javafx.scene.media.MediaPlayer;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
public final class MediaPlayerBuilder
implements Builder<MediaPlayer> {
    private int __set;
    private double audioSpectrumInterval;
    private AudioSpectrumListener audioSpectrumListener;
    private int audioSpectrumNumBands;
    private int audioSpectrumThreshold;
    private boolean autoPlay;
    private double balance;
    private int cycleCount;
    private Media media;
    private boolean mute;
    private Runnable onEndOfMedia;
    private Runnable onError;
    private Runnable onHalted;
    private EventHandler<MediaMarkerEvent> onMarker;
    private Runnable onPaused;
    private Runnable onPlaying;
    private Runnable onReady;
    private Runnable onRepeat;
    private Runnable onStalled;
    private Runnable onStopped;
    private double rate;
    private Duration startTime;
    private Duration stopTime;
    private double volume;

    protected MediaPlayerBuilder() {
    }

    public static MediaPlayerBuilder create() {
        return new MediaPlayerBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(MediaPlayer mediaPlayer) {
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    mediaPlayer.setAudioSpectrumInterval(this.audioSpectrumInterval);
                    break;
                }
                case 1: {
                    mediaPlayer.setAudioSpectrumListener(this.audioSpectrumListener);
                    break;
                }
                case 2: {
                    mediaPlayer.setAudioSpectrumNumBands(this.audioSpectrumNumBands);
                    break;
                }
                case 3: {
                    mediaPlayer.setAudioSpectrumThreshold(this.audioSpectrumThreshold);
                    break;
                }
                case 4: {
                    mediaPlayer.setAutoPlay(this.autoPlay);
                    break;
                }
                case 5: {
                    mediaPlayer.setBalance(this.balance);
                    break;
                }
                case 6: {
                    mediaPlayer.setCycleCount(this.cycleCount);
                    break;
                }
                case 7: {
                    mediaPlayer.setMute(this.mute);
                    break;
                }
                case 8: {
                    mediaPlayer.setOnEndOfMedia(this.onEndOfMedia);
                    break;
                }
                case 9: {
                    mediaPlayer.setOnError(this.onError);
                    break;
                }
                case 10: {
                    mediaPlayer.setOnHalted(this.onHalted);
                    break;
                }
                case 11: {
                    mediaPlayer.setOnMarker(this.onMarker);
                    break;
                }
                case 12: {
                    mediaPlayer.setOnPaused(this.onPaused);
                    break;
                }
                case 13: {
                    mediaPlayer.setOnPlaying(this.onPlaying);
                    break;
                }
                case 14: {
                    mediaPlayer.setOnReady(this.onReady);
                    break;
                }
                case 15: {
                    mediaPlayer.setOnRepeat(this.onRepeat);
                    break;
                }
                case 16: {
                    mediaPlayer.setOnStalled(this.onStalled);
                    break;
                }
                case 17: {
                    mediaPlayer.setOnStopped(this.onStopped);
                    break;
                }
                case 18: {
                    mediaPlayer.setRate(this.rate);
                    break;
                }
                case 19: {
                    mediaPlayer.setStartTime(this.startTime);
                    break;
                }
                case 20: {
                    mediaPlayer.setStopTime(this.stopTime);
                    break;
                }
                case 21: {
                    mediaPlayer.setVolume(this.volume);
                }
            }
        }
    }

    public MediaPlayerBuilder audioSpectrumInterval(double d2) {
        this.audioSpectrumInterval = d2;
        this.__set(0);
        return this;
    }

    public MediaPlayerBuilder audioSpectrumListener(AudioSpectrumListener audioSpectrumListener) {
        this.audioSpectrumListener = audioSpectrumListener;
        this.__set(1);
        return this;
    }

    public MediaPlayerBuilder audioSpectrumNumBands(int n2) {
        this.audioSpectrumNumBands = n2;
        this.__set(2);
        return this;
    }

    public MediaPlayerBuilder audioSpectrumThreshold(int n2) {
        this.audioSpectrumThreshold = n2;
        this.__set(3);
        return this;
    }

    public MediaPlayerBuilder autoPlay(boolean bl) {
        this.autoPlay = bl;
        this.__set(4);
        return this;
    }

    public MediaPlayerBuilder balance(double d2) {
        this.balance = d2;
        this.__set(5);
        return this;
    }

    public MediaPlayerBuilder cycleCount(int n2) {
        this.cycleCount = n2;
        this.__set(6);
        return this;
    }

    public MediaPlayerBuilder media(Media media) {
        this.media = media;
        return this;
    }

    public MediaPlayerBuilder mute(boolean bl) {
        this.mute = bl;
        this.__set(7);
        return this;
    }

    public MediaPlayerBuilder onEndOfMedia(Runnable runnable) {
        this.onEndOfMedia = runnable;
        this.__set(8);
        return this;
    }

    public MediaPlayerBuilder onError(Runnable runnable) {
        this.onError = runnable;
        this.__set(9);
        return this;
    }

    public MediaPlayerBuilder onHalted(Runnable runnable) {
        this.onHalted = runnable;
        this.__set(10);
        return this;
    }

    public MediaPlayerBuilder onMarker(EventHandler<MediaMarkerEvent> eventHandler) {
        this.onMarker = eventHandler;
        this.__set(11);
        return this;
    }

    public MediaPlayerBuilder onPaused(Runnable runnable) {
        this.onPaused = runnable;
        this.__set(12);
        return this;
    }

    public MediaPlayerBuilder onPlaying(Runnable runnable) {
        this.onPlaying = runnable;
        this.__set(13);
        return this;
    }

    public MediaPlayerBuilder onReady(Runnable runnable) {
        this.onReady = runnable;
        this.__set(14);
        return this;
    }

    public MediaPlayerBuilder onRepeat(Runnable runnable) {
        this.onRepeat = runnable;
        this.__set(15);
        return this;
    }

    public MediaPlayerBuilder onStalled(Runnable runnable) {
        this.onStalled = runnable;
        this.__set(16);
        return this;
    }

    public MediaPlayerBuilder onStopped(Runnable runnable) {
        this.onStopped = runnable;
        this.__set(17);
        return this;
    }

    public MediaPlayerBuilder rate(double d2) {
        this.rate = d2;
        this.__set(18);
        return this;
    }

    public MediaPlayerBuilder startTime(Duration duration) {
        this.startTime = duration;
        this.__set(19);
        return this;
    }

    public MediaPlayerBuilder stopTime(Duration duration) {
        this.stopTime = duration;
        this.__set(20);
        return this;
    }

    public MediaPlayerBuilder volume(double d2) {
        this.volume = d2;
        this.__set(21);
        return this;
    }

    @Override
    public MediaPlayer build() {
        MediaPlayer mediaPlayer = new MediaPlayer(this.media);
        this.applyTo(mediaPlayer);
        return mediaPlayer;
    }
}


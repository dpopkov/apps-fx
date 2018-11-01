package org.daydevjv.crow;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements countdown with a given interval.
 */
public class CountDown implements Observable {
    private final List<InvalidationListener> listeners = new ArrayList<>();
    private final Timeline timeline;

    public CountDown(int intervalMilliseconds) {
        timeline = new Timeline(new KeyFrame(Duration.millis(intervalMilliseconds), event -> {
            for (InvalidationListener listener : listeners) {
                listener.invalidated(CountDown.this);
            }
        }));
    }

    public void start(int cycleCount) {
        timeline.setCycleCount(cycleCount);
        timeline.play();
    }

    public void pause() {
        timeline.pause();
    }

    public void stop() {
        timeline.stop();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }
}

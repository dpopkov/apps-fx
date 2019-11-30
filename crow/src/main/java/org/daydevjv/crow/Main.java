package org.daydevjv.crow;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Main extends Application {
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String OUTPUT_PATH = USER_HOME + File.separator + "Documents" + File.separator;
    private static final String FILE_PATH = OUTPUT_PATH + "crow.log.txt";
    private static final int COUNT_DOWN_SECONDS = 5 * 60;
    private static final String AUDIO_FILE = "alarm.mp3";

    private final CountDown countDown = new CountDown(1000);
    private final File file = new File(FILE_PATH);
    private AudioClip audioClip;
    private CountDownTextField txInput;

    // todo: fix this problem with MEDIA_UNAVAILABLE
    /*

    C:\data\tmp>java -jar crow.jar
Exception in Application start method
Exception in thread "main" java.lang.reflect.InvocationTargetException
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at sun.launcher.LauncherHelper$FXHelper.main(LauncherHelper.java:767)
Caused by: java.lang.RuntimeException: Exception in Application start method
        at com.sun.javafx.application.LauncherImpl.launchApplication1(LauncherImpl.java:917)
        at com.sun.javafx.application.LauncherImpl.lambda$launchApplication$154(LauncherImpl.java:182)
        at java.lang.Thread.run(Thread.java:748)
Caused by: MediaException: MEDIA_UNAVAILABLE : C:\data\tmp\src\main\resources\audio\alarm.mp3 (Системе не удается найти указанный путь)
     */

    @Override
    public void start(Stage primaryStage) {
        String url = Paths.get("src/main/resources/audio/" + AUDIO_FILE).toUri().toString();
        audioClip = new AudioClip(url);
        txInput = new CountDownTextField();
        countDown.addListener(txInput);
        txInput.setPrefColumnCount(50);
        txInput.setOnAction(e -> {
            try {
                appendTextToFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            txInput.setText(Integer.toString(COUNT_DOWN_SECONDS));
            countDown.start(COUNT_DOWN_SECONDS);
        });
        HBox hBox = new HBox();
        hBox.getChildren().add(txInput);
        primaryStage.setTitle("Crow");
        primaryStage.setScene(new Scene(hBox));
        primaryStage.show();
    }

    private void playAlarm() {
        audioClip.setCycleCount(1);
        audioClip.play();
    }

    private void appendTextToFile() throws IOException {
        try (FileWriter writer = new FileWriter(file, true)) {
            String text = txInput.getText();
            writer.append(LocalDateTime.now().toString());
            writer.append(System.lineSeparator());
            writer.append(text);
            writer.append(System.lineSeparator());
        }
    }

    private class CountDownTextField extends TextField implements InvalidationListener {
        @Override
        public void invalidated(Observable observable) {
            String s = getText();
            if ("1".equals(s)) {
                setText("Type here");
                selectAll();
                playAlarm();
            } else {
                int n = Integer.parseInt(s);
                setText(Integer.toString(n - 1));
            }
        }
    }
}

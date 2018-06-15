package Code;

import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;


public class Game extends JFrame {
    public static final int width = 800,
                            height = 600;
    public Game() {
        add(GameManagement.getInstance());
        // параметры закрытия
        setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE);
        // размеры окна
        setSize(width, height);
        // позиционирование по центру
        setLocationRelativeTo(null);
        // название окна
        setTitle("Zombie Rush");
        // ресайз
        setResizable(false);
        // видимость
        setVisible(true);

    }

    public static synchronized void playSound(final String path) {
        new Thread(new Runnable() {
            public void run() {
                try {
                        Clip clip;
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "/src/Resources/" + path));
                        DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
                        clip = (Clip)AudioSystem.getLine(info);
                        try {
                            clip.open(inputStream);
                        }
                        catch(Exception e){}
                       if(clip.isRunning()) {
                            clip.close();
                        }
                        clip.start();

                }
                catch(Exception e ) {
                    e.printStackTrace();
                }


           }
        }).start();
       /* try {
            Clip clip;
            // Use URL (instead of File) to read from disk and JAR.
            // Set up an audio input stream piped from the sound file.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "/src/Resources/" + path));
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }*/

    }


    public static void main(String[] args) {
        new Game();
    }
}

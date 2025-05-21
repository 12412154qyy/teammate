package Game1.Controllers;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
public class MusicPlayer {
    private Clip clip;
    public MusicPlayer(){
        this.clip=clip;
    }
    public void play(String filePath, boolean loop) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        clip.close();
    }
}

package motor;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Musica {

  public static void main(String[] args) {

    ReproductorMusica miReproductor = new ReproductorMusica();
    miReproductor.reproducir("MusicaRPG.wav");

    try {
      Thread.sleep(1000000000);
    } catch (Exception e) {
    }
  }
}

class ReproductorMusica {

  public void reproducir(String rutaArchivo) {
    try {
      File archivoAudio = new File(rutaArchivo);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivoAudio);
      Clip clip = AudioSystem.getClip();
      clip.open(audioStream);
      clip.start();
      System.out.println("Reproduciendo: " + archivoAudio.getName());
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}

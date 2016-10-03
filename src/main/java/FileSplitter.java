import edu.cmu.sphinx.util.TimeFrame;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileSplitter {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {

        int threads = 3;

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        String filePath = "/Users/ashish/Desktop/demo_agent_2.wav";

        long fileLength = getFileLength(filePath);
        System.out.println("fileLength : " + fileLength);

        long divisoionFactor = fileLength / threads;
        System.out.println("division factor : " + divisoionFactor);

        for (long i = 0; i < fileLength; i = i + divisoionFactor) {
            Runnable worker = new WorkerThread(new BufferedInputStream(new FileInputStream(filePath)), new TimeFrame(i, i + divisoionFactor));
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        System.out.println("Finished all threads");

    }

    private static long getFileLength(String filePath) throws IOException, UnsupportedAudioFileException {

        InputStream is = new FileInputStream(filePath);
        BufferedInputStream bfs = new BufferedInputStream(is);
        AudioInputStream fileStream = AudioSystem.getAudioInputStream(bfs);
        final AudioFormat format = fileStream.getFormat();
        double length = (fileStream.getFrameLength() + 0.0) / format.getFrameRate();
        return (long) length * 1000; // time in milliseconds
    }
}

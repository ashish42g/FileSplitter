import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.util.TimeFrame;

import java.io.BufferedInputStream;
import java.io.IOException;

public class WorkerThread implements Runnable {

    BufferedInputStream bfs;
    TimeFrame timeFrame;

    public WorkerThread(BufferedInputStream bfs, TimeFrame timeFrame) {
        this.bfs = bfs;
        this.timeFrame = timeFrame;
    }

    public WorkerThread() {
    }

    public String convertSound(BufferedInputStream bfs, TimeFrame timeFrame) throws IOException {

        System.out.println(Thread.currentThread().getName() + " ::::: Start Time " + timeFrame.getStart() + " End Time :::::: " + timeFrame.getEnd());

        String convertedText = "";
        StreamSpeechRecognizer recognizer = getRecognizer();

        recognizer.startRecognition(bfs, timeFrame);
        SpeechResult speechResult;
        while ((speechResult = recognizer.getResult()) != null) {
            convertedText += String.format("Hypothesis: %s\n", speechResult.getHypothesis());
        }
        return convertedText;
    }

    private StreamSpeechRecognizer getRecognizer() throws IOException {

        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        configuration.setSampleRate(8000);

        return new StreamSpeechRecognizer(configuration);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " time frame " + timeFrame);
        try {
            System.out.println(Thread.currentThread().getName() + " :::::::: " + convertSound(bfs, timeFrame));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " (End)");
    }
}

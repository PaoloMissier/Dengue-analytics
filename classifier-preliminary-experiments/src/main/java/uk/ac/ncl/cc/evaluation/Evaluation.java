package uk.ac.ncl.cc.evaluation;

import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Evaluation {

    public static void main(String[] args) {
        String trainingDataFilePath = "tweets-processed.txt";
        FileInputStream dataInputStream = null;
        try {
            dataInputStream = new FileInputStream(trainingDataFilePath);
            ObjectStream<String> lineStream = new PlainTextByLineStream(dataInputStream.getChannel(), "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
            DoccatCrossValidator evaluator = new DoccatCrossValidator("pt", null);
            evaluator.evaluate(sampleStream, 10);
            System.out.println("Document Accuracy: " + evaluator.getDocumentAccuracy());
            System.out.println("Document Count: " + evaluator.getDocumentCount());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

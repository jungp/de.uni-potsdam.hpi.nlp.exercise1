package de.hpi.nlp.exercise1;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;

public class Exercise1 {

	public static void main(String[] args) throws FileNotFoundException, XMLStreamException, URISyntaxException {
		File uri = new File(Exercise1.class.getResource("/GENIA_treebank_v1").toURI());
		
		long start = System.currentTimeMillis();
		
		Corpus corpus = CorpusParser.parse(uri);
		
		TrainingAndTestSplitter tts = new TrainingAndTestSplitter(corpus, 0.9);
		Corpus training = tts.getTrainingCorpus();
		Corpus test = tts.getTestCorpus();
		BigramMLEModel model = new BigramMLEModel(training);

		double perplexity = Perplexity.calculate(model, test);
		System.out.println("Perplexity: " + perplexity);
		
		long time = System.currentTimeMillis() - start;
		System.out.println("Runtime: " + time + "ms\n");
	}

}
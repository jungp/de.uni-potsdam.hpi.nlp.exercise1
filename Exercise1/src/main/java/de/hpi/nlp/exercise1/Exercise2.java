package de.hpi.nlp.exercise1;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;

public class Exercise2 {

	public static void main(String[] args) throws URISyntaxException, FileNotFoundException, XMLStreamException {
		File uri = new File(Exercise2.class.getResource("/GENIA_treebank_v1").toURI());
		
		long start = System.currentTimeMillis();
		
		Corpus corpus = CorpusParser.parse(uri);
		
		TrainingAndTestSplitter splitter = new TrainingAndTestSplitter(corpus, 0.6);
		Corpus training = splitter.getTrainingCorpus();
		Corpus test = splitter.getTestCorpus();
		
		BaselineModel model = new BaselineModel(training);
		double taggerPrecision = PartOfSpeechTagging.determine(model, test);
		System.out.println("Precision: " + taggerPrecision);
		
		BigramHiddenMarkovModel model2 = new BigramHiddenMarkovModel(training);
		taggerPrecision = PartOfSpeechTaggingMarkov.determine(model2, test);
		System.out.println("Precision: " + taggerPrecision);
		
		long time = System.currentTimeMillis() - start;
		System.out.println("Runtime: " + time + "ms\n");
	}

}

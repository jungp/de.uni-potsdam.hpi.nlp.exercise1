package de.hpi.nlp.exercise1;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;

import de.hpi.nlp.evaluation.PartOfSpeechTagging;
import de.hpi.nlp.languagemodel.BaselineModel;
import de.hpi.nlp.languagemodel.BigramHiddenMarkovModel;
import de.hpi.nlp.languagemodel.Corpus;
import de.hpi.nlp.languagemodel.PartOfSpeechTagger;
import de.hpi.nlp.preparation.CorpusParser;
import de.hpi.nlp.preparation.TrainingAndTestSplitter;

public class Exercise2 {

	public static void main(String[] args) throws URISyntaxException, FileNotFoundException, XMLStreamException {
		File uri = new File(Exercise2.class.getResource("/GENIA_treebank_v1").toURI());
		
		long start = System.currentTimeMillis();
		
		Corpus corpus = CorpusParser.parse(uri);
		
		TrainingAndTestSplitter splitter = new TrainingAndTestSplitter(corpus, 0.6);
		Corpus training = splitter.getTrainingCorpus();
		Corpus test = splitter.getTestCorpus();
		
		PartOfSpeechTagger tagger = new BaselineModel(training);
		double taggerPrecision = PartOfSpeechTagging.determine(tagger, test);
		System.out.println("Precision: " + taggerPrecision);
		
		tagger = new BigramHiddenMarkovModel(training);
		taggerPrecision = PartOfSpeechTagging.determine(tagger, test);
		System.out.println("Precision: " + taggerPrecision);
		
		long time = System.currentTimeMillis() - start;
		System.out.println("Runtime: " + time + "ms\n");
	}

}

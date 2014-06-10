package exercise1;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;

import languagemodel.BaselineModel;
import languagemodel.BigramHiddenMarkovModel;
import languagemodel.Corpus;
import languagemodel.PartOfSpeechTagger;
import preparation.CorpusParser;
import preparation.TrainingAndTestSplitter;
import evaluation.PartOfSpeechTagging;

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

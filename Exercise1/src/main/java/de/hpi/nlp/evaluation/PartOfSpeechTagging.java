package de.hpi.nlp.evaluation;

import java.util.Iterator;

import de.hpi.nlp.languagemodel.Article;
import de.hpi.nlp.languagemodel.Corpus;
import de.hpi.nlp.languagemodel.PartOfSpeechTagger;
import de.hpi.nlp.languagemodel.Sentence;
import de.hpi.nlp.languagemodel.Token;

public class PartOfSpeechTagging {
	/**
	 * Determine the precision of a model which assigns tags to
	 * tokens from a preferably unseen test corpus. However, the tokens in the 
	 * test corpus need to have correct tags already assigned (which will be ignored by the model) 
	 * to be able to evaluate the correctness of the tagging.
	 * 
	 */
	public static double determine(PartOfSpeechTagger tagger, Corpus corpus) {
		int correct = 0;
		long numTokens = corpus.getNumTokens();
		
		for(Article a : corpus) {
			for(Sentence s : a) {
					// determine tags for a given sentence
					String tags[] = tagger.determineMostLikelyTags(s);
					
					Iterator<Token> it = s.iterator();
					for(String estimatedTag: tags) {			
						Token sentenceToken = it.next();
						if(sentenceToken.getTag().equals(estimatedTag)) {
							correct++;
						}
					}
			}
		}
		// precision = # of correctly tagged tokens / # of all tokens
		return (double)correct / numTokens;
	}
}

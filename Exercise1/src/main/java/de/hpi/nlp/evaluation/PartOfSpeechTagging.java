package de.hpi.nlp.evaluation;

import java.util.Iterator;

import de.hpi.nlp.languagemodel.Article;
import de.hpi.nlp.languagemodel.Corpus;
import de.hpi.nlp.languagemodel.PartOfSpeechTagger;
import de.hpi.nlp.languagemodel.Sentence;
import de.hpi.nlp.languagemodel.Token;

public class PartOfSpeechTagging {
	
	public static double determine(PartOfSpeechTagger tagger, Corpus corpus) {
		int correct = 0;
		long numTokens = corpus.getNumTokens();
		
		for(Article a : corpus) {
			for(Sentence s : a) {
					int corr = 0;
					//System.out.println(s);
					// determine tag for a given word
					String tags[] = tagger.determineMostLikelyTags(s);
					
					Iterator<Token> it = s.iterator();
					for(String estimatedTag: tags) {
						
						Token sentenceToken = it.next();
						//System.out.print(sentenceToken.getText() + "(" + estimatedTag + ") ");
						if(sentenceToken.getTag().equals(estimatedTag)) {
							correct++;
							corr++;
						}
					}
					//System.out.println("\ncorrect: " + corr + " of " + s.getNumTokens() + " = " + (double)corr / s.getNumTokens());
			}
		}
		return (double)correct / numTokens;
	}
}

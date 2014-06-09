package de.hpi.nlp.exercise1;

import java.util.Iterator;

public class PartOfSpeechTaggingMarkov {
	
	public static double determine(BigramHiddenMarkovModel model, Corpus corpus) {
		int correct = 0;
		long numTokens = corpus.getNumTokens();
		
		for(Article a : corpus) {
			for(Sentence s : a) {
					int corr = 0;
					System.out.println(s);
					// determine tag for a given word
					String tags[] = model.determineMostLikelyTags(s);
					
					Iterator<Token> it = s.iterator();
					for(String estimatedTag: tags) {
						
						Token sentenceToken = it.next();
						System.out.print(sentenceToken.getText() + "(" + estimatedTag + ") ");
						if(sentenceToken.getTag().equals(estimatedTag)) {
							correct++;
							corr++;
						}
					}
					System.out.println("\ncorrect: " + corr + " of " + s.getNumTokens() + " = " + (double)corr / s.getNumTokens());
			}
		}
		return (double)correct / numTokens;
	}
}

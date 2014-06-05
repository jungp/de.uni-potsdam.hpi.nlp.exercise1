package de.hpi.nlp.exercise1;

public class Perplexity {
	
	/**
	 * M : number of words
	 * x_i : sentence
	 * m : number of sentences
	 * 
	 * Perplexity formula:
	 * 2^{-{ 1/M * sum_{i=1}^m {log_2 {p{x_i}}} }}
	 * 
	 * For convenience here base e is used instead of base 2.
	 *
	 */
	public static double calculate(BigramMLEModel model, Corpus corpus) {
		double sum = 0.0;
		long M = corpus.getNumTokens();
		
		for(Article a : corpus) {
			for(Sentence s : a) {
				sum += model.sentenceLogProbability(s);
			}
		}
		
		return Math.exp(- sum / M);
	}
}

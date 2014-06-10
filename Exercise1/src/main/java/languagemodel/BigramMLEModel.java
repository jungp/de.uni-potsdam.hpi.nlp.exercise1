package languagemodel;

import java.util.HashMap;
import java.util.Map;

public class BigramMLEModel {
	private final String BEGINNING_OF_SENTENCE = "<s>";
	// both bigrams and termFrequency contain BEGINNING_OF_SENTENCE
	private final Map<String, Map<String, Integer>> bigrams;
	private final Map<String, Integer> termFrequency;
	private final int vocabSize;
	
	public BigramMLEModel(Corpus corpus) {
		this.vocabSize = corpus.getVocabularySize();
		this.bigrams = new HashMap<String, Map<String, Integer>>();
		this.termFrequency = new HashMap<String, Integer>();
		String prevToken = "";
		String currToken = "";
		
		for(Article a : corpus) {
			for(Sentence s : a) {
				for(Token token : s) {
					currToken = token.getText();

					if (prevToken.equals("")) {
						// beginning of sentence
						
						currToken = BEGINNING_OF_SENTENCE;
						if (bigrams.get(currToken) == null) {
							bigrams.put(currToken, new HashMap<String, Integer>());
						}
					} else {
						// anywhere in the sentence
							
						if (!bigrams.containsKey(currToken)) {
							bigrams.put(currToken, new HashMap<String, Integer>());
						}

						Map<String, Integer> tempMap = bigrams.get(prevToken);

						if (tempMap.containsKey(currToken)) {
							tempMap.put(currToken, tempMap.get(currToken) + 1);
						} else {
							tempMap.put(currToken, 1);
						}
						
					}
					
					prevToken = currToken;
					
					// save term frequency for later probability calculation
					if (termFrequency.get(currToken) != null) {
						termFrequency.put(currToken, termFrequency.get(currToken) + 1);
					} else {
						termFrequency.put(currToken, 1);
					}
				}
			}
		}
	}
	
	public double tokenProbabilityWithoutPreviousToken(String currToken) {
		int prevTokenOccurrences = termFrequency.get(BEGINNING_OF_SENTENCE);
		int currTokenOccurrences = 0;
		
		if (termFrequency.containsKey(currToken)) {
			// if contained get count, if not leave it 0
			currTokenOccurrences = termFrequency.get(currToken);
		}
		
		return (currTokenOccurrences + 1.0) / (prevTokenOccurrences + vocabSize);
	}
	
	public double tokenProbabilityWithPreviousToken(String prevToken, String currToken) {
		int prevTokenOccurrences = 0;
		int bigramOccurrences = 0;
		
		if (!bigrams.containsKey(prevToken)) {
			// probability = (0.0 + 1.0) / (0.0 + vocabSize);
			bigramOccurrences = 0;
			prevTokenOccurrences = 0;
		} else {
			if (!bigrams.get(prevToken).containsKey(currToken)) {
				// probability = (0.0 + 1.0) / (prevTokenOccs + vocabSize);
				bigramOccurrences = 0;
				prevTokenOccurrences = termFrequency.get(prevToken);
			}
			else {
				// probability = (bigramOccs + 1.0) / (prevTokenOccs + vocabSize);
				bigramOccurrences = bigrams.get(prevToken).get(currToken);
				prevTokenOccurrences = termFrequency.get(prevToken);
			}
		}
		return (bigramOccurrences + 1.0) / (prevTokenOccurrences + vocabSize);
	}
	
	public double sentenceLogProbability(Sentence s) {
		String prevToken = "";
		String currToken = "";
		
		double sentenceProbability = 0.0;
		for(Token token : s) {
			currToken = token.getText();
			
			if (prevToken.equals("")) {
				prevToken = BEGINNING_OF_SENTENCE;
				sentenceProbability += Math.log(tokenProbabilityWithoutPreviousToken(currToken));
			} else {
				sentenceProbability += Math.log(tokenProbabilityWithPreviousToken(prevToken, currToken));
			}
			prevToken = currToken;
		}
		return sentenceProbability;
	}
}

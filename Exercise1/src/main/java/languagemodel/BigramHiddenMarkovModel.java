package languagemodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BigramHiddenMarkovModel implements PartOfSpeechTagger{
	private final String BEGINNING_OF_SENTENCE = "<s>";
	private final Map<String, Map<String, Integer>> tagBigrams; // which tag follows which and how often
	private final Map<String, Map<String, Integer>> tagToToken; // how often appears a token for given a tag
	private final ArrayList<double[]> probabilityMatrix;
	private final int vocabSize;
	private final long numSentences;
	private Map<String, Integer> tagToPosition;
	private Map<Integer, String> positionToTag; 
	
	public BigramHiddenMarkovModel(Corpus corpus) {
		this.vocabSize = corpus.getVocabularySize();
		this.numSentences = corpus.getNumSentences();
		this.tagBigrams = new HashMap<String, Map<String,Integer>>();
		this.tagToToken = new HashMap<String, Map<String,Integer>>();
		this.probabilityMatrix = new ArrayList<double[]>();
		
		for(Article a : corpus) {
			for(Sentence s : a) {
				String currToken = "";
				String prevToken = "";
				String currTag = "";
				String prevTag = "";
				
				for(Token token : s) {
					currToken = token.getText();
					currTag = token.getTag();
					
					if (prevToken.equals("")) {
						// only once at beginning of sentence
						prevTag = BEGINNING_OF_SENTENCE;
						if (tagBigrams.get(prevTag) == null) {
							tagBigrams.put(prevTag, new HashMap<String, Integer>());
						}	
					}
					
					if (!tagBigrams.containsKey(currTag)) {
						tagBigrams.put(currTag, new HashMap<String, Integer>());
					}
					Map<String, Integer> tagMap = tagBigrams.get(prevTag);
					if (tagMap.containsKey(currTag)) {
						tagMap.put(currTag, tagMap.get(currTag) + 1);
					} else {
						tagMap.put(currTag, 1);
					}
						
					if (!tagToToken.containsKey(currTag)) {
						tagToToken.put(currTag, new HashMap<String, Integer>());
					}
					Map<String, Integer> tokenMap = tagToToken.get(currTag);
					if (tokenMap.containsKey(currToken)) {
						tokenMap.put(currToken, tokenMap.get(currToken) + 1);
					} else {
						tokenMap.put(currToken, 1);
					}				
					
					prevToken = currToken;
					prevTag = currTag;
				}
			}
		}

	}
	
//	private void printTagMap(Map<String, Map<String, Integer>> tagToToken) {
//		for(String key : tagToToken.keySet()) {
//			System.out.println("KEY: " + key);
//			for(String key2 : tagToToken.get(key).keySet()) {
//				System.out.println("-- KEY2: " + key2);
//				System.out.println("-- value: " + tagToToken.get(key).get(key2));
//			}
//		}
//	}
//	private void printMap(Map<String, Map<String, Integer>> tagBigrams) {
//		for(String key : tagBigrams.keySet()) {
//			System.out.println("KEY: " + key);
//			for(String key2 : tagBigrams.get(key).keySet()) {
//				System.out.println("-- KEY2: " + key2);
//				System.out.println("-- value: " + tagBigrams.get(key).get(key2));
//			}
//		}
//		System.out.println("--------------------------------------------\n");
//	}
	
	public String[] determineMostLikelyTags(Sentence s) {
		String []tags = new String[s.getNumTokens()];
		tags = viterbiAlgorithm(s);
		return tags;
	}
	
	private String[] viterbiAlgorithm(Sentence s) {
		Set<String> tagSet = tagToToken.keySet();
		this.tagToPosition = new HashMap<String, Integer>();
		this.positionToTag = new HashMap<Integer, String>();
		ArrayList<String> optimalTagPath = new ArrayList<String>();
		int numTokens = tagBigrams.size();
				
		Iterator<String> iterator = tagSet.iterator();
		
		int position = 0;
		while (iterator.hasNext()) {
			String tag = iterator.next();
			tagToPosition.put(tag, position);
			positionToTag.put(position, tag);
			position++;
		}
		
		Iterator<Token> it = s.iterator();
		int sentenceLength = s.getNumTokens();

		for(int i = 0; i < sentenceLength; i++) {
			Token token = it.next();
			String currText = token.getText();
			this.probabilityMatrix.add(new double[tagSet.size()]); // one entry(vector) for each sentence
			
			if (i == 0) {
				// beginning of the sentence	
				String bestTag = determineMostLikelyTagAtBeginning(tagSet, currText, numTokens);
				optimalTagPath.add(bestTag);	
			} else {
				String bestTag = determineMostLikelyTagWithPriorProbability(tagSet, currText, i, numTokens);
				optimalTagPath.add(bestTag); // update optimal tag
			}
		}

		String[] optTags = new String[s.getNumTokens()];	
		optimalTagPath.toArray(optTags);
		return optTags;
	}
	
	private String determineMostLikelyTagAtBeginning(Set<String> tagSet, String currText, int numTokens) {
		double priorProbability = 1.0;
		double transitionProbability = 0.0;
		double probabilityTokenWithThisTag = 0.0;
		
		for(String tag : tagSet) {
			int transitionOccurrences = 0;
			if(tagBigrams.get(BEGINNING_OF_SENTENCE).containsKey(tag)) {
				transitionOccurrences = tagBigrams.get(BEGINNING_OF_SENTENCE).get(tag);
			}
			transitionProbability = (double)(transitionOccurrences + 1.0) / (numSentences + vocabSize);
			
			int tokenWithThisTagCount = 0;
			if (tagToToken.get(tag).containsKey(currText)) {
				tokenWithThisTagCount = tagToToken.get(tag).get(currText);		
			}	
			
			probabilityTokenWithThisTag = (double)(tokenWithThisTagCount + 1.0) / (numTokens + vocabSize);
			int tagPos = tagToPosition.get(tag);
			probabilityMatrix.get(0)[tagPos] = Math.log(priorProbability * transitionProbability * probabilityTokenWithThisTag);
		}
		
		// determine first tag by finding the maximum probability
		double max = Double.NEGATIVE_INFINITY;
		int bestTagPosition = 0;

		for(int i = 0; i < probabilityMatrix.get(0).length; i++) {
			double value = probabilityMatrix.get(0)[i];
			if(value > max) {
				max = value;
				bestTagPosition = i;
			}
		}

		String bestTag = positionToTag.get(bestTagPosition);
		return bestTag;
	}
	
	private String determineMostLikelyTagWithPriorProbability(Set<String> tagSet, String currText, int index, int numTokens) {
		double priorProbability = 0.0;
		double transitionProbability = 0.0;
		double probabilityTokenWithThisTag = 0.0;
		int prevIndex = index - 1;
		
		for(String tag : tagSet) {
			double maxProbability = Double.NEGATIVE_INFINITY;
			
			for(int i = 0; i < probabilityMatrix.get(prevIndex).length; i++) {
				priorProbability = probabilityMatrix.get(prevIndex)[i];
				String prevTag = positionToTag.get(i);
				
				int transitionOccurrences = 0;
				int denominator = 0;
				if(tagBigrams.get(prevTag).containsKey(tag)) {
					transitionOccurrences = tagBigrams.get(prevTag).get(tag);
					denominator = tagBigrams.get(prevTag).size();
				}
				
				transitionProbability = (double)(transitionOccurrences + 1.0) / (denominator + vocabSize);
				double probability = priorProbability * Math.log(transitionProbability);
				
				if (probability > maxProbability) {
					maxProbability = probability;
				}
				
			}
			
			int tokenWithThisTagCount = 0;
			if (tagToToken.get(tag).containsKey(currText)) {
				tokenWithThisTagCount = tagToToken.get(tag).get(currText);
			}
			
			probabilityTokenWithThisTag = (double)(tokenWithThisTagCount + 1.0) / (numTokens + vocabSize);
			double overallProbability = maxProbability * Math.log(probabilityTokenWithThisTag);
			// save probability for this position
			int tagPosition = tagToPosition.get(tag);
			probabilityMatrix.get(index)[tagPosition] = overallProbability;
		}
			
		double max = Double.NEGATIVE_INFINITY;
		int bestTagPosition = 0;
		for(int i = 0; i < probabilityMatrix.get(index).length; i++) {
			double probability = probabilityMatrix.get(index)[i];
			if (probability > max) {
				max = probability;
				bestTagPosition = i;
			}
		}
		
		String bestTag = positionToTag.get(bestTagPosition);
		return bestTag;
	}
}

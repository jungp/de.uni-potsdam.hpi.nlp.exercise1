package de.hpi.nlp.exercise1;

import java.util.HashMap;
import java.util.Map;

public class BaselineModel {
	private final Map<String, Map<String, Integer>> tokenPOSTags;
	
	public BaselineModel(Corpus corpus) {
		this.tokenPOSTags = new HashMap<String, Map<String, Integer>>();
		
		for(Article a : corpus) {
			for(Sentence s : a) {
				for(Token tok : s) {
					String tag = tok.getTag();
					String token = tok.getText();
					
					if (!tokenPOSTags.containsKey(token)) {
						tokenPOSTags.put(token, new HashMap<String, Integer>());
					}
					
					Map<String, Integer> tempMap = tokenPOSTags.get(token);

					if (tempMap.containsKey(tag)) {
						tempMap.put(tag, tempMap.get(tag) + 1);
					} else {
						tempMap.put(tag, 1);
					}
				}
			}	
		}
	}

	public String determineMostLikelyTag(Token tok) {
		String token = tok.getText();
		
		if (!tokenPOSTags.containsKey(token)) {
			// unknown words are by default tagged as a noun
			return "NN";
		} else {
			Map<String, Integer> tagMap = tokenPOSTags.get(token);
			
			if (tagMap.size() > 1) {
				int max = 0;
				String mostLikelyTag = "";
				
				// ambiguity, need to find the most likely tag
				for(String tag : tagMap.keySet()) { // TODO: very inefficient
					if(tagMap.get(tag) > max) {
						// if two tags have the same "probability", take first in alphabetical order
						max = tagMap.get(tag);
						mostLikelyTag = tag;
					}
				}
				return mostLikelyTag;
			} else {
				return tagMap.keySet().iterator().next();		
			}
		}
	}

}

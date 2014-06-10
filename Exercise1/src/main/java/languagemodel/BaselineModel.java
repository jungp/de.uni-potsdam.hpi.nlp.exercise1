package languagemodel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaselineModel implements PartOfSpeechTagger {
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

	public String[] determineMostLikelyTags(Sentence s) {
		String[] bestTags = new String[s.getNumTokens()];
		
		Iterator<Token> iterator = s.iterator();
		for(int i = 0; i < s.getNumTokens(); i++) {
			Token tok = iterator.next();
			String token = tok.getText();
			String bestTag = "";
			if (!tokenPOSTags.containsKey(token)) {
				// unknown words are by default tagged as a noun
				bestTag = "NN";
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
					bestTag = mostLikelyTag;
				} else {
					bestTag = tagMap.keySet().iterator().next();		
				}
			}
			bestTags[i] = bestTag;
		}
		return bestTags;
	}

}

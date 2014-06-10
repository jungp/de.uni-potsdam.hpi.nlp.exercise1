package de.hpi.nlp.languagemodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
					// ambiguity, need to find the most likely tag
					List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(tagMap.entrySet());
					// use custom comparator to order by values
					Collections.sort(list, new Comparator<Entry<String, Integer>>() {
						public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
							return o2.getValue().compareTo(o1.getValue());
						}
					});
					bestTag = list.get(0).getKey();
				} else {
					// unambiguousness, assign the only possible tag
					bestTag = tagMap.keySet().iterator().next();		
				}
			}
			bestTags[i] = bestTag;
		}
		return bestTags;
	}

}

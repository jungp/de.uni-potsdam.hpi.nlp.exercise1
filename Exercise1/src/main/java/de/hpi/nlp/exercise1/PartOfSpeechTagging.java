package de.hpi.nlp.exercise1;

public class PartOfSpeechTagging {
	
	public static double determine(BaselineModel model, Corpus corpus) {
		int correct = 0;
		long numTokens = corpus.getNumTokens();
		
		for(Article a : corpus) {
			for(Sentence s : a) {
				for(Token t : s) {
					// determine tag for a given word
					String actualTag = t.getTag();
					String tag = model.determineMostLikelyTag(t);

					if(tag.equals(actualTag)) {
						correct++;
					}
				}
			}
		}
		return (double)correct / numTokens;
	}
}

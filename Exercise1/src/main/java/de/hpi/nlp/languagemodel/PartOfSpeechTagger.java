package de.hpi.nlp.languagemodel;

public interface PartOfSpeechTagger {
	/*
	 * Should return the most likely tags for a given sentence.
	 */
	String[] determineMostLikelyTags(Sentence s);
}

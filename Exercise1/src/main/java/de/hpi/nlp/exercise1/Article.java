package de.hpi.nlp.exercise1;

import java.util.ArrayList;
import java.util.Iterator;

public class Article implements Iterable<Sentence>{
	private final ArrayList<Sentence> sentences = new ArrayList<Sentence>();
	
	public Iterator<Sentence> iterator() {
		return sentences.iterator();
	}
	
	public void addSentence(Sentence s) {
		sentences.add(s);
	}
}

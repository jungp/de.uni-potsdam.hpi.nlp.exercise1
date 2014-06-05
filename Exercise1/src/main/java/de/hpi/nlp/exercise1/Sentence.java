package de.hpi.nlp.exercise1;

import java.util.ArrayList;
import java.util.Iterator;

public class Sentence implements Iterable<String>{
	private final ArrayList<String> tokens;
	private int numTokens = 0;
	
	public Sentence() {
		tokens = new ArrayList<String>();
	}
	
	public void addToken(String token) {
		tokens.add(token);
		numTokens++;
	}

	public Iterator<String> iterator() {
		return tokens.iterator();
	}
	
	public int getNumTokens() {
		return numTokens;
	}
}

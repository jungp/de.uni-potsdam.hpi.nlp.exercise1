package de.hpi.nlp.languagemodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Corpus implements Iterable<Article>{
	private final ArrayList<Article> articles = new ArrayList<Article>();
	private final Set<String> vocabulary = new HashSet<String>();
	private long numSentences = 0;
	private long numTokens = 0;
	
	public void addArticle(Article a) {
		articles.add(a);
		for(Sentence s : a) {
			numSentences++;
			for(Token token : s) {
				vocabulary.add(token.getText());
				numTokens++;
			}
		}
	}
	
	public long getNumTokens() {
		return numTokens;
	}
	
	public long getNumSentences() {
		return numSentences;
	}

	public Iterator<Article> iterator() {
		return articles.iterator();
	}
	
	public int getVocabularySize() {
		return vocabulary.size();
	}

}

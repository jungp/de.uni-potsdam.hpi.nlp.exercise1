package de.hpi.nlp.languagemodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Corpus implements Iterable<Article>{
	private final ArrayList<Article> articles;
	private final Set<String> vocabulary;
	private long numSentences = 0;
	private long numTokens = 0;
	
	public Corpus() {
		this.articles = new ArrayList<Article>();
		this.vocabulary =  new HashSet<String>();
	}
	
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

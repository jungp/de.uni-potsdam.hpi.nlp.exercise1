import de.hpi.nlp.languagemodel.Article;
import de.hpi.nlp.languagemodel.BigramHiddenMarkovModel;
import de.hpi.nlp.languagemodel.Corpus;
import de.hpi.nlp.languagemodel.Sentence;
import de.hpi.nlp.languagemodel.Token;

import org.junit.Before;
import org.junit.Test;

import de.hpi.nlp.evaluation.PartOfSpeechTagging;


public class HiddenMarkovModelTest {
	private Corpus trainingCorpus;
	private Corpus testCorpus;
	
	@Before
	public void setUpTraining() {
		trainingCorpus = new Corpus();
		
		Sentence x = new Sentence();
		x.addToken(new Token("The", "IT"));
		x.addToken(new Token("man", "NN"));
		x.addToken(new Token("guy", "NN"));
		x.addToken(new Token("likes", "VB"));
		x.addToken(new Token("fish", "NN"));
		x.addToken(new Token(".", "PERIOD"));
		
		Sentence y = new Sentence();
		y.addToken(new Token("It", "OB"));
		y.addToken(new Token("smells", "VB"));
		y.addToken(new Token("like", "PRP"));
		y.addToken(new Token("fish", "NN"));
		
		Article a = new Article();
		Article b = new Article();
		
		a.addSentence(x);
		b.addSentence(y);
		
		trainingCorpus.addArticle(a);
		trainingCorpus.addArticle(b);
	}
	
	
	@Before
	public void setUpTest() {
		testCorpus = new Corpus();
		Sentence x = new Sentence();
		x.addToken(new Token("The", "IT"));
		x.addToken(new Token("man", "NN"));
		x.addToken(new Token("guy", "NN"));
		x.addToken(new Token("likes", "VB"));
		x.addToken(new Token("fish", "NN"));
		x.addToken(new Token(".", "PERIOD"));
		
		Sentence y = new Sentence();
		y.addToken(new Token("The", "IT"));
		y.addToken(new Token("man", "NN"));
		y.addToken(new Token("eats", "VB"));
		y.addToken(new Token("like", "PRP"));
		y.addToken(new Token("fish", "NN"));
	
//		Sentence z = new Sentence();
//		z.addToken(new Token("ajxhcjahc", "VB"));
//		z.addToken(new Token("afdoas", "NN"));
//		z.addToken(new Token("fas", "VB"));
//		z.addToken(new Token("asfsia", "PRP"));
//		z.addToken(new Token("iasffsa", "NN"));
		
		Article a = new Article();
		a.addSentence(x);
		a.addSentence(y);
//		a.addSentence(z);
		testCorpus.addArticle(a);
	}
		
	@Test
	public void test() {
		BigramHiddenMarkovModel model = new BigramHiddenMarkovModel(trainingCorpus);
		double taggerPrecision = PartOfSpeechTagging.determine(model, testCorpus);
		System.out.println("Precision: " + taggerPrecision);
	}
}

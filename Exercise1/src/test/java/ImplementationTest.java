import org.junit.Test;

import de.hpi.nlp.exercise1.Article;
import de.hpi.nlp.exercise1.Corpus;
import de.hpi.nlp.exercise1.Sentence;


public class ImplementationTest {
	
	@Test
	public void test() {
		Sentence x = new Sentence();
		x.addToken("Hello");
		x.addToken("it's");
		x.addToken("me");
		x.addToken("Mario");
		x.addToken(".");

		Corpus c = new Corpus();
		Article a = new Article();
		
		a.addSentence(x);
		a.addSentence(x);
		c.addArticle(a);
		
		for(Article ar : c) {
			for(Sentence s : ar) {
				for(String token : s) {
					System.out.println(token);
				}
			}
		}
	}
}

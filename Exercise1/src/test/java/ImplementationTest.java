import org.junit.Test;

import de.hpi.nlp.languagemodel.Article;
import de.hpi.nlp.languagemodel.Corpus;
import de.hpi.nlp.languagemodel.Sentence;
import de.hpi.nlp.languagemodel.Token;


public class ImplementationTest {
	
	@Test
	public void test() {
		Sentence x = new Sentence();
		x.addToken(new Token("Hello", ""));
		x.addToken(new Token("it's", ""));
		x.addToken(new Token("me", ""));
		x.addToken(new Token("Mario", ""));
		x.addToken(new Token(".", ""));

		Corpus c = new Corpus();
		Article a = new Article();
		
		a.addSentence(x);
		a.addSentence(x);
		c.addArticle(a);
		
		for(Article ar : c) {
			for(Sentence s : ar) {
				for(Token token : s) {
					System.out.println(token.getText());
				}
			}
		}
	}
}

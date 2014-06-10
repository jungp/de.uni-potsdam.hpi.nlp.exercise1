import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import de.hpi.nlp.evaluation.PartOfSpeechTagging;
import de.hpi.nlp.languagemodel.Article;
import de.hpi.nlp.languagemodel.BaselineModel;
import de.hpi.nlp.languagemodel.Corpus;
import de.hpi.nlp.languagemodel.Sentence;
import de.hpi.nlp.languagemodel.Token;

/**
 * Test to play around with tags and see if tagger reacts as expected.
 * @author pjung
 *
 */
public class BaselineModelTest {
	
	@Test
	public void taggingTest() throws FileNotFoundException, XMLStreamException, URISyntaxException {
		
		// training corpus
		Corpus training = new Corpus();
		
		Sentence st = new Sentence();
		Token tt1 = new Token("This", "DT2");
		Token tt2 = new Token("is", "VB");
		Token tt3 = new Token("is", "VBZ");
		Token tt4 = new Token("is", "VB");
		Token tt5 = new Token("good", "ADJ");
		Token tt6 = new Token("This", "DT1");
		st.addToken(tt1);
		st.addToken(tt2);
		st.addToken(tt3);
		st.addToken(tt4);
		st.addToken(tt5);
		st.addToken(tt6);
		
		Article art = new Article();
		art.addSentence(st);
		training.addArticle(art);
		
		// test corpus
		Corpus test = new Corpus();
		
		Sentence s = new Sentence();
		Token t1 = new Token("This", "DT");
		Token t2 = new Token("is", "VBZ");
		Token t3 = new Token("good", "JJ");
		Token t4 = new Token("news", "NN");
		Token t5 = new Token(".", "PERIOD");
		s.addToken(t1);
		s.addToken(t2);
		s.addToken(t3);
		s.addToken(t4);
		s.addToken(t5);
		
		Article a = new Article();
		a.addSentence(s);
		test.addArticle(a);
		
		// learn model and determine tags
		BaselineModel model = new BaselineModel(training);
		double taggerPrecision = PartOfSpeechTagging.determine(model, test);
		System.out.println("Precision: " + taggerPrecision);	
	}
}

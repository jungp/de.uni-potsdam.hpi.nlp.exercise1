import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;

import languagemodel.Article;
import languagemodel.BaselineModel;
import languagemodel.Corpus;
import languagemodel.Sentence;
import languagemodel.Token;

import org.junit.Test;

import evaluation.PartOfSpeechTagging;


public class PartOfSpeechTagTest {
	
	@Test
	public void test() {
		Sentence s = new Sentence();
		Token t1 = new Token("This", "tag1");
		Token t2 = new Token("is", "tag2");
		Token t3 = new Token("good", "tag3");
		Token t4 = new Token("news", "tag4");
		Token t5 = new Token(".", "tag5");
		s.addToken(t1);
		s.addToken(t2);
		s.addToken(t3);
		s.addToken(t4);
		s.addToken(t5);
		
		for(Token token : s) {
			System.out.println(token.getText() + ", " + token.getTag());
		}
	}
	
	@Test
	public void taggingTest() throws FileNotFoundException, XMLStreamException, URISyntaxException {
//		File uri = new File(Exercise2.class.getResource("/GENIA_treebank_v1").toURI());
//		Corpus corpus = CorpusParser.parse(uri);
//		
//		TrainingAndTestSplitter splitter = new TrainingAndTestSplitter(corpus, 0.9);
//		Corpus training = splitter.getTrainingCorpus();
		
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
		
		Corpus training = new Corpus();
		training.addArticle(art);
		
		
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
		
		BaselineModel model = new BaselineModel(training);
		double taggerPrecision = PartOfSpeechTagging.determine(model, test);
		System.out.println("Precision: " + taggerPrecision);	
	}
}

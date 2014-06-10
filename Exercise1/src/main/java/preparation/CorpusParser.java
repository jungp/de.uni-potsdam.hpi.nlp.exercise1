package preparation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import languagemodel.Article;
import languagemodel.Corpus;
import languagemodel.Sentence;
import languagemodel.Token;

import org.apache.commons.io.FilenameUtils;

public class CorpusParser {

	public static Corpus parse(File uri) throws FileNotFoundException, XMLStreamException {
		Corpus c = new Corpus();
		Sentence s = null;
		Article a = null;
		
		for (File file : uri.listFiles()) {
			if (file.isFile()) {
				
				FileInputStream stream = new FileInputStream(file.getAbsolutePath());
				String ext = FilenameUtils.getExtension(file.getAbsolutePath());

				if (ext.equals("xml")) {
					XMLInputFactory factory = XMLInputFactory.newInstance();
					XMLStreamReader parser = factory.createXMLStreamReader(stream);
					String tag = "";
					
					// read from stream
					while (parser.hasNext()) {
						switch (parser.getEventType()) {

						case XMLStreamConstants.START_ELEMENT:
							String posTag = parser.getAttributeValue(null, "cat");
							tag = parser.getLocalName();
							if (tag.equals("Article")) {
								a = new Article();
							} else if (tag.equals("sentence")) {
								s = new Sentence();
							} else if (tag.equals("tok")) {	
								String word = parser.getElementText(); 
								Token t = new Token(word, posTag);
								s.addToken(t);
							}
							break;

						case XMLStreamConstants.END_ELEMENT:
							tag = parser.getLocalName();
							if (tag.equals("Article")) {
								c.addArticle(a);
							} else if (tag.equals("sentence")) {
								a.addSentence(s);
							}
							break;

						default:
							break;
						}

						parser.next();
					}
				}

			}
		}
		return c;
	}
}

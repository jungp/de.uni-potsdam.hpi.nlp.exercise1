package preparation;

import java.util.Random;

import languagemodel.Article;
import languagemodel.Corpus;

public class TrainingAndTestSplitter {
	private final Corpus training;
	private final Corpus test;
	
	public TrainingAndTestSplitter(Corpus corpus, double trainingFactor) {
		training = new Corpus();
		test = new Corpus();
		Random rnd = new Random();	

		for (Article article : corpus) {
			int random = rnd.nextInt(10) + 1;
			
			if (random <= trainingFactor * 10) {
				// training data
				training.addArticle(article);
			} else {
				// test data
				test.addArticle(article);
			}
			
		}
	}
	
	public Corpus getTrainingCorpus() {
		return training;
	}
	
	public Corpus getTestCorpus() {
		return test;
	}
}

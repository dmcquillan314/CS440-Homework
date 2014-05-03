import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import com.hw6.document.classification.helpers.EvaluationHelper;
import com.hw6.document.classification.helpers.InstancesHelper;
import com.hw6.document.classification.helpers.StopWordsHelper;
import com.hw6.documentFrequency.InverseDocumentFrequencyService;

import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.output.prediction.XML;
import weka.classifiers.meta.Bagging;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Stopwords;


public class ClassifierRunner {

	private final static String TRAINING_FILE = "hw6data/train.arff";
	private final static String TESTING_FILE = "hw6data/test.arff";
	private final static String APPLICATION_FILE = "hw6data/application.arff";
	
	public static void main(String[] args) {

		try {
			File trainingFile = new File(TRAINING_FILE);
			File testingFile = new File(TESTING_FILE);
			File applicationFile = new File(APPLICATION_FILE);
			
			Instances trainingInstances = InstancesHelper.createInstancesWithFile(trainingFile); 
			Instances testingInstances = InstancesHelper.createInstancesWithFile(testingFile); 
			Instances applicationInstances = InstancesHelper.createInstancesWithFile(applicationFile); 
			
			InverseDocumentFrequencyService documentFrequencies = new InverseDocumentFrequencyService(trainingInstances, 1.0);
			Double threshold = 0.0;
			Integer maxStopWords = 1000;
			
			Stopwords stopwords = StopWordsHelper.findStopWords(trainingInstances, documentFrequencies.getStatistics(), threshold, maxStopWords);
			trainingInstances = InstancesHelper.filterInstancesByStopwords( trainingInstances, stopwords );
			testingInstances = InstancesHelper.filterInstancesByStopwords( testingInstances, stopwords );
			applicationInstances = InstancesHelper.filterInstancesByStopwords( applicationInstances, stopwords );
			
			NaiveBayesMultinomial naiveBayesClassifier = new NaiveBayesMultinomial();
			
			Bagging baggingClassifierWrapper = new Bagging();
			baggingClassifierWrapper.setClassifier(naiveBayesClassifier);
			baggingClassifierWrapper.buildClassifier(trainingInstances);
			
			StringBuffer internalStringBuffer = new StringBuffer();
			XML internalOutput = new XML();
			internalOutput.setBuffer(internalStringBuffer);
			internalOutput.setHeader(trainingInstances);
			internalOutput.setOutputDistribution(true);
			
			Evaluation evaluation = new Evaluation(trainingInstances);
			evaluation.crossValidateModel(baggingClassifierWrapper, trainingInstances, 3, new Random( new Date().getTime() ), internalOutput );
			
			EvaluationHelper.evaluateAndPrintResults(baggingClassifierWrapper, trainingInstances, trainingInstances);
			EvaluationHelper.evaluateAndPrintResults(baggingClassifierWrapper, trainingInstances, testingInstances);
			
			Iterator<Instance> instanceIterator = applicationInstances.listIterator();
			File file = new File("dmcquill_application_result.txt");
			if( file.exists() ) {
				file.delete();
			}
			BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter( file, true ) );
			
			while( instanceIterator.hasNext() ) {
				Instance instance = instanceIterator.next();
				String line = baggingClassifierWrapper.classifyInstance(instance) + ( instanceIterator.hasNext() ? "\n" : "" );
				bufferedWriter.append(line);
			}
			
			bufferedWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

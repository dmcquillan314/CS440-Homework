package hw4.weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.output.prediction.XML;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;

public class MainP4 {

	public static void main(String[] args) {
		try {
			
			File file = new File("glass.arff");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			
			
			Instances instances = new Instances(bufferedReader);
			
			bufferedReader.close();
			
			final int numInstances = instances.numAttributes();			
			instances.setClassIndex(numInstances - 1);
			
			Evaluation evaluation = new Evaluation(instances);
			final Integer folds = 10;

			StringBuilder applicationOutput = new StringBuilder();

			applicationOutput.append("Begin problem 3 part a\n");
			applicationOutput.append("folds: " + folds + "\n\n");

				
			StringBuffer internalStringBuffer = new StringBuffer();
			XML internalOutput = new XML();
			internalOutput.setBuffer(internalStringBuffer);
			internalOutput.setHeader(instances);
			internalOutput.setOutputDistribution(true);
			
//			percep.setGUI(true);
//			percep.buildClassifier(instances);
			
			MultilayerPerceptron percep = new MultilayerPerceptron();
			percep.setHiddenLayers("10");
			Bagging bagging = new Bagging();
			bagging.setClassifier(percep);
			evaluation.crossValidateModel(bagging, instances, folds, new Random( new Date().getTime() ), internalOutput );
			applicationOutput.append("Accuracy: " + evaluation.pctCorrect() + "%\n");

			MultilayerPerceptron percep2 = new MultilayerPerceptron();
			percep2.setHiddenLayers("10,5");
			Bagging bagging2 = new Bagging();
			bagging2.setClassifier(percep);
			evaluation.crossValidateModel(bagging2, instances, folds, new Random( new Date().getTime() ), internalOutput );
			applicationOutput.append("Accuracy: " + evaluation.pctCorrect() + "%\n");

			MultilayerPerceptron percep3 = new MultilayerPerceptron();
			percep3.setHiddenLayers("5,10,15,20");
			Bagging bagging3 = new Bagging();
			bagging3.setClassifier(percep);
			evaluation.crossValidateModel(bagging3, instances, folds, new Random( new Date().getTime() ), internalOutput );
			applicationOutput.append("Accuracy: " + evaluation.pctCorrect() + "%\n");

			MultilayerPerceptron percep4 = new MultilayerPerceptron();
			percep4.setHiddenLayers("5,10,15,20,15,10,5,2");
			Bagging bagging4 = new Bagging();
			bagging4.setClassifier(percep);
			evaluation.crossValidateModel(bagging4, instances, folds, new Random( new Date().getTime() ), internalOutput );
			applicationOutput.append("Accuracy: " + evaluation.pctCorrect() + "%\n");

			System.out.println(applicationOutput.toString());
			
			System.out.println("complete.");

			applicationOutput.append("Begin problem 4 part a\n");
			applicationOutput.append("folds: " + folds + "\n\n");

				
			Bagging percepTest = new Bagging();
//			percep.setHiddenLayers("10");
//			percep.setGUI(true);
			percepTest.buildClassifier(instances);
			
			evaluation.crossValidateModel(percepTest, instances, folds, new Random( new Date().getTime() ), internalOutput );
			applicationOutput.append("Accuracy: " + evaluation.pctCorrect() + "%\n");

			applicationOutput.append(
						"After conducting the same trial as problem 3 a with the same " +
					    "parameters for the amount of hidden layers I obtained results " +
						"contrary to what I was expecting.  This shows that the training " +
					    "of the classifier using the neural network was most likely overfitting " +
						"from the training data.  However, using the bagging technique it resulted in " +
					    "a much lower accuracy which could just mean that the training data is insufficient " +
						"or simply that a bad set of parameters for the hidden layers were used. The evidence of the claim " +
					    "is shown in problem 4 part a.  Also below is a graph plotting the results of problem 3 part a to " +
						"the results of problem 4 part a: "
					);
			
			System.out.println(applicationOutput.toString());
			
			System.out.println("complete.");

		} catch (IOException e) {
			System.err.println( e );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

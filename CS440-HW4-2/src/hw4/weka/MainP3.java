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
import weka.core.Instances;

public class MainP3 {

	/*
	 * Training a neural net
	 * - Compute the Delta values for the output units, 
	 * using the observed error from the training examples.  
	 * Starting with the output layer we then repeat the following 
	 * for each layer in the network until the earliest layer is reached:
	 *  - Propagate the Delta values back to the previous layer.
	 *  - Update the weights between the two layers.
	 *  
	 *  How can the number of layers in a topology affect the result
	 *  - If the optimal amount of layers are used it can result in increased
	 *    accuracy of the training data and the testing data as well.  However,
	 *    if too many layers are used it can result in overfitting of the data.
	 *    Consequently, if too few layers are used it can result in underfitting.
	 */
	
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
			
			MultilayerPerceptron percep = new MultilayerPerceptron();
			percep.setHiddenLayers("10");
//			percep.setGUI(true);
//			percep.buildClassifier(instances);
			
			evaluation.crossValidateModel(percep, instances, folds, new Random( new Date().getTime() ), internalOutput );
			applicationOutput.append("Accuracy: " + evaluation.pctCorrect() + "%\n");
			percep.setHiddenLayers("10,5");
//			percep.setGUI(true);
//			percep.buildClassifier(instances);
			
			evaluation.crossValidateModel(percep, instances, folds, new Random( new Date().getTime() ), internalOutput );
			applicationOutput.append("Accuracy: " + evaluation.pctCorrect() + "%\n");
			percep.setHiddenLayers("5,10,15,20");
//			percep.setGUI(true);
//			percep.buildClassifier(instances);
			
			evaluation.crossValidateModel(percep, instances, folds, new Random( new Date().getTime() ), internalOutput );
			applicationOutput.append("Accuracy: " + evaluation.pctCorrect() + "%\n");
			percep.setHiddenLayers("5,10,15,20,15,10,5,2");
//			percep.setGUI(true);
//			percep.buildClassifier(instances);
			
			evaluation.crossValidateModel(percep, instances, folds, new Random( new Date().getTime() ), internalOutput );
			applicationOutput.append("Accuracy: " + evaluation.pctCorrect() + "%\n");

			System.out.println(applicationOutput.toString());
			
			System.out.println("complete.");

		} catch (IOException e) {
			System.err.println( e );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

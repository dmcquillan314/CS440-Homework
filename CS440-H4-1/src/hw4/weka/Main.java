package hw4.weka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class Main {

	public static void main(String[] args) {

		try {
			File file = new File("DataSet1.arff");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			
			Instances instances = new Instances(bufferedReader);
			
			bufferedReader.close();

			int numInstances = instances.numInstances();

			instances.setClassIndex(instances.numAttributes() - 1);
			
			MultilayerPerceptron percep = new MultilayerPerceptron();
			Instances trainingData = new Instances(instances, 0, (int) (numInstances * 0.8) );
			
			percep.buildClassifier(trainingData);
			
			
			Instances testingData = new Instances(
							instances,
							(int) (numInstances * 0.8),
							numInstances - (int) (numInstances * 0.8)
						);
			
			double correct = 0.0;
			double incorrect = 0.0;
			
			for( int i = 0; i < testingData.numInstances(); i++) {
				double assignedClass = percep.classifyInstance(testingData.instance(i));
				double originalClass = testingData.instance(i).classValue();
				
				if( assignedClass == originalClass ) {
					correct++;
				} else {
					incorrect++;
				}
			}
			
			Double accuracy = 100.0 * correct / (correct + incorrect);
			System.out.printf("%f\n", accuracy); // Accuracy
			
			File outputFile = new File("dmcquil2.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			writer.append(accuracy.toString());
			writer.close();
			
		} catch (IOException e) {
			System.err.println( e );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

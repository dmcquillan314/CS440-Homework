package hw4.weka;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.output.prediction.XML;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class Main {

	public static void main(String[] args) {

		try {
			File file = new File("data/glass.arff");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			
			Instances instances = new Instances(bufferedReader);
			
			bufferedReader.close();
			
			StringBuilder applicationOutput = new StringBuilder();
			applicationOutput.append("**************************\n").append("**************************\n").append("**************************\n")
							 .append("Begin Part A\n")
							 .append("**************************\n").append("**************************\n").append("**************************\n");
			// Part a
			double[] nValues = new double[] { 
					10.0,
					30.0,
					50.0,
					70.0,
					90.0
			};
			
			Map<Double, ClassifierWrapper> classificationResults = new HashMap<Double, ClassifierWrapper>();
			double accuracyArray[] = new double[5];
			double trainingAccuracyArray[] = new double[5];
			int i = 0;
			for( Double N : nValues ) {
				
				ClassifierWrapper wrapper = new ClassifierWrapper(
						null,
						null,
						new MultilayerPerceptronClassifier(instances, N)
					);
				
				wrapper.setTrainingAccuracy( wrapper.getClassifier().classify() );
				wrapper.setTestingAccuracy( wrapper.getClassifier().test( instances, 100.0 ) );
				classificationResults.put( N, wrapper );

				trainingAccuracyArray[i] = wrapper.getTrainingAccuracy();
				accuracyArray[i++] = wrapper.getTestingAccuracy();
				
				applicationOutput.append( wrapper.toString() ).append("\n");
			}	
			
//			Plot2DPanel plot = new Plot2DPanel();
//			plot.addLinePlot("Training Accuracy", Color.GREEN, nValues, trainingAccuracyArray);
//			plot.addLinePlot("Testing Accuracy", Color.darkGray, nValues, accuracyArray);
//			plot.setLegendOrientation("NORTH");
//			JFrame frame = new JFrame("Problem 1a Results");
//			plot.setAxisLabels(new String[] { "N", "Accuracy" });
//			frame.setContentPane(plot);
//			frame.setVisible(true);
//			frame.setSize(800, 600);
			
			applicationOutput.append("The result is as expected since with each value of N the amount of training examples is increasing." + 
									"Therefore, with each next value of N the accuracy will also increase since the bias is increasing.\n");
			
			applicationOutput.append("**************************\n").append("**************************\n").append("**************************\n")
							 .append("End Part A\n")
							 .append("**************************\n").append("**************************\n").append("**************************\n\n");
			
			applicationOutput.append("**************************\n").append("**************************\n").append("**************************\n")
							 .append("Begin Part B\n")
							 .append("**************************\n").append("**************************\n").append("**************************\n");
							
			// Part b			
			int[] foldsArray = new int[] {
					2,
					5,
					10,
					15,
					20
			};
			
			double[] foldsDoubleArray = new double[] {
					2,
					5,
					10,
					15,
					20
			};
			
			Instances trainInstances = instances;
			Map<Integer, Tuple3<StringBuffer, Evaluation, Double>> results = new HashMap<Integer, Tuple3<StringBuffer, Evaluation, Double>>();
			for( final Integer folds : foldsArray ) {
				Tuple3<StringBuffer, Evaluation, Double> tuple3 = new Tuple3<StringBuffer, Evaluation, Double>();
				
				StringBuffer internalStringBuffer = new StringBuffer();
				XML internalOutput = new XML();
				internalOutput.setBuffer(internalStringBuffer);
				internalOutput.setHeader(trainInstances);
				internalOutput.setOutputDistribution(true);

				Evaluation evaluation = new Evaluation(trainInstances);
				
				MultilayerPerceptron percep = new MultilayerPerceptron();
				Date beforeCrossValidate = new Date();
				evaluation.crossValidateModel(percep, trainInstances, folds, new Random( new Date().getTime() ), internalOutput );
				Date afterCrossValidate = new Date();
				
				Double timeTakenCrossValidation =  ((double)( afterCrossValidate.getTime() - beforeCrossValidate.getTime())) / 1000.0;
				
				tuple3.setFirst( internalStringBuffer );
				tuple3.setSecond( evaluation );
				tuple3.setThird( timeTakenCrossValidation );
				
				results.put(folds, tuple3);
			}
			List<Integer> keySet = new ArrayList<Integer>();
			for( Integer key : results.keySet() ) {
				keySet.add(key);
			}
			Collections.sort(keySet);
			accuracyArray = new double[5];
			double timeTakenArray[] = new double[5];
			i = 0;
			for( final Integer key : keySet ) {
				Tuple3<StringBuffer, Evaluation, Double> result = results.get(key);
				File outputFile = new File("result/cross-validation-output-" + key + ".xml");
				if( outputFile.exists() ) {
					outputFile.delete();
				}
				
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
				bufferedWriter.write(result.getFirst().toString());
				bufferedWriter.close();
				
				timeTakenArray[i] = result.getThird();
				accuracyArray[i++] = result.getSecond().pctCorrect();
				
				applicationOutput.append("***********************************\nCross Validation Result\n***********************************\n");
				applicationOutput.append("Results for N = ").append(key).append("\n");
				applicationOutput.append("Time taken: " + result.getThird() + "s\n");
				applicationOutput.append("Percent Correct: " + result.getSecond().pctCorrect() + "\n");
				applicationOutput.append("Percent Incorrect: " + result.getSecond().pctIncorrect() + "\n");
				applicationOutput.append("Confusion Matrix: \n" + result.getSecond().confusionMatrix() + "\n\n");
//				applicationOutput.append("------------\nXML Data\n------------\n\n").append( result.getFirst().toString() ).append("\n\n");
			}
			
			Plot2DPanel plot2 = new Plot2DPanel();
			plot2.addLinePlot("n vs. Accuracy", Color.DARK_GRAY, foldsDoubleArray, accuracyArray);
			JFrame frame2 = new JFrame("Problem 1b Results");
			plot2.setAxisLabels(new String[] { "N", "Accuracy" });
			frame2.setContentPane(plot2);
			frame2.setVisible(true);
			frame2.setSize(800, 600);

			
			Plot2DPanel plot3 = new Plot2DPanel();
			plot3.addLinePlot("n vs. Time Taken by Cross Validation", Color.DARK_GRAY, foldsDoubleArray, timeTakenArray);
			JFrame frame3 = new JFrame("Problem 1b Results");
			plot3.setAxisLabels(new String[] { "n", "Time Taken(s)" });
			frame3.setContentPane(plot3);
			frame3.setVisible(true);
			frame3.setSize(800, 600);

			applicationOutput.append("The advantage of using n-fold cross validation is simply that " +
									"the learning of the percentage of data to be used is also learned by k-fold cross validation.\n\n");
			
			applicationOutput.append("The advantages of using a large n is you will get a closer value of the percentage of the instances you " +
									"should use to the optimal value.  However the disadvantage comes with the processing time of the cross validation " +
									"as shown in the graph from part b.ii.\n\n");
			
			applicationOutput.append("**************************\n").append("**************************\n").append("**************************\n")
							 .append("End Part B\n")
							 .append("**************************\n").append("**************************\n").append("**************************\n");
		
			System.out.println(applicationOutput.toString());
		} catch (IOException e) {
			System.err.println( e );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

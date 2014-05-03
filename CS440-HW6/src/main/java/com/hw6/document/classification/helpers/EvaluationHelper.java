package com.hw6.document.classification.helpers;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

public class EvaluationHelper {

	public static void evaluateAndPrintResults( Classifier classifier, Instances trainingInstances, Instances testingInstances ) throws Exception {
		Evaluation evaluation = new Evaluation(trainingInstances);
		evaluation.evaluateModel(classifier, testingInstances);
		
		String summary = evaluation.toSummaryString();
		System.out.println(summary);
		 
		// Get the confusion matrix
		double[][] cmMatrix = evaluation.confusionMatrix();
		
		for( int i = 0; i < cmMatrix.length; i++ ) {
			StringBuilder stringBuilder = new StringBuilder();
			for( int j = 0; j < cmMatrix[i].length; j++ ) {
				stringBuilder.append(cmMatrix[i][j] + " ");
			}
			System.out.println(stringBuilder.toString());
		}

	}
	
}

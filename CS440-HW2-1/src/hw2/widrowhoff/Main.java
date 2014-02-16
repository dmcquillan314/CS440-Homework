package hw2.widrowhoff;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		final String dataSet1Csv = "DataSet1.csv";
		final String dataSet2Csv = "DataSet2.csv";
		final String dataSet3Csv = "DataSet3.csv";
		
		DataSet dataSet1 = new DataSet(dataSet1Csv);
		DataSet dataSet2 = new DataSet(dataSet2Csv);
		DataSet dataSet3 = new DataSet(dataSet3Csv);
		
		WidrowHoffLearner learner = new WidrowHoffLearner( dataSet2, 0.0 );
		learner.trainWeightVector();
		
		
		
		for( int i = 0; i < learner.getWeights().length; i++ ) {
			System.out.println("\\( w_{" + (i+1) + "} \\rightarrow " + learner.getWeights()[i] + " \\) \\\\");
		}
		
		System.out.println("------------------------");
		System.out.println("Data set 1 test");
		System.out.println("------------------------");
		learner.testWeightVector(dataSet1);
		System.out.println("------------------------");
		System.out.println("Data set 2 test");
		System.out.println("------------------------");
		learner.testWeightVector(dataSet2);
		
		System.out.println("------------------------");
		System.out.println("Data set 3 application");
		System.out.println("------------------------");
		
		final List<Double> classifications = learner.applyWeightVector(dataSet3);
		for( int i = 0; i < classifications.size(); i++ ) {
			System.out.println( "\\( " + (i+1) + " \\rightarrow " + classifications.get(i) + " \\) \\\\" );
		}
		
	}

}

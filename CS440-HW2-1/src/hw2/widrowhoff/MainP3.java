package hw2.widrowhoff;

import java.util.List;

public class MainP3 {
	public static void main(String[] args) {
		final String dataSet1Csv = "DataSet1.csv";
		final String dataSet2Csv = "DataSet2.csv";
		final String dataSet3Csv = "DataSet3.csv";
		
		DataSet dataSet1 = new DataSet(dataSet1Csv);
		double[] dataSet1ListMax = VectorUtils.getListMax( dataSet1 );
		DataSet dataSet2 = new DataSet(dataSet2Csv);
		double[] dataSet2ListMax = VectorUtils.getListMax( dataSet2 );
		System.out.println("DataSet2 List Max: " + VectorUtils.vectorToString(dataSet2ListMax));
		
		DataSet dataSet1x = VectorUtils.normalize( new DataSet( dataSet1Csv ) );
		DataSet dataSet2x = VectorUtils.normalize( new DataSet( dataSet2Csv ) );
		DataSet dataSet3x = VectorUtils.normalize( new DataSet( dataSet3Csv ) );
		
		WidrowHoffLearner learner = new WidrowHoffLearner( dataSet1x, 0.0 );
		learner.trainWeightVector();
		
		for( int i = 0; i < learner.getWeights().length; i++ ) {
			System.out.println("\\( w_{" + (i+1) + "} \\rightarrow " + learner.getWeights()[i] + " \\) \\\\");
		}
		
		System.out.println("------------------------");
		System.out.println("Data set 1x test");
		System.out.println("------------------------");
		learner.testWeightVector(dataSet1x);
		System.out.println("------------------------");
		System.out.println("Data set 2x test");
		System.out.println("------------------------");
		learner.testWeightVector(dataSet2x);
		
		System.out.println("------------------------");
		System.out.println("Data set 3x application");
		System.out.println("------------------------");
		
		final List<Double> classifications = learner.applyWeightVector(dataSet3x);
		for( int i = 0; i < classifications.size(); i++ ) {
			System.out.println( "\\( " + (i+1) + " \\rightarrow " + classifications.get(i) + " \\) \\\\" );
		}
		
		System.out.println("------------------------");
		System.out.println("Data set 1 test");
		System.out.println("------------------------");
		learner.testWeightVector(dataSet1);
		
		double[] testWeights = learner.getWeights().clone();
		for( int i = 0; i < testWeights.length; i++ ) {
			testWeights[i] = testWeights[i] * dataSet1ListMax[i];
		}
		
		WidrowHoffLearner learnerTest = new WidrowHoffLearner( dataSet1, testWeights, learner.getThreshold() );
		learnerTest.trainWeightVector();
		for( int i = 0; i < learner.getWeights().length; i++ ) {
			System.out.println("\\( w_{" + (i+1) + "} \\rightarrow " + learner.getWeights()[i] + " \\) \\\\");
		}
	
	}
	
	

}

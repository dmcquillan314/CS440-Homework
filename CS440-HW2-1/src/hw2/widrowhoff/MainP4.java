package hw2.widrowhoff;

import java.util.ArrayList;
import java.util.List;

public class MainP4 {
	public static void main(String[] args) {
		final String dataSet1Csv = "DataSet1.csv";
		final String dataSet2Csv = "DataSet2.csv";
		
		DataSet dataSet1 = new DataSet( dataSet1Csv );
		DataSet dataSet2 = new DataSet( dataSet2Csv );
		DataSet dataSet4 = new DataSet( dataSet2Csv, true );
		DataSet dataSet5 = new DataSet( dataSet2Csv, true );
		DataSet dataSet6 = new DataSet( dataSet2Csv, true );
		
		System.out.println("------------------------");
		System.out.println("Data set 2 training");
		System.out.println("------------------------");
		WidrowHoffLearner learner2 = new WidrowHoffLearner( dataSet2, 0.0 );
		learner2.trainWeightVector();
		for( int i = 0; i < learner2.getWeights().length; i++ ) {
			System.out.println("\\( w_{" + (i+1) + "} \\rightarrow " + learner2.getWeights()[i] + " \\) \\\\");
		}
		
		System.out.println("------------------------");
		System.out.println("Data set 4 training");
		System.out.println("------------------------");
		WidrowHoffLearner learner4 = new WidrowHoffLearner( dataSet4, 0.0 );
		learner4.trainWeightVector();
		for( int i = 0; i < learner4.getWeights().length; i++ ) {
			System.out.println("\\( w_{" + (i+1) + "} \\rightarrow " + learner4.getWeights()[i] + " \\) \\\\");
		}
		System.out.println("------------------------");
		System.out.println("Data set 5 training");
		System.out.println("------------------------");
		WidrowHoffLearner learner5 = new WidrowHoffLearner( dataSet5, 0.0 );
		learner5.trainWeightVector();
		for( int i = 0; i < learner5.getWeights().length; i++ ) {
			System.out.println("\\( w_{" + (i+1) + "} \\rightarrow " + learner5.getWeights()[i] + " \\) \\\\");
		}
		System.out.println("------------------------");
		System.out.println("Data set 6 training");
		System.out.println("------------------------");
		WidrowHoffLearner learner6 = new WidrowHoffLearner( dataSet6, 0.0 );
		learner6.trainWeightVector();
		for( int i = 0; i < learner6.getWeights().length; i++ ) {
			System.out.println("\\( w_{" + (i+1) + "} \\rightarrow " + learner6.getWeights()[i] + " \\) \\\\");
		}
		
		
		System.out.println("------------------------");
		System.out.println("Data set 1 test dataset 2");
		System.out.println("------------------------");
		learner2.testWeightVector( dataSet1 );
		
		System.out.println("------------------------");
		System.out.println("Data set 1 test dataset 4");
		System.out.println("------------------------");
		learner4.testWeightVector( dataSet1 );
		
		System.out.println("------------------------");
		System.out.println("Data set 1 test dataset 5");
		System.out.println("------------------------");
		learner5.testWeightVector( dataSet1 );
		
		System.out.println("------------------------");
		System.out.println("Data set 1 test dataset 6");
		System.out.println("------------------------");
		learner6.testWeightVector( dataSet1 );
		
		double[] dataSet2UnitVector = VectorUtils.unitVector(VectorUtils.concat(new double[] { learner2.getThreshold() }, learner2.getWeights()));
		double[] dataSet4UnitVector = VectorUtils.unitVector(VectorUtils.concat(new double[] { learner4.getThreshold() }, learner4.getWeights()));
		double[] dataSet5UnitVector = VectorUtils.unitVector(VectorUtils.concat(new double[] { learner5.getThreshold() }, learner5.getWeights()));
		double[] dataSet6UnitVector = VectorUtils.unitVector(VectorUtils.concat(new double[] { learner6.getThreshold() }, learner6.getWeights()));

		List<double[]> vectors = new ArrayList<double[]>();
		vectors.add(dataSet2UnitVector);
		vectors.add(dataSet4UnitVector);
		vectors.add(dataSet5UnitVector);
		vectors.add(dataSet6UnitVector);
		
		System.out.println("------------------------");
		System.out.println("Averaged Weight Vector");
		System.out.println("------------------------");

		double[] averagedUnitWeightVector = VectorUtils.averageVector(vectors);
		for( int i = 0; i < averagedUnitWeightVector.length; i++ ) {
			System.out.println("\\( w_{" + (i) + "} \\rightarrow " + averagedUnitWeightVector[i] + " \\) \\\\");
		}
		
		double threshold = averagedUnitWeightVector[0];
		double[] weight = new double[averagedUnitWeightVector.length - 1];
		for( int i = 1; i < averagedUnitWeightVector.length; i++ ) {
			weight[i - 1] = averagedUnitWeightVector[i];
		}
		
		WidrowHoffLearner learner = new WidrowHoffLearner(dataSet1, weight, threshold);
		System.out.println("------------------------");
		System.out.println("Data set 1 test");
		System.out.println("------------------------");
		learner.testWeightVector(dataSet1);

	}
}

package hw2.widrowhoff;

import java.util.ArrayList;
import java.util.List;

public class MainP4e {
	public static void main(String[] args) {
		final String dataSet1Csv = "DataSet1.csv";
		
		DataSet dataSet1 = new DataSet(dataSet1Csv);
		double[] dataSet2Threshold = new double[] {
			513.0
		};
		double[] dataSet2Weights = new double[] {
			-1719.0,
			2216.0,
			1357.0,
			1506.0,
			435.0,
			1152.0,
			-300.0,
			-1928.0,
			1462.0,
			991.9000000000113,
			-775.0,
			6726.0,
			11721.0
		};
		double[] dataSet4Weights = new double[] {
			-39.0,
			3052.0,
			3274.0,
			533.0,
			550.0,
			2354.0,
			-3552.0,
			-2025.0,
			2148.0,
			2022.2999999999358,
			-1500.0,
			6491.0,
			14862.0
		};
		double[] dataSet4Threshold = new double[] {
			413.0
		};
		double[] dataSet5Weights = new double[] {
			177.0,
			2699.0,
			819.0,
			631.0,
			497.0,
			1189.0,
			-952.0,
			-1948.0,
			2047.0,
			1521.3999999999726,
			-1380.0,
			5972.0,
			10336.0
		};
		double[] dataSet5Threshold = new double[] {
			272.0
		};
		double[] dataSet6Weights = new double[] {
			-1853.0,
			3172.0,
			3650.0,
			914.0,
			588.0,
			753.0,
			-3254.0,
			-1900.0,
			1060.0,
			1868.8000000000209,
			-580.0,
			8319.0,
			17636.0
		};
		double[] dataSet6Threshold = new double[] {
			 858.0
		};
		
		double[] dataSet2UnitVector = VectorUtils.unitVector(VectorUtils.concat(dataSet2Threshold, dataSet2Weights));
		double[] dataSet4UnitVector = VectorUtils.unitVector(VectorUtils.concat(dataSet4Threshold, dataSet4Weights));
		double[] dataSet5UnitVector = VectorUtils.unitVector(VectorUtils.concat(dataSet5Threshold, dataSet5Weights));
		double[] dataSet6UnitVector = VectorUtils.unitVector(VectorUtils.concat(dataSet6Threshold, dataSet6Weights));
		
		List<double[]> vectors = new ArrayList<double[]>();
		vectors.add(dataSet2UnitVector);
		vectors.add(dataSet4UnitVector);
		vectors.add(dataSet5UnitVector);
		vectors.add(dataSet6UnitVector);
		
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
		learner.testWeightVector(dataSet1);
	}

}

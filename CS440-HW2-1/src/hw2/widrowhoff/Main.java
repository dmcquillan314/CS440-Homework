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
		
		WidrowHoffLearner learner = new WidrowHoffLearner( dataSet1, 0.0 );
		learner.trainWeightVector();
		
		for( int i = 0; i < learner.getWeights().length; i++ ) {
			System.out.println("\\( w_{" + (i+1) + "} \\rightarrow " + learner.getWeights()[i] + " \\)");
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
		
		final List<Pair<String,Double>> coefficients = CorrelationCoefficient.calculate(dataSet3, classifications);
		System.out.println("------------------------");
		System.out.println("Correlation Coefficients");
		System.out.println("------------------------");
		String maxProp = "";
		Double maxCorrelation = Double.MIN_VALUE;
		System.out.println("\\( \\begin{array}{l|l}");
		System.out.println("\\bf{Label} & \\bf{Correlation Coefficient} \\\\");
		System.out.println("\\hline");
		for(final Pair<String, Double> coefficient : coefficients ) {
			System.out.println("\\text{ " + coefficient.getLeft().trim().replaceAll("[^A-Za-z0-9]", " ") + " } & " + coefficient.getRight() + " \\\\");
			if( coefficient.getRight().compareTo(maxCorrelation) > 0 ) {
				maxProp = coefficient.getLeft();
				maxCorrelation = coefficient.getRight();
			}
		}
		System.out.println("\\end{array} \\)");
		System.out.println();

		
		System.out.println("------------------------");
		System.out.println("Maximum Correlation Prop");
		System.out.println("------------------------");
		System.out.println("\\bf{ Property With Maximum Correlation} \\\\ ");
		System.out.println("Label: " + maxProp.replaceAll("[^A-Za-z0-9]", " ") + " \\\\");
		System.out.println("Correlation Coefficient: \\(" + maxCorrelation + "\\)");
	}

}

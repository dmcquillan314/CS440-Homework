package hw2.widrowhoff;

import java.util.ArrayList;
import java.util.List;

public class WidrowHoffLearner {

	private double[] weights;
	private double[] homogeneousWeights;
	private double[][] x;
	private double alpha = 1;
	private double threshold;
	private double[] labels;
	private double margin;
	
	public WidrowHoffLearner( final DataSet dataSet, final double[] weights, final double threshold ) {
		this.x = dataSet.exData;
		this.labels = dataSet.exLabels;
		this.threshold = threshold;
	}
	
	public WidrowHoffLearner( final DataSet dataSet, final double threshold ) {
		this.x = dataSet.exData;
		this.labels = dataSet.exLabels;
		final int inputLength = x[0].length; // get input vector length less the value of the class
		double[] weightsVector = new double[inputLength];
		for( int i = 0; i < inputLength; i++ ) {
			weightsVector[i] = 0;
		}
		this.weights = weightsVector;
		
		homogeneousWeights = new double[ weights.length + 1];
		homogeneousWeights[0] = threshold;
		for(int k = 1; k < weights.length; k++ ) {
			homogeneousWeights[k] = weights[k - 1];
		}
		
		this.threshold = threshold;
	}
	
	public void trainWeightVector() {
		int iterations = 0;
		while( true ) {
			int errors = 0;
			for( int i = 0; i < x.length; i++ ) {
				double[] heterogeneousInputs = x[i];
				double[] inputs = new double[heterogeneousInputs.length + 1];
				inputs[0] = -1;
				for(int j = 1; j < inputs.length; j++) {
					inputs[j] = heterogeneousInputs[j-1];
				}
				
				double err = labels[i] - percepW( inputs ); // use last value in array as label
				
				// 0 - correct
				// -1 - false positive
				// 1 - false negative
				if( err != 0.0 ) {
					errors++;
//					double loss = -1.0 * err * VectorUtils.dotProduct(inputs, weights);
					
					final double[] scaledInputs = VectorUtils.scale(inputs, alpha * err );
					homogeneousWeights = VectorUtils.add(scaledInputs, homogeneousWeights);
				}
			}
			
			if(errors == 0) {
				break;
			}
			
			iterations++;
		}
		
		calculateMargin();
		
		for( int i = 1; i < homogeneousWeights.length; i++ ) {
			weights[i-1] = homogeneousWeights[i];
		}
		
		System.out.println("The training took " + iterations + " epochs.");
		System.out.println("Threshhold: " + homogeneousWeights[0]);
		System.out.println("\\( \\gamma \\rightarrow " + margin + " \\)");
	}
	
	public void testWeightVector(DataSet dataSet) {
		int falseNegatives = 0;
		int falsePositives = 0;
		int truePositives = 0;
		int trueNegatives = 0;
		double totalLoss = 0.0;
		for( int i = 0; i < dataSet.exData.length; i++ ) {
			double[] heterogeneousInputs = dataSet.exData[i];
			double[] inputs = new double[heterogeneousInputs.length + 1];
			inputs[0] = -1;
			for(int j = 1; j < inputs.length; j++) {
				inputs[j] = heterogeneousInputs[j-1];
			}
			
			final double percepResult = percepW( inputs );
			final double err = dataSet.exLabels[i] - percepResult; // use last value in array as label
			
			// 0 - correct
			// -1 - false positive
			// 1 - false negative
			if( err > 0.0 ) {
				falseNegatives++;
				System.out.println("False Negative Found: ");
				System.out.println("Index: " + i);
				System.out.println("Inputs: " + vectorToString(inputs));
				totalLoss += -1.0 * err * VectorUtils.dotProduct(inputs, homogeneousWeights);
			} else if ( err < 0.0 ) {
				falsePositives++;
				System.out.println("False Positive Found: ");
				System.out.println("Index: " + i);
				System.out.println("Inputs: " + vectorToString(inputs));
				totalLoss += -1.0 * err * VectorUtils.dotProduct(inputs, homogeneousWeights);
			} else {
				if( percepResult == 1 ) {
					truePositives++;
				} else {
					trueNegatives++;
				}
			}
		}
		
		System.out.println("True Positives: " + truePositives );
		System.out.println("True Negatives: " + trueNegatives );
		System.out.println("False positives: " + falsePositives);
		System.out.println("False negatives: " + falseNegatives);
		System.out.println("Total loss: " + totalLoss);
		System.out.println("Total items: " + dataSet.exData.length );
	}
	
	private String vectorToString( final double[] x ) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("(");
		for( int i = 1; i < x.length; i++ ) {
			stringBuilder.append(x[i]);
			if( i != x.length - 1) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
	
	public List<Double> applyWeightVector(DataSet dataSet) {
		List<Double> classifications = new ArrayList<Double>();
		for( int i = 0; i < dataSet.exData.length; i++ ) {
			double[] heterogeneousInputs = dataSet.exData[i];
			double[] inputs = new double[heterogeneousInputs.length + 1];
			inputs[0] = -1;
			for(int j = 1; j < inputs.length; j++) {
				inputs[j] = heterogeneousInputs[j-1];
			}
			double classification = percepW(inputs);
			classifications.add(classification);
		}
		return classifications;
	}
	
	private void calculateMargin() {
		margin = Double.MAX_VALUE;
		final double[] unitWeightVector = VectorUtils.unitVector(homogeneousWeights);
		
		for( int i = 0; i < x.length; i++ ) {
			double[] inputs = x[i];
			
			double dotProduct = Math.abs( VectorUtils.dotProduct(unitWeightVector, inputs) );
			double norm = VectorUtils.norm(inputs);
			
			double marginPart = dotProduct / norm;
			
			margin = Math.min(margin, marginPart);
		}
	}
	
	private double percepW( final double[] curInputVector ) {
		double sum = 0.0;
		
		sum += VectorUtils.dotProduct( curInputVector, homogeneousWeights );
		
		if( sum > 0 ) return 1;
		else if ( sum < 0 ) return 0;
		else return 0;
	}

	public double[] getWeights() {
		return weights;
	}
	public double[][] getX() {
		return x;
	}

	public double getAlpha() {
		return alpha;
	}

	public double getThreshold() {
		return threshold;
	}

	public double[] getLabels() {
		return labels;
	}

	public double getMargin() {
		return margin;
	}

}

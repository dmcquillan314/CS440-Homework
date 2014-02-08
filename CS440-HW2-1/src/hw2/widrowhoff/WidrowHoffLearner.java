package hw2.widrowhoff;

import java.util.ArrayList;
import java.util.List;

public class WidrowHoffLearner {

	private double[] weights;
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
		this.threshold = threshold;
	}
	
	public void trainWeightVector() {
		int iterations = 0;
		while( true ) {
			int errors = 0;
			for( int i = 0; i < x.length; i++ ) {
				final double[] inputs = x[i];
				
				double err = labels[i] - percepW( inputs ); // use last value in array as label
				
				// 0 - correct
				// -1 - false positive
				// 1 - false negative
				if( err != 0.0 ) {
					errors++;
//					double loss = -1.0 * err * VectorUtils.dotProduct(inputs, weights);
					
					final double[] scaledInputs = VectorUtils.scale(inputs, alpha * err );
					weights = VectorUtils.add(scaledInputs, weights);
				}
			}
			
			if(errors == 0) {
				break;
			}
			
			iterations++;
		}
		
		calculateMargin();
		
		System.out.println("The training took " + iterations + " epochs.");
		System.out.println("Threshhold: " + threshold);
		System.out.println("Margin: " + margin );
	}
	
	public void testWeightVector(DataSet dataSet) {
		int falseNegatives = 0;
		int falsePositives = 0;
		int truePositives = 0;
		int trueNegatives = 0;
		double totalLoss = 0.0;
		for( int i = 0; i < dataSet.exData.length; i++ ) {
			final double[] inputs = dataSet.exData[i];
			
			final double percepResult = percepW( inputs );
			final double err = dataSet.exLabels[i] - percepResult; // use last value in array as label
			
			// 0 - correct
			// -1 - false positive
			// 1 - false negative
			if( err > 0.0 ) {
				falseNegatives++;
				totalLoss += -1.0 * err * VectorUtils.dotProduct(inputs, weights);
			} else if ( err < 0.0 ) {
				falsePositives++;
				totalLoss += -1.0 * err * VectorUtils.dotProduct(inputs, weights);
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
	
	public List<Double> applyWeightVector(DataSet dataSet) {
		List<Double> classifications = new ArrayList<Double>();
		for( int i = 0; i < dataSet.exData.length; i++ ) {
			final double[] inputs = dataSet.exData[i];
			double classification = percepW(inputs);
			classifications.add(classification);
		}
		return classifications;
	}
	
	private void calculateMargin() {
		margin = Double.MAX_VALUE;
		final double[] unitWeightVector = VectorUtils.unitVector(weights);
		
		for( int i = 0; i < x.length; i++ ) {
			double[] inputs = x[i];
			
			double dotProduct = Math.abs( VectorUtils.dotProduct(unitWeightVector, inputs) );
			double norm = VectorUtils.norm(inputs);
			
			double marginPart = dotProduct / norm;
			
			margin = Math.min(margin, marginPart);
		}
	}
	
	private double percepW( final double[] curInputVector ) {
		double sum = threshold * -1;
		
		sum += VectorUtils.dotProduct( curInputVector, weights);
		
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

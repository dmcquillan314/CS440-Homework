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
		
		this.weights = weights;
		this.threshold = threshold;
		
		homogeneousWeights = VectorUtils.concat(new double[] { threshold }, weights );
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
		
		homogeneousWeights = VectorUtils.concat(new double[] { threshold }, weights );
		
		this.threshold = threshold;
	}
	
	public void trainWeightVector() {
		int iterations = 0;
		int errors;
		do {
			errors = 0;
			for( int i = 0; i < x.length; i++ ) {
				double[] heterogeneousInputs = VectorUtils.concat(new double[] { -1.0 }, x[i]);
				
				double err = labels[i] - percepW( heterogeneousInputs ); // use last value in array as label
				
				// 0 - correct
				// -1 - false positive
				// 1 - false negative
				if( err != 0.0 ) {
					errors++;
//					double loss = -1.0 * err * VectorUtils.dotProduct(inputs, weights);
					
					final double[] scaledInputs = VectorUtils.scale(heterogeneousInputs, alpha * err );
					homogeneousWeights = VectorUtils.add(scaledInputs, homogeneousWeights);
				}
			}
			
			iterations++;
			
		} while( errors != 0);
		
		calculateMargin();
		threshold = homogeneousWeights[0];
		for( int i = 1; i < homogeneousWeights.length; i++ ) {
			weights[i-1] = homogeneousWeights[i];
		}
		
		System.out.println("\\textnormal{The training took } \\(" + iterations + "\\) \\textnormal{ epochs. }");
		System.out.println("\\textnormal{Threshhold:} \\( " + homogeneousWeights[0] + " \\)");
		System.out.println("\\( \\gamma \\rightarrow " + margin + " \\)");
	}
	
	public void testWeightVector(DataSet dataSet) {
		int falseNegatives = 0;
		int falsePositives = 0;
		int truePositives = 0;
		int trueNegatives = 0;
		double totalLoss = 0.0;
		for( int i = 0; i < dataSet.exData.length; i++ ) {
			double[] heterogeneousInputs = VectorUtils.concat(new double[] { -1.0 }, dataSet.exData[i]);
			
			final double percepResult = percepW( heterogeneousInputs );
			final double err = dataSet.exLabels[i] - percepResult; // use last value in array as label
			
			// 0 - correct
			// -1 - false positive
			// 1 - false negative
			if( err > 0.0 ) {
				falseNegatives++;
				System.out.println("False Negative Found: ");
				System.out.println("\\textnormal{Index: } \\( " + i + " \\) \\\\");
				System.out.println("\\textnormal{Inputs: } \\( " + VectorUtils.vectorToString(heterogeneousInputs, 1) + " \\) \\\\" );
				totalLoss += Math.abs(-1.0 * err * VectorUtils.dotProduct(heterogeneousInputs, homogeneousWeights ) / VectorUtils.norm(homogeneousWeights));
			} else if ( err < 0.0 ) {
				falsePositives++;
				System.out.println("False Positive Found: ");
				System.out.println("\\textnormal{Index: } \\( " + i + " \\) \\\\");
				System.out.println("\\textnormal{Inputs: } \\( " + VectorUtils.vectorToString(heterogeneousInputs, 1) + " \\) \\\\" );
				totalLoss += Math.abs(-1.0 * err * VectorUtils.dotProduct(heterogeneousInputs, homogeneousWeights ) / VectorUtils.norm(homogeneousWeights));
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
			double[] heterogeneousInputs = VectorUtils.concat(new double[] { -1.0 }, dataSet.exData[i]);
			double classification = percepW(heterogeneousInputs);
			classifications.add(classification);
		}
		return classifications;
	}
	
	private void calculateMargin() {
		margin = Double.MAX_VALUE;
		final double[] unitWeightVector = VectorUtils.unitVector(homogeneousWeights);
		
		for( int i = 0; i < x.length; i++ ) {
			double[] heterogeneousInputs = VectorUtils.concat(new double[] { -1.0 }, x[i]);
			
			double dotProduct = Math.abs( VectorUtils.dotProduct(homogeneousWeights, heterogeneousInputs) );
			double norm = VectorUtils.norm(homogeneousWeights);
			
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

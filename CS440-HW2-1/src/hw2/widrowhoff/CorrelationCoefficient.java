package hw2.widrowhoff;

import java.util.ArrayList;
import java.util.List;

public class CorrelationCoefficient {

	public static List<Pair<String,Double>> calculate(final DataSet dataSet, final List<Double> classifications ) {
		final String[] labels = dataSet.fields;
		List<Pair<String,Double>> coefficients = new ArrayList<Pair<String,Double>>();
		
		final List<List<Double>> transformedArray = transform(dataSet.exData);
		final double classificationMean = mean(classifications);
		
		int i = 0;
		for( final List<Double> array : transformedArray ) {
			final double mean = mean(array);
			final String label = labels[i];
			
			double numeratorSum = 0.0;
			double xDenominatorSum = 0.0;
			double yDenominatorSum = 0.0;
			
			int j = 0;
			for( final Double x : array ) {
				final double xDiff = x - mean;
				final double yDiff = classifications.get(j) - classificationMean;
				
				numeratorSum += xDiff * yDiff;
				xDenominatorSum += xDiff * xDiff;
				yDenominatorSum += yDiff * yDiff;
				
				j++;
			}
			
			final Double correlationCoefficient = numeratorSum / Math.sqrt(xDenominatorSum * yDenominatorSum);
			final Pair<String,Double> coefficientWithLabel = new Pair<String,Double>(label, correlationCoefficient);
			coefficients.add(coefficientWithLabel);
			i++;
		}
		
		return coefficients;
	}
	
	private static List< List<Double> > transform(double[][] data ) {
		List<List<Double>> transformed = new ArrayList<List<Double>>();
		
		for( int i = 0; i < data[0].length; i++ ) {
			List<Double> column = new ArrayList<Double>();
			for( int j = 0; j < data.length; j++ ) {
				column.add(data[j][i]);
			}
			transformed.add(column);
		}
		
		return transformed;
	}
	
	@SuppressWarnings("unused")
	private static double mean(final double[] array) {
		double sum = 0.0;
		for( int i = 0; i < array.length; i++ ) {
			sum+= array[i];
		}
		return sum / (double) array.length;
	}
	
	private static double mean(final List<Double> array) {
		double sum = 0.0;
		for( int i = 0; i < array.size(); i++ ) {
			sum+= array.get(i);
		}
		return sum / (double) array.size();
	}
}

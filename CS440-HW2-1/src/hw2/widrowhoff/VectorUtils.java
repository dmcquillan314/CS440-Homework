package hw2.widrowhoff;

import java.util.ArrayList;
import java.util.List;

public class VectorUtils {
	
	public static double dotProduct( final double[] x, final double[] y ) {
		double sum = 0;
		for( int i = 0; i < x.length && i < y.length; i++ ) {
			sum += x[i] * y[i];
		}
		
		return sum;
	}
	
	public static double[] scale( final double[] x, final double scalar ) {
		double[] xCopy = x.clone();
		for( int i = 0; i < x.length; i++ ) {
			xCopy[i] *= scalar;
		}
		return xCopy;
	}
	
	public static double[] add( double[] x, final double[] y ) {
		double[] xCopy = x.clone();
		double[] yCopy = y.clone();
		for( int i = 0; i < xCopy.length && i < yCopy.length; i++ ) {
			xCopy[i] += yCopy[i];
		}
		
		return xCopy;
	}
	
	public static List<Pair<Double, Double> > zip( double[] x, double[] y ) {
		List<Pair<Double, Double> > zippedVector = new ArrayList<Pair<Double, Double>>();
		for( int i = 0; i < x.length && i < y.length; i++ ) {
			zippedVector.add( new Pair<Double,Double>(x[i], y[i]) );
		}
		return zippedVector;
	}
	
	public static double[] concat( double[] left, double[] right) {
		if( left == null || right == null ) {
			throw new IllegalArgumentException("The left or right arguments must not be null");
		}
		
		final int totalLength = left.length + right.length;
		double[] concatted = new double[totalLength];
		for(int i = 0; i < left.length; i++ ) {
			concatted[i] = left[i];
		}
		for(int i = 0; i < right.length; i++ ) {
			concatted[i + left.length] = right[i];
		}
		return concatted;
	}
	
	public static double[] unitVector( double[] x ) {
		double[] copy = x.clone();
		double norm = norm(copy);
		return scale(x, 1.0 / norm );
	}
	
	public static double norm( double[] x ) {
		double sum = 0;
		for( int i = 0; i < x.length; i++) {
			sum += Math.pow(x[i], 2.0);
		}
		return Math.sqrt(sum);
	}
}

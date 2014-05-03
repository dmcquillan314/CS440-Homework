package com.hw6.document.classification.helpers;

import java.util.Collections;
import java.util.List;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Stopwords;

import com.hw6.documentFrequency.InverseDocumentFrequencyStatistics;
import com.hw6.util.Pair;

public class StopWordsHelper {

	public static Stopwords findStopWords( final Instances instances, final InverseDocumentFrequencyStatistics statistics, final Double threshold, final Integer maxStopWords ) {
		
		if( threshold == null) {
			throw new IllegalArgumentException("Threshold must not be null");
		}
		
		Stopwords stopwords = new Stopwords();
		Integer numStopwords = 0;
		
		List<Pair<Integer,Double>> sortedDocumentFrequencies = statistics.getSortedInverseDocumentFrequencies();
		Collections.reverse(sortedDocumentFrequencies);
		
		for( Pair<Integer,Double> stat : sortedDocumentFrequencies ) {
			
			Attribute attribute = instances.attribute( stat.getLeft() );
			
			if( (stat.getRight().compareTo(threshold) < 0 || threshold == 0.0) && numStopwords <= maxStopWords ) {
				numStopwords++;
				stopwords.add( attribute.name().substring(1) );
			}
		}
		
		return stopwords;

	}
	
}

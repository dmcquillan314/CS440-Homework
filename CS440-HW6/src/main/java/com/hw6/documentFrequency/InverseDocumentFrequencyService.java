package com.hw6.documentFrequency;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.hw6.documentFrequency.comparator.DocumentFrequencyComparator;
import com.hw6.util.Pair;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class InverseDocumentFrequencyService {

	private Instances instances;
	private Map<Integer,Double> termCounts = new TreeMap<Integer,Double>();
	private Integer numberOfDocuments = 0;
	private Double p = 0.0;
	private InverseDocumentFrequencyStatistics statistics = new InverseDocumentFrequencyStatistics();
	
	public InverseDocumentFrequencyService(final Instances instances, final Double p) {
		this.instances = instances;
		this.p = p;
		
		calculateDocumentFrequencies();
	}
	
	public InverseDocumentFrequencyService(final Instances instances) {
		this.instances = instances;
		
		calculateDocumentFrequencies();
	}
	
	private void populateTermCounts() {

		Iterator<Instance> instanceIterator = instances.listIterator();

		while( instanceIterator.hasNext() ) {
			Instance instance = instanceIterator.next();
			
			numberOfDocuments++;
			
			@SuppressWarnings("unchecked")
			Enumeration<Attribute> enumeration = instance.enumerateAttributes();
			
			while( enumeration.hasMoreElements() ) {
				
				Attribute attribute = enumeration.nextElement();
				
				Double termCount = instance.value(attribute);
				
				if( termCount > 0.0 ) {
					Integer attributeIndex = attribute.index();
					
					if( termCounts.containsKey(attributeIndex) == false ) {
						termCounts.put(attributeIndex, 0.0);
					}
					
					termCounts.put(attributeIndex, termCounts.get(attributeIndex) + 1.0 );
				}
			}
		}
	}
	
	private void calculateDocumentFrequencies() {
		populateTermCounts();
		
		for( Integer attributeIndex : termCounts.keySet() ) {
			Double termCount = termCounts.get(attributeIndex);
			
			Double inverseDocumentFrequency = Math.log(numberOfDocuments / ( termCount + p ) );
			Double currentDocumentFrequency = getStatistics().getInverseDocumentFrequencies().containsKey(attributeIndex) 
													? getStatistics().getInverseDocumentFrequencies().get(attributeIndex) 
													: 0.0;
			
			getStatistics().getInverseDocumentFrequencies().put(attributeIndex, Math.max(inverseDocumentFrequency, currentDocumentFrequency));
		}
		
		for( Integer attributeIndex : getStatistics().getInverseDocumentFrequencies().keySet() ) {
			Double inverseDocumentFrequency = getStatistics().getInverseDocumentFrequencies().get(attributeIndex);
			
			getStatistics().getSortedInverseDocumentFrequencies().add(new Pair<Integer,Double>(attributeIndex,inverseDocumentFrequency));
		}
		
		Collections.sort(getStatistics().getSortedInverseDocumentFrequencies(), new DocumentFrequencyComparator());
	}
	
	public InverseDocumentFrequencyStatistics getStatistics() {
		return statistics;
	}
}

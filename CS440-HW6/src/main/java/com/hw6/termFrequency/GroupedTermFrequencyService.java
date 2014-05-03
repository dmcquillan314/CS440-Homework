package com.hw6.termFrequency;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hw6.termFrequency.comparator.TermFrequencyComparator;
import com.hw6.util.Pair;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class GroupedTermFrequencyService {
	
	private Instances instances;
	private Map<Integer,Double> documentFrequencies = null;
	private Map<Double,TermFrequencyStatistics> groupedTermFrequencyStatistics = new HashMap<Double,TermFrequencyStatistics>();
	
	public GroupedTermFrequencyService( final Instances instances ) {
		this(instances, null);
	}
	
	public GroupedTermFrequencyService( final Instances instances, final Map<Integer, Double> documentFrequencies ) {
		this.instances = instances;
		this.documentFrequencies = documentFrequencies;
		
		calculateTermFrequencies();
	}
	
	private Double getMaxTermCount( Instance instance ) {
		@SuppressWarnings("unchecked")
		Enumeration<Attribute> enumeration = instance.enumerateAttributes();
		
		Double maxTermCount = 0.0;
		
		while( enumeration.hasMoreElements() ) {
			final Attribute attribute = enumeration.nextElement();
			
			final Double value = instance.value(attribute);
			if( value > 0.0 ) {
				maxTermCount = Math.max(maxTermCount, value);
			}
		}
		
		return maxTermCount;
	}
	
	private Double getTotalTermCount( Instance instance ) {
		@SuppressWarnings("unchecked")
		Enumeration<Attribute> enumeration = instance.enumerateAttributes();
		
		Double totalTermCount = 0.0;
		
		while( enumeration.hasMoreElements() ) {
			final Attribute attribute = enumeration.nextElement();
			
			double value = instance.value(attribute);
			if( value > 0.0 ) {
				totalTermCount += value;
			}
		}
		
		return totalTermCount;
	}
	
	private void calculateIDFTermFrequency(TermFrequencyStatistics statistics, Attribute attribute, Double termFrequency) {
		if( documentFrequencies != null ) {
			Double idfTermFrequency = termFrequency * documentFrequencies.get(attribute.index()); 
			
			if( statistics.getCalculatedIDFTermFrequencies().containsKey(attribute.index()) == false ) {
				statistics.getCalculatedIDFTermFrequencies().put(attribute.index(), 0.0);
			}
			
			Double currentMaxIDFTermFrequency = statistics.getCalculatedIDFTermFrequencies().get(attribute.index());
			statistics.getCalculatedIDFTermFrequencies().put(attribute.index(), Math.max(idfTermFrequency, currentMaxIDFTermFrequency));
		}
	}
	
	private void calculateTermFrequencies() {
		
		Iterator<Instance> instanceIterator = instances.listIterator();
		
		while( instanceIterator.hasNext() ) {
			Instance instance = instanceIterator.next();
			
			@SuppressWarnings("unchecked")
			Enumeration<Attribute> enumeration = instance.enumerateAttributes();
			
			Double maxTermCount = getMaxTermCount( instance );
			Double totalTermCount = getTotalTermCount( instance );
			Double groupId = instance.value(instance.classAttribute());
			
			if( getGroupedTermFrequencyStatistics().containsKey(groupId) == false ) {
				getGroupedTermFrequencyStatistics().put(groupId, new TermFrequencyStatistics() );
			}
			
			TermFrequencyStatistics statistics = getGroupedTermFrequencyStatistics().get(groupId);
			
			while( enumeration.hasMoreElements() ) {
				final Attribute attribute = enumeration.nextElement();
				
				Double termCount = instance.value(attribute);
				if( termCount > 0.0 ) {
					Double termFrequency = termCount / totalTermCount;
					Double normalizedTermFrequency = termCount / maxTermCount;
					
					calculateIDFTermFrequency(statistics, attribute, termFrequency);
					
					if( statistics.getCalculatedTermFrequencies().containsKey(attribute.index()) == false ) {
						statistics.getCalculatedTermFrequencies().put(attribute.index(), 0.0);
					}
					if( statistics.getNormalizedCalculatedTermFrequencies().containsKey(attribute.index()) == false ) {
						statistics.getNormalizedCalculatedTermFrequencies().put(attribute.index(), 0.0);
					}
					
					Double currentMaxTermFrequency = statistics.getCalculatedTermFrequencies().get(attribute.index());
					Double currentNormalizedMaxTermFrequency = statistics.getNormalizedCalculatedTermFrequencies().get(attribute.index());
				
					statistics.getCalculatedTermFrequencies().put(attribute.index(), Math.max(termFrequency, currentMaxTermFrequency));
					statistics.getNormalizedCalculatedTermFrequencies().put(attribute.index(), Math.max(normalizedTermFrequency, currentNormalizedMaxTermFrequency));
				}
			}
		}
		
		for( Double groupId : getGroupedTermFrequencyStatistics().keySet() ) {
			
			TermFrequencyStatistics statistics = getGroupedTermFrequencyStatistics().get(groupId);
			for( Integer attributeIndex : statistics.getCalculatedTermFrequencies().keySet() ) {
				Double termFrequency = statistics.getCalculatedTermFrequencies().get(attributeIndex);
				Double normalizedTermFrequency = statistics.getNormalizedCalculatedTermFrequencies().get(attributeIndex);
				Double idfTermFrequency = statistics.getCalculatedIDFTermFrequencies().get(attributeIndex);
				
				statistics.getSortedCalculatedTermFrequencies().add(new Pair<Integer,Double>(attributeIndex, termFrequency));
				statistics.getSortedNormalizedCalculatedTermFrequencies().add(new Pair<Integer,Double>(attributeIndex, normalizedTermFrequency));
				statistics.getSortedCalculatedIDFTermFrequencies().add(new Pair<Integer,Double>(attributeIndex, idfTermFrequency));
			}
			
			Collections.sort(statistics.getSortedCalculatedTermFrequencies(), new TermFrequencyComparator());
			Collections.sort(statistics.getSortedNormalizedCalculatedTermFrequencies(), new TermFrequencyComparator());
			Collections.sort(statistics.getSortedCalculatedIDFTermFrequencies(), new TermFrequencyComparator());
			
			getGroupedTermFrequencyStatistics().put(groupId, statistics);
		}
	}

	public Instances getInstances() {
		return instances;
	}

	public Map<Double,TermFrequencyStatistics> getGroupedTermFrequencyStatistics() {
		return groupedTermFrequencyStatistics;
	}
	
	public Map<Integer,Double> getDocumentFrequencies() {
		return documentFrequencies;
	}
	
}

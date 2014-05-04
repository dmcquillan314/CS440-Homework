package com.hw6.document.classification.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.EnglishStemmer;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Stopwords;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class InstancesFilterHelper {
	
	private static Instances filterInstancesByAttributeList( Instances instances, List<Attribute> attributesToBeRemoved ) throws Exception {
		Remove remove = new Remove();

		int numAttributes = attributesToBeRemoved.size();
		int[] indicesArray = new int[numAttributes];
		
		for( int i = 0; i < numAttributes; i++ ) {
			final Attribute attribute = attributesToBeRemoved.get(i);
			indicesArray[i] = attribute.index();
		}
		
		remove.setAttributeIndicesArray(indicesArray);
		remove.setInputFormat(instances);
		
		instances = Filter.useFilter(instances, remove);
		instances.setClassIndex( instances.numAttributes() - 1);

		
		return instances;
	}
	
	public static Instances filterInstancesByStopwords( Instances instances, Stopwords stopwords ) throws Exception {
		
		System.out.println("Attributes before stop word filter: " + instances.numAttributes() );
		
		List<Attribute> attributesToBeRemoved = new ArrayList<Attribute>();
		
		for( int i = 0; i < instances.numAttributes(); i++ ) {
			Attribute attribute = instances.attribute(i);
			final String name = attribute.name().substring(1);
			
			if( Stopwords.isStopword(name) ) {
				attributesToBeRemoved.add(attribute);
			}
		}
		
		instances = filterInstancesByAttributeList( instances, attributesToBeRemoved );
		
		System.out.println("Attributes after stop word filter: " + instances.numAttributes() );
		
		return instances;

	}
	
	public static Instances filterInstancesWithStemmer( Instances instances ) throws Exception {
		System.out.println("Attributes before stemmer filter: " + instances.numAttributes() );
		
		List<Attribute> attributesToBeRemoved = new ArrayList<Attribute>();

		for( int i = 0; i < instances.numAttributes(); i++ ) {
			final Attribute attribute = instances.attribute(i);
			final String name = attribute.name().substring(1).toLowerCase();
			
			SnowballStemmer stemmer = new EnglishStemmer();

			stemmer.setCurrent(name);
			stemmer.stem();
			
			final String postStem = stemmer.getCurrent();
			
			if( name.equals( postStem ) == false ) {
				Attribute postAttribute = instances.attribute("T" + postStem);
				
				if( postAttribute == null ) {
					try {
						instances.renameAttribute(attribute.index(), postStem);
						updateValues(instances, attribute, postAttribute);
					} catch( Exception e ) {
						// Do nothing
					}
				} else {
					attributesToBeRemoved.add(attribute);
					updateValues(instances, attribute, postAttribute);
				}
			}
		}
		
		instances = filterInstancesByAttributeList( instances, attributesToBeRemoved );
		
		System.out.println("Attributes after stemmer filter: " + instances.numAttributes() );
		
		return instances;
	}

	private static void updateValues(Instances instances, Attribute fromAttribute, Attribute toAttribute) {
		
		Iterator<Instance> instanceIterator = instances.listIterator();

		while( instanceIterator.hasNext() ) {
			Instance instance = instanceIterator.next();
			
			final Double fromValue = instance.value(fromAttribute);
			final Double toValue = instance.value(toAttribute);
			
			final Double resultValue = fromValue + toValue;
			instance.setValue(toAttribute, resultValue);
		}
	}

}

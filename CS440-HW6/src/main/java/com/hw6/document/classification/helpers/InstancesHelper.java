package com.hw6.document.classification.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Stopwords;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class InstancesHelper {

	public static Instances createInstancesWithFile( final File file ) throws IOException {
		
		BufferedReader bufferedReader = new BufferedReader( new FileReader(file) );
		
		Instances instances = new Instances(bufferedReader);
		
		instances.setClassIndex( instances.numAttributes() - 1 );
		
		bufferedReader.close();
		
		return instances;
	}
	
	public static Instances filterInstancesByStopwords( Instances instances, Stopwords stopwords ) throws Exception {
		
		System.out.println("Attributes before stop word filter: " + instances.numAttributes() );
		
		Remove remove = new Remove();
				
		List<Attribute> attributesToBeRemoved = new ArrayList<Attribute>();
		
		for( int i = 0; i < instances.numAttributes(); i++ ) {
			Attribute attribute = instances.attribute(i);
			final String name = attribute.name().substring(1);
			
			if( Stopwords.isStopword(name) ) {
				attributesToBeRemoved.add(attribute);
			}
		}
		
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
		
		System.out.println("Attributes after stop word filter: " + instances.numAttributes() );
		
		return instances;

	}
	
}

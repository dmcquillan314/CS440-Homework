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
	
}

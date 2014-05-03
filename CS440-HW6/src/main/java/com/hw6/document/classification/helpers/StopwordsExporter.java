package com.hw6.document.classification.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import weka.core.Stopwords;

public class StopwordsExporter {

	public static void exportStopwords( final Stopwords stopwords, final File directory, final File file ) throws Exception {
		
		if( directory.exists() == false ) {
			directory.mkdir();
		}
		
		if( file.exists() ) {
			file.delete();
		}

		BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(file, true ) );
		stopwords.write(bufferedWriter);
		bufferedWriter.close();
		
	}
}

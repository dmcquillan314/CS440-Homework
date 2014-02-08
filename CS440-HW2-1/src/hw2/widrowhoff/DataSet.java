package hw2.widrowhoff;
// CSV file to arrays for CS440 / ECE448 MP HW2 on perceptrons

// Read in a comma separated value file for training or classifying examlpes from two classes
// First line is a list of feature names, the last one should be "class"
// second line is blank; then one line for each example in csv form

// the DataSet object provides the final variables below: feature values, example labels & feature names

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Usage:
 * <p>String csvFile = "DataSet1.csv";</p>
 * <p>DataSet ds = new DataSet(csvFile);</p>
 * <p>int[] keep = {0, 3, 12}</p>
 * <p>ds = new DataSet(csvFile, keep)</p>>
 */
public class DataSet {

  public final double[][] exData;	// feature values except for class: [num examples][num features]
  public final double[] exLabels;	// a class labels: [num examples] each is (0., 1., -1.)
  public final String[] fields;	// the feature names (including "class" if given in file)
  private String fileName;			// name of the CVS data set file
 
  private void disp10(String desc) {
	System.out.printf("%s: %s: %d examples\n  %d features", 
		desc, fileName, exData.length, exData[0].length);
	if (exLabels[0] > -1.) System.out.println(" and class");
	else System.out.println(":");
	for (String fld : fields) System.out.printf("  %s", fld);
	System.out.println("\nPrinting up to the first 10");
	for (int i = 0; i < exData.length && i < 10; i++) {
		for (double val : exData[i]) System.out.printf("%f,  ", val);
		System.out.println(exLabels[i]);
	}
  }
  
  public DataSet(String csvFileName) {	// overloadedd constructors
	  this(csvFileName, false, null);
  }
	  
  public DataSet(String csvFileName, boolean permuteP) {
	  this(csvFileName, permuteP, null);
  }
	  
  public DataSet(String csvFileName, int[] keepIdxs) {
	  this(csvFileName, false, keepIdxs);
  }
	  
  public DataSet(String csvFileName, boolean permuteP, int[] keepIdxs) {
	fileName = csvFileName;
	double[][] dats = null;			// for finals
	double[] labs = null;
//	String[] flds = null;
	String[] keepFlds = null;
	BufferedReader br = null;
	ArrayList<double[]> dataAL = new ArrayList<double[]>();
	int nFeats;
	try {
		br = new BufferedReader(new FileReader(csvFileName));
		String line = br.readLine();
		String[] flds = line.split(",");			// feature names
		br.readLine();
		while ((line = br.readLine()) != null) {
			String[] rec = line.split(",");		// comma separates fields of example
			double[] frec = new double[rec.length];
			for (int i = 0; i < rec.length; i++) 
				frec[i] = Double.parseDouble(rec[i]);
			dataAL.add(frec);			// grow the array list with a line of doubles
		}
		br.close();
		
		if (permuteP) Collections.shuffle(dataAL);	// Optional permutation of examples
		if (keepIdxs == null) nFeats = flds.length - 1;
		else nFeats = keepIdxs.length;
		
		double[][] dataSet = new double[dataAL.size()][flds.length];
		dataAL.toArray(dataSet);
		labs = new double[dataAL.size()];
		dats = new double[dataAL.size()][nFeats];
		keepFlds = new String[nFeats+1];
		
		for (int i = 0; i < dataSet.length; i++) {
			for (int j = 0; j < nFeats; j++) {		// add the (desired) feature values
				if (keepIdxs == null) dats[i][j] = dataSet[i][j];
				else dats[i][j] = dataSet[i][keepIdxs[j]];
			}
			labs[i] = dataSet[i][flds.length-1];	// add the class value
		}
		for (int j = 0; j < nFeats; j++) {		// add the (desired) feature names
			if (keepIdxs == null) keepFlds[j] = flds[j];
			else keepFlds[j] = flds[keepIdxs[j]];
		}
		keepFlds[nFeats] = flds[flds.length-1];
	}
	catch (IOException e) {
		e.printStackTrace();
	}
	finally {
		exData = dats;
		exLabels = labs;
		fields = keepFlds;
	}
} }
	

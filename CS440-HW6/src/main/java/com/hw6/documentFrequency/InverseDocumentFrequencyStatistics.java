package com.hw6.documentFrequency;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.hw6.util.Pair;

public class InverseDocumentFrequencyStatistics {

	private Map<Integer,Double> inverseDocumentFrequencies = new HashMap<Integer,Double>();
	
	private List<Pair<Integer,Double>> sortedInverseDocumentFrequencies = new ArrayList<Pair<Integer,Double>>();

	public Map<Integer, Double> getInverseDocumentFrequencies() {
		return inverseDocumentFrequencies;
	}

	public List<Pair<Integer,Double>> getSortedInverseDocumentFrequencies() {
		return sortedInverseDocumentFrequencies;
	}
	
}

package com.hw6.termFrequency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hw6.util.Pair;

public class TermFrequencyStatistics {
	
	private Map<Integer,Double> calculatedTermFrequencies = new HashMap<Integer,Double>();
	private Map<Integer,Double> normalizedCalculatedTermFrequencies = new HashMap<Integer,Double>();
	private Map<Integer,Double> calculatedIDFTermFrequencies = new HashMap<Integer,Double>();
	
	private List<Pair<Integer,Double>> sortedCalculatedTermFrequencies = new ArrayList<Pair<Integer,Double>>();
	private List<Pair<Integer,Double>> sortedNormalizedCalculatedTermFrequencies = new ArrayList<Pair<Integer,Double>>();
	private List<Pair<Integer,Double>> sortedCalculatedIDFTermFrequencies = new ArrayList<Pair<Integer,Double>>();
	
	public Map<Integer, Double> getCalculatedTermFrequencies() {
		return calculatedTermFrequencies;
	}
	public Map<Integer, Double> getNormalizedCalculatedTermFrequencies() {
		return normalizedCalculatedTermFrequencies;
	}
	public List<Pair<Integer, Double>> getSortedCalculatedTermFrequencies() {
		return sortedCalculatedTermFrequencies;
	}
	public List<Pair<Integer, Double>> getSortedNormalizedCalculatedTermFrequencies() {
		return sortedNormalizedCalculatedTermFrequencies;
	}
	public Map<Integer, Double> getCalculatedIDFTermFrequencies() {
		return calculatedIDFTermFrequencies;
	}
	public List<Pair<Integer, Double>> getSortedCalculatedIDFTermFrequencies() {
		return sortedCalculatedIDFTermFrequencies;
	}
	
}

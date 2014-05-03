package com.hw6.termFrequency.comparator;

import java.util.Comparator;

import com.hw6.util.Pair;

public class TermFrequencyComparator implements Comparator<Pair<Integer,Double>> {

	@Override
	public int compare(Pair<Integer,Double> leftPair, Pair<Integer,Double> rightPair) {
		return leftPair.getRight().compareTo(rightPair.getRight());
	}

}

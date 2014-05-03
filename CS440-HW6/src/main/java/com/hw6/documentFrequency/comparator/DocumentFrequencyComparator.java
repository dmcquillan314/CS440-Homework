package com.hw6.documentFrequency.comparator;

import java.util.Comparator;

import com.hw6.util.Pair;

public class DocumentFrequencyComparator implements Comparator<Pair<Integer,Double>> {

	@Override
	public int compare(Pair<Integer,Double> leftPair, Pair<Integer,Double> rightPair) {
		return leftPair.getRight().compareTo(rightPair.getRight());
	}

}

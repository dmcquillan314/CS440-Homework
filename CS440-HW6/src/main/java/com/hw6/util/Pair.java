package com.hw6.util;

public class Pair<T, V> {

	T left;
	V right;
	
	public Pair(T left, V right ) {
		this.left = left;
		this.right = right;
	}
	
	public T getLeft() {
		return left;
	}
	public void setLeft(T left) {
		this.left = left;
	}
	public V getRight() {
		return right;
	}
	public void setRight(V right) {
		this.right = right;
	}
	
	
}

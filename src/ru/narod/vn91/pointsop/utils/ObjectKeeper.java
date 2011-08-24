package ru.narod.vn91.pointsop.utils;

/**
 * for synchronization
 */
public class ObjectKeeper<T> {

	public volatile T value;

	public ObjectKeeper(T o) {
		this.value = o;
	}

	public ObjectKeeper() {
	}
}

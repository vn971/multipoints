package ru.narod.vn91.pointsop.server.zagram;

import java.util.ArrayList;

public class MessageQueue {

	final ArrayList<String> stringList;
	volatile int size = 0;
	final int stackSize;

	public MessageQueue(int stackSize) {
		this.stackSize = stackSize;
		stringList = new ArrayList<>(stackSize);
		for (int i = 0; i < stackSize; i++) {
			stringList.add("");
		}
	}

	public synchronized String get(int index) {
		if (index >= 0
				&& index <= size()
				&& index >= size() - stackSize + 1) {
			return stringList.get(index % stackSize);
		} else {
			return null;
		}
	}

	public synchronized void add(String message) {
		size = size + 1;
		stringList.set(size % stackSize, message);
	}

	public synchronized int size() {
		return size;
	}

	public synchronized int sizePlusOne() {
		return size() + 1;
	}
}

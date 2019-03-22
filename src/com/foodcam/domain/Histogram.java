package com.foodcam.domain;

import org.opencv.core.Mat;

public class Histogram {

	private Mat matrix;
	private int directoryIdx;

	public Mat getMatrix() {
		return matrix;
	}

	public void setMatrix(Mat matrix) {
		this.matrix = matrix;
	}

	public int getDirectoryIdx() {
		return directoryIdx;
	}

	public void setDirectoryIdx(int directoryIdx) {
		this.directoryIdx = directoryIdx;
	}
}

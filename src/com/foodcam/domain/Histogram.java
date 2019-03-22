package com.foodcam.domain;

import org.opencv.core.Mat;

public class Histogram {

	private Mat matrix;
	private int directoryIdx;
	private int rank;
	private double r0, r1, r2, r3;

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

	public int getRank() {
		return rank;
	}

	public void addRank(int rank) {
		this.rank += rank;
	}

	public double getR0() {
		return r0;
	}

	public void setR0(double r0) {
		this.r0 = r0;
	}

	public double getR1() {
		return r1;
	}

	public void setR1(double r1) {
		this.r1 = r1;
	}

	public double getR2() {
		return r2;
	}

	public void setR2(double r2) {
		this.r2 = r2;
	}

	public double getR3() {
		return r3;
	}

	public void setR3(double r3) {
		this.r3 = r3;
	}
}

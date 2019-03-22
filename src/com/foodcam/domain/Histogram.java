package com.foodcam.domain;

import org.opencv.core.Mat;

/**
 * 추천순위를 계산하는데 필요한 히스토그램 클래스
 * 
 * matrix
 * Mat 타입의 히스토그램이며 이 매트릭스의 비교를 통해
 * 얼마나 RGB의 분포값이 유사한지를 판단해 추천순위가 정해진다
 * 
 * directoryIdx
 * svm response와 동일한 형태의 값으로
 * 초기 DataSet 로딩 시 디렉토리의 인덱스를 값으로 가지게 된다
 * 이 디렉토리 인덱스는 ResponseMap의 인자로 사용되어 음식의 실제 이름과 링크를 얻는데 사용된다
 * 
 * r0, r1, r2, r3
 * 히스토그램 비교에 사용되는 4개의 메소드에 대한 결과값을 저장하게 된다
 * r0, r2는 결과값이 높을수록 유사한 히스토그램의 성질을 가지며
 * r1, r3는 결과값이 낮을수록 유사한 히스토그램의 성질을 가진다
 * 
 * rank
 * 각 r0, r1, r2, r3으로 비교를 하는 중간에
 * 결과값을 기준으로 히스토그램리스트를 정렬하게 되는데
 * 이 때 순위추출을 위해 순위를 누적하게 되는 변수이다
 * 4개의 메소드를 모두 이용해 최종 순위가 결정되었을때 
 * rank값이 낮을수록 클라이언트가 요청한 이미지와 유사한 히스토그램이다 
 * 
 * @author root
 *
 */
public class Histogram {

	private Mat matrix;
	private int directoryIdx;
	private double r0, r1, r2, r3;
	private int rank;
	

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
	
	public void resetRank() {
		this.rank = 0;
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

package com.foodcam.domain;

import java.util.ArrayList;

import org.opencv.core.Mat;

/**
 * 이미지 훈련에 필요한 데이터를 모아둔 클래스. Value Object로서 사용된다. knn train에 필요한 vector화된 Mat과 라벨
 * 디스크립터 연산을 위한 디스크립터, RGB 분포도의 비교를 위한 히스토그램을 가지고 있다 또한 해당 이미지가 어떤 음식의 이미지인지를
 * 매핑해놓은 com.foodguider.domains.ResponseMapper 클래스도 가지고 있다
 * 
 * @author root
 *
 */
public final class DataSet {
	private Mat FeatureVector;
	private ArrayList<Integer> featureLabelList;
	private ResponseMapper responseMapper;
	private ArrayList<Mat> descriptorList;
	private ArrayList<Mat> histogramList;

	public Mat getFeatureVector() {
		return FeatureVector;
	}

	public void setFeatureVector(Mat featureVector) {
		FeatureVector = featureVector;
	}

	public ArrayList<Integer> getFeatureLabelList() {
		return featureLabelList;
	}

	public void setFeatureLabelList(ArrayList<Integer> featureLabelList) {
		this.featureLabelList = featureLabelList;
	}

	public ResponseMapper getResponseMapper() {
		return responseMapper;
	}

	public void setResponseMapper(ResponseMapper responseMapper) {
		this.responseMapper = responseMapper;
	}

	public ArrayList<Mat> getDescriptorList() {
		return descriptorList;
	}

	public void setDescriptorList(ArrayList<Mat> descriptorList) {
		this.descriptorList = descriptorList;
	}

	public ArrayList<Mat> getHistogramList() {
		return histogramList;
	}

	public void setHistogramList(ArrayList<Mat> histogramList) {
		this.histogramList = histogramList;
	}
}

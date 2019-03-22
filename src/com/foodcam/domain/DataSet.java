package com.foodcam.domain;

import java.util.ArrayList;

import org.opencv.core.Mat;

/**
 * 이미지 훈련에 필요한 데이터를 모아둔 클래스. Value Object로서 사용된다. svm train에 필요한 vector화된 Mat과 라벨,
 * 해당 이미지가 어떤 음식의 이미지인지를 매핑해놓은 com.foodguider.domains.ResponseMapper 클래스를 가지고 있다
 * 
 * @author root
 *
 */
public final class DataSet {
	
	private Mat FeatureVector;
	private ArrayList<Integer> featureLabelList;
	private ResponseMapper responseMapper;
	
	private ArrayList<Histogram> histogramList;

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
	
	public ArrayList<Histogram> getHistogramList() {
		return histogramList;
	}

	public void setHistogramList(ArrayList<Histogram> histogramList) {
		this.histogramList = histogramList;
	}
}

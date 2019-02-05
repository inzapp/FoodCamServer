package com.foodguider.domain;

import java.util.ArrayList;

import org.opencv.core.Mat;

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

package com.foodcam.domain;

import java.util.ArrayList;

import org.opencv.core.Mat;

/**
 * 이미지 훈련에 필요한 데이터를 모아둔 클래스. Value Object로서 사용된다. svm train에 필요한 vector화된 Mat과 라벨,
 * 해당 이미지가 어떤 음식의 이미지인지를 매핑해놓은 com.foodguider.domains.ResponseMapper 클래스를 가지고 있고
 * 추천순위에 필요한 히스토그램 리스트를 가지고 있다
 * 
 * featureVector
 * SVM 훈련에 필요한 인자 중 하나로서 훈련데이터에 저장된
 * 모든 이미지 데이터베이스를 단일행으로 변환해
 * 이를 벡터에 누적시키기 위한 변수이다
 * 
 * featureLabelList
 * featureVector의 벡터화된 훈련데이터의 행에 대한 이름표의 역할로
 * SVM 훈련 시 이 이미지는 어떤 음식이다 라는 것을 알려주기 위한 레이블이다
 * 
 * responseMapper
 * featureLabelList와 연계되는 변수로 SVM predict 시 추출되는
 * response를 이용해 해당 response가 어떤 음식인지를 판별하는데 사용되는 Mapper의 역할을 한다
 * 
 * @author root
 *
 */
public final class DataSet {
	
	private Mat featureVector;
	private ArrayList<Integer> featureLabelList;
	private ResponseMapper responseMapper;
	
	private ArrayList<Histogram> histogramList;

	public Mat getFeatureVector() {
		return featureVector;
	}

	public void setFeatureVector(Mat featureVector) {
		this.featureVector = featureVector;
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

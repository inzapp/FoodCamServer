package com.foodcam.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;
import org.opencv.utils.Converters;

import com.foodcam.core.train.DataSetLoader;
import com.foodcam.domain.DataSet;
import com.foodcam.domain.ResponseMapper;

/**
 * Predictor에 필요한 인스턴스들의 초기화와
 * descriptor matching, histogram calculating과 같은 연산을 담당하는 함수를 가지고 있다
 * @author root
 *
 */
abstract class PredictorManager {
	
	KNearest knn;
	HashMap<Integer, String> responseMap;
	ArrayList<Mat> descriptorList;
	ArrayList<Mat> histogramList;
	int k;

	/**
	 * 이미지의 경로를 인자로 받아 해당 이미지가 어떤 이미지인지 예측해
	 * 해당 이미지를 소개하는 링크를 Json 형태로 리턴
	 * @param imgPath
	 * @return
	 */
	abstract JSONObject predict(Mat receivedImg);
	
	PredictorManager() {
		DataSet trainDataSet = getTrainDataSet();
		
		responseMap = getResponseMap(trainDataSet);
		
		k = getK(trainDataSet);

		knn = getTrainedKnn(trainDataSet);
		
		descriptorList = getDescriptorList(trainDataSet);
		
		histogramList = getHistogramList(trainDataSet);
	}

	/**
	 * 훈련을 위해 필요한 모든 이미지 데이터를 묶어 DataSet 형으로 로드한다
	 * @return
	 */
	private DataSet getTrainDataSet() {
		DataSetLoader dataSetLoader = new DataSetLoader();
		DataSet trainDataSet = dataSetLoader.getTrainDataSet(DataSetLoader.ALL);

		return trainDataSet;
	}

	/**
	 * k-NN의 response와 실제 디렉토리이름(음식이름)을 매핑하기 위한 HashMap을 리턴한다
	 * @param trainDataSet
	 * @return
	 */
	private HashMap<Integer, String> getResponseMap(DataSet trainDataSet) {
		ResponseMapper responseMapper = trainDataSet.getResponseMapper();
		return responseMapper.getResponseMap();
	}
	
	/**
	 * k-NN에서 주변 몇 개의 feature를 보고 해당 개체를 분류할지 정해주는 k를 로드한다
	 * @param trainDataSet
	 * @return
	 */
	private int getK(DataSet trainDataSet) {
		ArrayList<Integer> trainFeatureLabelList = trainDataSet.getFeatureLabelList();
		int minFeatureCount = getMinFeatureCount(trainFeatureLabelList);
		
		return (int) (Math.sqrt(minFeatureCount) % 2 != 0 ? Math.sqrt(minFeatureCount)
				: Math.sqrt(minFeatureCount) + 1);
	}

	/**
	 * feature리스트를 인자로 받아 해당 리스트에 존재하는 feature 중
	 * 가장 적은 feature의 갯수를 리턴한다
	 * @param trainFeatureLabelList
	 * @return
	 */
	private int getMinFeatureCount(ArrayList<Integer> trainFeatureLabelList) {
		int count = 0;
		int minCount = Integer.MAX_VALUE;
		int beforeLabel = trainFeatureLabelList.get(0);
		for (int curLabel : trainFeatureLabelList) {
			if (beforeLabel == curLabel) {
				count++;
			} else {
				minCount = count < minCount ? count : minCount;
				count = 0;
			}

			beforeLabel = curLabel;
		}

		return minCount;
	}

	/**
	 * featureVector과 labelList가 train된 KNearest를 로드한다
	 * @param trainDataSet
	 * @return
	 */
	private KNearest getTrainedKnn(DataSet trainDataSet) {
		KNearest newKnn = KNearest.create();
		Mat trainFeatureVector = trainDataSet.getFeatureVector();
		
		ArrayList<Integer> trainFeatureLabelList = trainDataSet.getFeatureLabelList();
		Mat convertedLabelList = Converters.vector_int_to_Mat(trainFeatureLabelList);
		
		boolean res = newKnn.train(trainFeatureVector, Ml.ROW_SAMPLE, convertedLabelList);
		return res ? newKnn : null;
	}
	
	/**
	 * descriptorMatching에 필요한 descriptor 리스트를 반환한다
	 * @param trainDataSet
	 * @return
	 */
	private ArrayList<Mat> getDescriptorList(DataSet trainDataSet){
		return trainDataSet.getDescriptorList();
	}
	
	/**
	 * histogramComparing에 필요한 histogram 리스트를 반환한다
	 * @param trainDataSet
	 * @return
	 */
	private ArrayList<Mat> getHistogramList(DataSet trainDataSet) {
		return trainDataSet.getHistogramList();
	}
}

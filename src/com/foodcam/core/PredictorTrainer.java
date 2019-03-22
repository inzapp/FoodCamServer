package com.foodcam.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;
import org.opencv.ml.TrainData;
import org.opencv.utils.Converters;

import com.foodcam.domain.DataSet;
import com.foodcam.domain.Histogram;
import com.foodcam.domain.ResponseMapper;
import com.foodcam.util.LinkMapper;
import com.foodcam.util.pRes;

/**
 * Predictor의 훈련을 담당하는 클래스
 * DataSet을 인자로 받아 필요한 정보를 훈련하고 로드한다
 * 
 * @author root
 *
 */
abstract class PredictorTrainer {

	SVM classifier;
	HashMap<Integer, String> responseMap;
	HashMap<String, String> linkMap;
	ArrayList<Histogram> histogramList; 

	PredictorTrainer() {
		linkMap = getLinkMap();
	}
	
	/**
	 * DataSet을 인자로 훈련을 진행한다
	 * ResponseMap 저장, SVM train, Histogram 리스트의 저장으로 진행된다
	 * @param trainDataSet
	 */
	public void train(DataSet trainDataSet) {
		pRes.log("이미지 훈련을 시작합니다. 이 작업은 몇 분 정도 소요될 수 있습니다.");
		responseMap = getResponseMap(trainDataSet);
		classifier = getTrainedSVM(trainDataSet);
		histogramList = trainDataSet.getHistogramList();
		pRes.log("이미지 훈련을 완료했습니다.");
	}

	/**
	 * SVM의 response와 실제 디렉토리이름(음식이름)을 매핑하기 위한 HashMap을 리턴한다
	 * 
	 * @param trainDataSet
	 * @return
	 */
	private HashMap<Integer, String> getResponseMap(DataSet trainDataSet) {
		ResponseMapper responseMapper = trainDataSet.getResponseMapper();
		return responseMapper.getResponseMap();
	}

	/**
	 * 추출된 디렉토리이름을 키로 해당 음식에 대한 소개 링크를 받아오는 링크 맵을 리턴한다
	 * 
	 * @return
	 */
	private HashMap<String, String> getLinkMap() {
		LinkMapper linkMapper = new LinkMapper();
		return linkMapper.getLinkMap();
	}

	/**
	 * featureVector과 labelList가 train된 SVM을 로드한다
	 * 
	 * @param trainDataSet
	 * @return
	 */
	private SVM getTrainedSVM(DataSet trainDataSet) {
		SVM newSVM = SVM.create();
		newSVM.setKernel(SVM.LINEAR);
		newSVM.setType(SVM.C_SVC);
		Mat trainFeatureVector = trainDataSet.getFeatureVector();

		ArrayList<Integer> trainFeatureLabelList = trainDataSet.getFeatureLabelList();
		Mat convertedLabelList = Converters.vector_int_to_Mat(trainFeatureLabelList);
		
		TrainData trainData = TrainData.create(trainFeatureVector, Ml.ROW_SAMPLE, convertedLabelList);
		boolean res = newSVM.train(trainData);
		
		return res ? newSVM : null;
	}
}

package com.foodcam.core;

import java.util.ArrayList;

import org.json.JSONObject;
import org.opencv.core.Mat;

import com.foodcam.core.train.DataSetLoader;
import com.foodcam.domain.DataSet;

/**
 * 훈련된 데이터를 기반으로 새로운 이미지가 어떤 음식인지를 예측하는 클래스
 * 
 * @author root
 *
 */
public final class Predictor extends PredictorOperator {

	private static class Singleton {
		static final Predictor INSTANCE = new Predictor();
	}

	public static Predictor getInstance() {
		return Singleton.INSTANCE;
	}

	/**
	 * 이미지 Mat을 인자로 받아 해당 이미지가 어떤 이미지인지 예측해 해당 이미지를 소개하는 링크를 Json 형태로 리턴
	 * 
	 * @param imgPath
	 * @return
	 */
	public synchronized JSONObject predict(Mat receivedImg) {
		DataSet requestDataSet = getDataSetOfReceivedImg(receivedImg);
		
		ArrayList<String> foodLinkListByTotalRanking = getFoodLinkListByTotalRanking(requestDataSet);
		JSONObject foodLinkJsonByRanking = new JSONObject();
		
		for(int i=0; i<foodLinkListByTotalRanking.size(); i++) 
			foodLinkJsonByRanking.put(Integer.toString(i + 1), foodLinkListByTotalRanking.get(i));
		
		return foodLinkJsonByRanking;
	}
	
	/**
	 * 클라이언트 요청이 아닌 서버 내부 테스트에 사용되는 raw response
	 * 
	 * @param requestFeature
	 * @return
	 */
	public int rawPredict(Mat requestFeature) {
		return (int) classifier.predict(requestFeature);
	}

	/**
	 * 클라이언트로부터 수신된 이미지에 대한 DataSet을 리턴한다 해당 DataSet은 k-NN, descriptorMatching,
	 * histogramComparing에 사용된다
	 * 
	 * @param receivedImg
	 * @return
	 */
	private DataSet getDataSetOfReceivedImg(Mat receivedImg) {
		DataSetLoader dataSetLoader = new DataSetLoader();
		DataSet dataSet = dataSetLoader.getRequestDataSet(receivedImg);

		return dataSet;
	}
}
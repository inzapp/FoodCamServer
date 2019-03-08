package com.foodcam.core;

import java.util.ArrayList;

import org.json.JSONObject;
import org.opencv.core.Mat;

import com.foodcam.core.train.DataSetLoader;
import com.foodcam.domain.DataSet;
import com.foodcam.util.pRes;

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
	public JSONObject predict(Mat receivedImg) {
		DataSet requestDataSet = getDataSetOfReceivedImg(receivedImg);

//		String foodName = getFoodName(requestDataSet);
//		String foodLink = getFoodLink(foodName);
//		String firstLink = getFirstResponseFoodLink(requestDataSet);
		
		ArrayList<String> foodLinkListByTotalRanking = getFoodLinkListByTotalRanking(requestDataSet);
		JSONObject json = new JSONObject();
		
		for(int i=0; i<foodLinkListByTotalRanking.size(); i++) 
			json.put(Integer.toString(i + 1), foodLinkListByTotalRanking.get(i));
		
		return json;
		

//		pRes.log(foodName);
//		pRes.log(foodLink);

//		return getResult(foodLink);
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

	/**
	 * 로드된 DataSet을 이용해 해당 이미지가 어떤 이미지인지 예측한다 예측 방법에는 k-NN 분류, descriptorMatching,
	 * histogramComparing이 이용된다
	 * 
	 * @param requestDataSet
	 * @return
	 */
	private String getFoodName(DataSet requestDataSet) {
		int response = (int) knn.findNearest(requestDataSet.getFeatureVector().row(0), k, new Mat());
		return responseMap.get(response);
	}

	/**
	 * 예측된 음식이름을 이용해 해당 음식을 소개하는 웹링크를 받아온다
	 * 
	 * @param foodName
	 * @return
	 */
	private String getFoodLink(String foodName) {
		return linkMap.get(foodName);
	}

	/**
	 * 링크를 받아온 후 최종적인 JSONObject형 리턴을 위해 해당 링크를 JSONObject에 담아 리턴한다
	 * 
	 * @param foodLink
	 * @return
	 */
	private JSONObject getResult(String foodLink) {
		JSONObject json = new JSONObject();
		try {
			json.put("link", foodLink);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
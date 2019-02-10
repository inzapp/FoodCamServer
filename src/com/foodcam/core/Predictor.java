package com.foodcam.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;
import org.opencv.utils.Converters;

import com.foodcam.core.train.DataSetLoader;
import com.foodcam.domain.DataSet;
import com.foodcam.domain.ResponseMapper;
import com.foodcam.util.LinkMapper;
import com.foodcam.util.pRes;

/**
 * 훈련된 데이터를 기반으로 새로운 이미지가 어떤 음식인지를 예측하는 클래스
 * @author root
 *
 */
public final class Predictor {

	private KNearest knn;
	private HashMap<Integer, String> responseMap;
	private ArrayList<Mat> descriptorList;
	private ArrayList<Mat> histogramList;
	private int k;

	private Predictor() {
		DataSet trainDataSet = getTrainDataSet();
		
		getResponseMap(trainDataSet);

		calculateK(trainDataSet);

		getTrainedKnn(trainDataSet);

		loadCalculatedValue(trainDataSet);
	}

	private DataSet getTrainDataSet() {
		DataSetLoader dataSetLoader = new DataSetLoader();
		DataSet trainDataSet = dataSetLoader.getTrainDataSet(DataSetLoader.ALL);

		return trainDataSet;
	}

	private void getResponseMap(DataSet trainDataSet) {
		ResponseMapper responseMapper = trainDataSet.getResponseMapper();
		responseMap = responseMapper.getResponseMap();
	}

	private void calculateK(DataSet trainDataSet) {
		ArrayList<Integer> trainFeatureLabelList = trainDataSet.getFeatureLabelList();
		int minFeatureCount = getMinFeatureCount(trainFeatureLabelList);
		k = (int) (Math.sqrt(minFeatureCount) % 2 != 0 ? Math.sqrt(minFeatureCount)
				: Math.sqrt(minFeatureCount) + 1);
	}

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

	private void getTrainedKnn(DataSet trainDataSet) {
		knn = KNearest.create();
		Mat trainFeatureVector = trainDataSet.getFeatureVector();
		ArrayList<Integer> trainFeatureLabelList = trainDataSet.getFeatureLabelList();
		Mat convertedLabelList = Converters.vector_int_to_Mat(trainFeatureLabelList);
		knn.train(trainFeatureVector, Ml.ROW_SAMPLE, convertedLabelList);
	}

	private void loadCalculatedValue(DataSet trainDataSet) {
		descriptorList = trainDataSet.getDescriptorList();
		histogramList = trainDataSet.getHistogramList();
	}

	private static class Singleton {
		static final Predictor INSTANCE = new Predictor();
	}

	public static Predictor getInstance() {
		return Singleton.INSTANCE;
	}

	/**
	 * 이미지의 경로를 인자로 받아 해당 이미지가 어떤 이미지인지 예측해
	 * 해당 이미지를 소개하는 링크를 Json 형태로 리턴
	 * @param imgPath
	 * @return
	 */
	public JSONObject predict(Mat receivedImg) {
		DataSet requestDataSet = getRequestDataSet(receivedImg);

		int response = (int) knn.findNearest(requestDataSet.getFeatureVector().row(0), k, new Mat());
		String foodName = responseMap.get(response);

		pRes.log(foodName);

		String link = LinkMapper.getInstance().getLinkMap().get(foodName);
		pRes.log(link);
		
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("link", link);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultJson;
	}

	private DataSet getRequestDataSet(Mat receivedImg) {
		DataSetLoader dataSetLoader = new DataSetLoader();
		DataSet requestDataSet = dataSetLoader.getRequestDataSet(receivedImg);

		return requestDataSet;
	}
}
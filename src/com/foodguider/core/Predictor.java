package com.foodguider.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;
import org.opencv.utils.Converters;

import com.foodguider.core.train.DataSetLoader;
import com.foodguider.domain.DataSet;
import com.foodguider.domain.ResponseMapper;
import com.foodguider.util.LinkMapper;
import com.foodguider.util.pRes;

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

	public JSONObject predict(String imgPath) {
		DataSet requestDataSet = getRequestDataSet(imgPath);

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

	private DataSet getRequestDataSet(String imgPath) {
		DataSetLoader dataSetLoader = new DataSetLoader();
		DataSet requestDataSet = dataSetLoader.getRequestDataSet(imgPath);

		return requestDataSet;
	}
}
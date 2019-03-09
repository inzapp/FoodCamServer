package com.foodcam.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Mat;

import com.foodcam.core.Predictor;
import com.foodcam.core.train.DataSetLoader;
import com.foodcam.domain.DataSet;
import com.foodcam.domain.MatchCountAccumulater;
import com.foodcam.domain.ResponseMapper;

/**
 * svm 매칭률 측정을 위한 테스트 클래스
 * 
 * @author root
 *
 */
public final class SVMAccuracyTester implements Tester {

	@Override
	public void test() {
		
		DataSetLoader trainDataSetLoader = new DataSetLoader();
		
		DataSet halfTrainDataSet = trainDataSetLoader.getTrainDataSet(DataSetLoader.HALF_TRAIN);
		Predictor.getInstance().train(halfTrainDataSet);

		DataSet halfTestDataSet = trainDataSetLoader.getTrainDataSet(DataSetLoader.HALF_TEST);
		Mat testFeatureVector = halfTestDataSet.getFeatureVector();
		ArrayList<Integer> halfTestLabelList = halfTestDataSet.getFeatureLabelList();

		ResponseMapper responseMapper = halfTestDataSet.getResponseMapper();
		HashMap<Integer, String> responseMap = responseMapper.getResponseMap();

		HashMap<Integer, MatchCountAccumulater> matchCountAccumulaterOf = new HashMap<>();
		int maxIndexOfTestLabelList = halfTestLabelList.get(halfTestLabelList.size() - 1);
		
		for (int i = 0; i <= maxIndexOfTestLabelList; i++)
			matchCountAccumulaterOf.put(i, new MatchCountAccumulater());

		System.out.println("matchCountAccumulateOf size : " + responseMap.size());

		float totalMatchCount = 0;
		for (int i = 0; i < testFeatureVector.rows(); i++) {
			Mat curTestFeature = testFeatureVector.row(i);
			
			int label = halfTestLabelList.get(i);
			int response = (int) Predictor.getInstance().rawPredict(curTestFeature);
			
			if (response == label) {
				matchCountAccumulaterOf.get(label).addMatchCount();
				totalMatchCount++;
			}
			
			String originalFoodName = responseMap.get(label);
			String responseFoodName = responseMap.get(response);

			System.out.println("[" + originalFoodName  + "] " + responseFoodName);
			matchCountAccumulaterOf.get(label).addTotalFeatureCount();
		}

		System.out.println();
		for (int i = 0; i < matchCountAccumulaterOf.size(); i++)
			System.out.println(responseMap.get(i) + " " + matchCountAccumulaterOf.get(i).getMatchingRate() + "%");

		float totalMatchingRate = (totalMatchCount / testFeatureVector.rows()) * 100.0f;
		System.out.println("\nTotal matching rate : " + totalMatchingRate + "%");
	}
}

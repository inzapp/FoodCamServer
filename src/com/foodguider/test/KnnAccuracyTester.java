package com.foodguider.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;
import org.opencv.utils.Converters;

import com.foodguider.core.train.DataSetLoader;
import com.foodguider.domain.DataSet;
import com.foodguider.domain.MatchCountAccumulater;
import com.foodguider.domain.ResponseMapper;


public final class KnnAccuracyTester implements Tester{

	@Override
	public void test() {
		DataSetLoader trainDataSetLoader = new DataSetLoader();
		
		DataSet halfTrainDataSet = trainDataSetLoader.getTrainDataSet(DataSetLoader.HALF_TRAIN);
		Mat trainFeatureVector = halfTrainDataSet.getFeatureVector();
		ArrayList<Integer> halfTrainLabelList = halfTrainDataSet.getFeatureLabelList();
		
		KNearest knn = KNearest.create();
		knn.train(trainFeatureVector, Ml.ROW_SAMPLE, Converters.vector_int_to_Mat(halfTrainLabelList));

		DataSet halfTestDataSet = trainDataSetLoader.getTrainDataSet(DataSetLoader.HALF_TEST);
		Mat testFeatureVector = halfTestDataSet.getFeatureVector();
		ArrayList<Integer> halfTestLabelList = halfTestDataSet.getFeatureLabelList();
		
		ResponseMapper responseMapper = halfTestDataSet.getResponseMapper();
		HashMap<Integer, String> responseMap = responseMapper.getResponseMap();
		
		HashMap<Integer, MatchCountAccumulater> matchCountAccumulaterOf = new HashMap<>();
		for(int i=0; i<=halfTestLabelList.get(halfTestLabelList.size() - 1); i++) {
			matchCountAccumulaterOf.put(i, new MatchCountAccumulater());
		}
		
		System.out.println("matchCountAccumulateOf size : " + halfTestLabelList.get(halfTestLabelList.size() - 1));
		
		float totalMatchCount = 0;
		for(int i=0; i<testFeatureVector.rows(); i++) {
			int response = (int) knn.predict(testFeatureVector.row(i));
			if(response == halfTestLabelList.get(i)) {
				matchCountAccumulaterOf.get(halfTestLabelList.get(i)).addMatchCount();
				totalMatchCount++;
			}

			System.out.println("[" + responseMap.get(halfTestLabelList.get(i)) + "] " + responseMap.get(response));
			matchCountAccumulaterOf.get(halfTestLabelList.get(i)).addTotalFeatureCount();
		}
		
		for(int i=0; i<matchCountAccumulaterOf.size(); i++) {
			System.out.println(responseMap.get(i) + " " + matchCountAccumulaterOf.get(i).getMatchingRate() + "%");
		}
		
		float totalMatchingRate = (totalMatchCount / testFeatureVector.rows()) * 100.0f;
		System.out.println("Total matching rate : " + totalMatchingRate + "%");
	}
}

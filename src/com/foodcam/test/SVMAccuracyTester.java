package com.foodcam.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Mat;

import com.foodcam.core.Predictor;
import com.foodcam.core.train.DataSetLoader;
import com.foodcam.domain.DataSet;
import com.foodcam.domain.MatchCountAccumulater;
import com.foodcam.domain.ResponseMapper;
import com.foodcam.util.pRes;

/**
 * SVM 매칭률 측정을 위한 테스트 클래스
 * 
 * @author root
 *
 */
public final class SVMAccuracyTester implements Tester {

	@Override
	public void test() {
		pRes.log("SVM Accuracy 테스트 모드");
		int minTrainDataCount = Integer.MAX_VALUE;
		File[] dirs = new File(pRes.TRAIN_DATA_PATH).listFiles();
		for(File curDir : dirs) {
			File[] files = curDir.listFiles();
			minTrainDataCount = files.length < minTrainDataCount ? files.length : minTrainDataCount;
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int trainCount, testCount;
		while(true) {
			System.out.printf("학습시킬 이미지의 갯수를 입력하세요(최대 %d) : ", minTrainDataCount);
			try {
				trainCount = Integer.parseInt(br.readLine());
				if(1 <= trainCount && trainCount <= minTrainDataCount) {
					break;	
				} else {
					System.out.println("갯수는 1이상 최대값 이하만 가능합니다.");
					continue;	
				}
			} catch(Exception e) {
				continue;
			}	
		}
		
		while(true) {
			System.out.printf("테스트할 이미지의 갯수를 입력하세요(최대 %d) : ", minTrainDataCount);
			try {
				testCount = Integer.parseInt(br.readLine());
				if(1 <= testCount && testCount <= minTrainDataCount) {
					break;	
				} else {
					System.out.println("갯수는 1이상 최대값 이하만 가능합니다.");
					continue;	
				}
			} catch(Exception e) {
				continue;
			}	
		}
		
		DataSetLoader trainDataSetLoader = new DataSetLoader();
		
		DataSet halfTrainDataSet = trainDataSetLoader.getTrainDataSet(trainCount);
		Predictor.getInstance().train(halfTrainDataSet);

		DataSet halfTestDataSet = trainDataSetLoader.getTrainDataSet(testCount);
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

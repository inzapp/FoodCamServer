package com.foodcam.core;

import java.util.ArrayList;

import org.opencv.core.Mat;

import com.foodcam.domain.DataSet;

/**
 * Predictor의 순위결정 연산을 담당하는 클래스
 * @author root
 *
 */
public class PredictorOperator extends PredictorInitializer {
	
	ArrayList<String> getFoodLinkListByTotalRanking(DataSet requestDataSet) {
		
		ArrayList<String> foodLinkListByTotalRanking = new ArrayList<>();
		foodLinkListByTotalRanking.add(getFirstResponseFoodLink(requestDataSet));
		
		
		
		return foodLinkListByTotalRanking;
		
	}
	
	private String getFirstResponseFoodLink(DataSet requestDataSet) {
		
		return linkMap.get(responseMap.get(getFirstResponse(requestDataSet)));
	}

	private int getFirstResponse(DataSet requestDataSet) {
		
		return (int) knn.findNearest(requestDataSet.getFeatureVector().row(0), k, new Mat());
	}
	
	
}

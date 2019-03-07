package com.foodcam.core;

import java.util.ArrayList;

import org.opencv.core.Mat;

import com.foodcam.domain.DataSet;

/**
 * Predictor의 순위결정 연산을 담당하는 클래스
 * 
 * @author root
 *
 */
public class PredictorOperator extends PredictorInitializer {

	ArrayList<String> getFoodLinkListByTotalRanking(DataSet requestDataSet) {

		ArrayList<String> foodLinkListByTotalRanking = new ArrayList<>();
		foodLinkListByTotalRanking.add(getFirstResponseFoodLink(requestDataSet));

		ArrayList<String> foodLinkListExceptFirstRanking = getFoodLinkListExceptFirstRanking(requestDataSet);
		for (String curExceptFirstRanking : foodLinkListExceptFirstRanking)
			foodLinkListByTotalRanking.add(curExceptFirstRanking);

		return foodLinkListByTotalRanking;

	}

	private String getFirstResponseFoodLink(DataSet requestDataSet) {

		return linkMap.get(responseMap.get(getFirstResponse(requestDataSet)));
	}

	private int getFirstResponse(DataSet requestDataSet) {

		return (int) knn.findNearest(requestDataSet.getFeatureVector().row(0), k, new Mat());
	}

	private ArrayList<String> getFoodLinkListExceptFirstRanking(DataSet requestDataSet) {

		ArrayList<String> foodLinkListExceptFirstRanking = new ArrayList<>();

		return foodLinkListExceptFirstRanking;
	}

}

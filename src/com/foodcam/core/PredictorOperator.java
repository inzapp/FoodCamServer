package com.foodcam.core;

import java.util.ArrayList;

import com.foodcam.domain.DataSet;

/**
 * Predictor의 순위결정 연산을 담당하는 클래스
 * 
 * @author root
 *
 */
public class PredictorOperator extends PredictorTrainer {

	/**
	 * 가장 유사한 음식들의 소개링크를 순위대로 순차저장해 리턴한다
	 * ex) .get(0) : 1순위 음식의 소개링크
	 * 
	 * @param requestDataSet
	 * @return
	 */
	ArrayList<String> getFoodLinkListByTotalRanking(DataSet requestDataSet) {

		ArrayList<String> foodLinkListByTotalRanking = new ArrayList<>();
		foodLinkListByTotalRanking.add(getFirstResponseFoodLink(requestDataSet));

		return foodLinkListByTotalRanking;
	}

	/**
	 * 가장 비슷한 1순위 음식의 소개링크를 받아온다
	 * 
	 * @param requestDataSet
	 * @return
	 */
	private String getFirstResponseFoodLink(DataSet requestDataSet) {

		return linkMap.get(responseMap.get(getFirstResponse(requestDataSet)));
	}

	/**
	 * 가장 비슷한 1순위 음식의 knn response를 받아온다
	 * 
	 * @param requestDataSet
	 * @return
	 */
	private int getFirstResponse(DataSet requestDataSet) {

		return (int) svm.predict(requestDataSet.getFeatureVector().row(0));
	}
}

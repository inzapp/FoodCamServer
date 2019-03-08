package com.foodcam.core;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.foodcam.domain.DataSet;

/**
 * Predictor의 순위결정 연산을 담당하는 클래스
 * 
 * @author root
 *
 */
public class PredictorOperator extends PredictorInitializer {

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

//		ArrayList<String> foodLinkListExceptFirstRanking = getFoodLinkListExceptFirstRanking(requestDataSet);
//		for (String curExceptFirstRanking : foodLinkListExceptFirstRanking)
//			foodLinkListByTotalRanking.add(curExceptFirstRanking);

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

		return (int) knn.findNearest(requestDataSet.getFeatureVector().row(0), k, new Mat());
	}

//	private ArrayList<String> getFoodLinkListExceptFirstRanking(DataSet requestDataSet) {
//
//		ArrayList<String> foodLinkListExceptFirstRanking = new ArrayList<>();
//		ArrayList<Mat> requestHistogramList = requestDataSet.getHistogramList();
//		Mat requestHistogram = requestHistogramList.get(0);
//		
////		double m0Res, m1Res, m2Res, m3Res, t0Res, t1Res, t2Res, t3Res;
////		m0Res = m2Res = t0Res = t2Res = -1;
////		m1Res = m1Res = t0Res = t2Res = Double.MAX_VALUE;
//		
//		double bestCompareResult, curCompareResult;
//		bestCompareResult = curCompareResult = -1;
//		Mat bestHistogram = new Mat();
//		
//		for(Mat curServerHistogram : this.histogramList) {
//			curCompareResult = Imgproc.compareHist(curServerHistogram, requestHistogram, Imgproc.HISTCMP_INTERSECT);
//			if(curCompareResult < bestCompareResult) {
//				bestHistogram = curServerHistogram;
//				bestCompareResult = curCompareResult;
//			}
//		}
//
//		return foodLinkListExceptFirstRanking;
//	}

}

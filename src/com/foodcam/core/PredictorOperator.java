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
		ArrayList<Mat> requestHistogramList = requestDataSet.getHistogramList();
		Mat requestHistogram = requestHistogramList.get(0);
		
		double m0Res, m1Res, m2Res, m3Res, t1Res, t2Res, t3Res, t4Res;
		m0Res = m1Res = m2Res = m3Res = -1;
		
		
		for(Mat curServerHistogram : this.histogramList) {
			m0Res = Imgproc.compareHist(curServerHistogram, requestHistogram, Imgproc.HISTCMP_CORREL);
			m1Res = Imgproc.compareHist(curServerHistogram, requestHistogram, Imgproc.HISTCMP_CHISQR);
			m2Res = Imgproc.compareHist(curServerHistogram, requestHistogram, Imgproc.HISTCMP_INTERSECT);
			m3Res = Imgproc.compareHist(curServerHistogram, requestHistogram, Imgproc.HISTCMP_HELLINGER);
		}

		return foodLinkListExceptFirstRanking;
	}

}

package com.foodcam.core;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.foodcam.domain.DataSet;
import com.foodcam.domain.Histogram;

/**
 * Predictor의 순위결정 연산을 담당하는 클래스
 * 
 * @author root
 *
 */
public class PredictorOperator extends PredictorTrainer {

	private int addRankCount = 2;

	/**
	 * 가장 유사한 음식들의 소개링크를 순위대로 순차저장해 리턴한다 ex) .get(0) : 1순위 음식의 소개링크
	 * 
	 * @param requestDataSet
	 * @return
	 */
	ArrayList<String> getFoodLinkListByTotalRanking(DataSet requestDataSet) {

		ArrayList<String> foodLinkListByTotalRanking = new ArrayList<>();

		int firstResponse = getFirstResponse(requestDataSet);
		String firstFoodLink = linkMap.get(responseMap.get(firstResponse));
		foodLinkListByTotalRanking.add(firstFoodLink);

		ArrayList<String> theOtherFoodLinkList = getTheOtherFoodLinkList(requestDataSet, firstResponse);
		for (String curLink : theOtherFoodLinkList)
			foodLinkListByTotalRanking.add(curLink);

		return foodLinkListByTotalRanking;
	}

	/**
	 * 가장 비슷한 1순위 음식의 knn response를 받아온다
	 * 
	 * @param requestDataSet
	 * @return
	 */
	private int getFirstResponse(DataSet requestDataSet) {
		return (int) classifier.predict(requestDataSet.getFeatureVector().row(0));
	}

	private ArrayList<String> getTheOtherFoodLinkList(DataSet requestDataSet, int firstResponse) {

		ArrayList<String> theOtherFoodLinkList = new ArrayList<>();

		Mat requestMatrix = requestDataSet.getHistogramList().get(0).getMatrix();
		ArrayList<Histogram> histogramList = requestDataSet.getHistogramList();

		for (Histogram curHistogram : histogramList) {
			Mat curMatrix = curHistogram.getMatrix();
			curHistogram.setR0(Imgproc.compareHist(curMatrix, requestMatrix, 0));
			curHistogram.setR1(Imgproc.compareHist(curMatrix, requestMatrix, 1));
			curHistogram.setR2(Imgproc.compareHist(curMatrix, requestMatrix, 2));
			curHistogram.setR3(Imgproc.compareHist(curMatrix, requestMatrix, 3));
		}

		sortByMethodAndAddRank();
		sortByRank();

		for (int i = 0; i < addRankCount; ++i) {
			int curResponse = histogramList.get(i).getDirectoryIdx();
			if (curResponse == firstResponse) {
				++addRankCount;
				continue;
			}
			theOtherFoodLinkList.add(linkMap.get(responseMap.get(curResponse)));
		}

		return theOtherFoodLinkList;
	}

	private void sortByMethodAndAddRank() {
		histogramList.sort((a, b) -> {
			return Double.compare(b.getR0(), a.getR0());
		});
		addRankToSortedHistogramList();

		histogramList.sort((a, b) -> {
			return Double.compare(a.getR1(), b.getR1());
		});
		addRankToSortedHistogramList();

		histogramList.sort((a, b) -> {
			return Double.compare(b.getR2(), a.getR2());
		});
		addRankToSortedHistogramList();

		histogramList.sort((a, b) -> {
			return Double.compare(a.getR3(), b.getR3());
		});
		addRankToSortedHistogramList();
	}

	private void addRankToSortedHistogramList() {
		for (int i = 0; i < histogramList.size(); ++i)
			histogramList.get(i).addRank(i + 1);
	}

	private void sortByRank() {
		histogramList.sort((a, b) -> {
			return Integer.compare(a.getRank(), b.getRank());
		});
	}
}

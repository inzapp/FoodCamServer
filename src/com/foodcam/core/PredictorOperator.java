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

	/**
	 * 1순위 이외에 몇개의 순위를 더 추출할지에 대한 갯수
	 * 2라면 1, 2, 3순위까지 추출하게 된다
	 */
	private final int addRankCount = 2;

	/**
	 * 가장 유사한 음식들의 소개링크를 순위대로 순차저장해 리턴한다 ex) .get(0) : 1순위 음식의 소개링크
	 * 
	 * @param requestDataSet
	 * @return
	 */
	ArrayList<String> getFoodLinkListByTotalRanking(DataSet requestDataSet) {
		ArrayList<String> foodLinkListByTotalRank = new ArrayList<>();

		int firstResponse = getFirstResponse(requestDataSet);
		String firstFoodLink = linkMap.get(responseMap.get(firstResponse));
		foodLinkListByTotalRank.add(firstFoodLink);

		addTheOtherFoodLinkList(foodLinkListByTotalRank, requestDataSet);

		return foodLinkListByTotalRank;
	}

	/**
	 * 가장 비슷한 1순위 음식의 svm response를 받아온다
	 * 
	 * @param requestDataSet
	 * @return
	 */
	private int getFirstResponse(DataSet requestDataSet) {
		return (int) classifier.predict(requestDataSet.getFeatureVector().row(0));
	}

	/**
	 * 1순위 이외의 추천링크를 더한다
	 * 1순위를 제외한 순위에는 이미지 히스토그램 매트릭스를 이용해
	 * 가장 유사한 히스토그램을 가진 음식이 추천순위에 추가된다
	 * 
	 * 히스토그램 비교 메소드를 이용해 각 결과값을 저장하고
	 * 해당 값을 기준으로 순위를 부여한다
	 * 
	 * 총 4개의 메소드로 진행이 되며
	 * 각 순위가 모두 부여된 후에 총 순위를 기준으로 히스토그램 리스트를 정렬한다
	 * 정렬된 리스트의 0번 인덱스에는 가장 유사한 음식의 히스토그램이 위치하게 된다
	 * 0번부터 2순위 이상의 추천순위를 theOtherFoodLinkList에 추가하되
	 * 중복검사를 진행하며 추가한다
	 * 
	 * @param foodLinkListByTotalRank
	 * @param requestDataSet
	 */
	private void addTheOtherFoodLinkList(ArrayList<String> foodLinkListByTotalRank, DataSet requestDataSet) {
		Mat requestMatrix = requestDataSet.getHistogramList().get(0).getMatrix();

		for (Histogram curHistogram : histogramList) {
			Mat curMatrix = curHistogram.getMatrix();
			curHistogram.setR0(Imgproc.compareHist(curMatrix, requestMatrix, 0));
			curHistogram.setR1(Imgproc.compareHist(curMatrix, requestMatrix, 1));
			curHistogram.setR2(Imgproc.compareHist(curMatrix, requestMatrix, 2));
			curHistogram.setR3(Imgproc.compareHist(curMatrix, requestMatrix, 3));
		}

		sortByMethodAndAddRank();
		sortByRank();

		int addCount = 0;
		int i = 0;
		while (true) {
			int curResponse = histogramList.get(i++).getDirectoryIdx();
			String curLink = linkMap.get(responseMap.get(curResponse));
			if (foodLinkListByTotalRank.contains(curLink))
				continue;

			foodLinkListByTotalRank.add(linkMap.get(responseMap.get(curResponse)));
			++addCount;
			if (addCount == addRankCount)
				break;
		}

		resetRank();
	}

	/**
	 * 히스토그램 비교 메소드의 결과값을 기준으로 히스토그램 리스트를 정렬하고 순위를 부여한다
	 */
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

	/**
	 * 정렬된 리스트에 순차대로 순위를 부여한다
	 */
	private void addRankToSortedHistogramList() {
		for (int i = 0; i < histogramList.size(); ++i)
			histogramList.get(i).addRank(i + 1);
	}

	/**
	 * 히스토그램 리스트를 최종 결정된 rank로 오름차순 정렬을 해 
	 * 가장 비슷한 음식의 히스토그램이 0번 인덱스에 위치하게 된다
	 */
	private void sortByRank() {
		histogramList.sort((a, b) -> {
			return Integer.compare(a.getRank(), b.getRank());
		});
	}

	private void resetRank() {
		for (Histogram curHistogram : histogramList)
			curHistogram.resetRank();
	}
}

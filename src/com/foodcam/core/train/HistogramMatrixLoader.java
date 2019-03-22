package com.foodcam.core.train;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import com.foodcam.util.pRes;

/**
 * 이미지 RGB값의 분포도인 히스토그램을 로드하는 클래스로 histogram calculation에 사용된다
 * 
 * @author root
 *
 */
class HistogramMatrixLoader implements DataLoader {
	private Mat mask;
	private MatOfInt channel;
	private MatOfInt histSize;
	private MatOfFloat histRange;
	private boolean accumulate;

	public HistogramMatrixLoader() {
		mask = new Mat();
		channel = new MatOfInt(0);
		histSize = new MatOfInt(256);
		histRange = new MatOfFloat(0f, 255f);
		accumulate = false;
	}

	/**
	 * 이미지 매트릭스를 Blue, Green, Red 채널로 각각 분할 후
	 * 단일채널을 이용해 히스토그램을 계산한다
	 */
	@Override
	public Mat load(Mat rawImg) {

		List<Mat> bgrPlanes = splitBGR(rawImg);
		Mat histogramMatrix = new Mat();

		getHistogram(bgrPlanes, histogramMatrix);

		if (histogramMatrix.empty()) {
			pRes.log("Histogram get failure");
			return null;
		}

		return histogramMatrix;
	}

	/**
	 * 단일 채널을 사용해 연산 속도를 높이기 위해
	 * Blue, Green, Red채널을 분할한다
	 * 
	 * @param rawImg
	 * @return
	 */
	private List<Mat> splitBGR(Mat rawImg) {
		List<Mat> bgrPlanes = new ArrayList<>();
		Core.split(rawImg, bgrPlanes);

		return bgrPlanes;
	}

	/**
	 * 분할된 히스토그램 채널을 이용해
	 * 단일 채널(0번)을 이용해 히스토그램을 계산한다
	 * 추출된 히스토그램의 범위는 실제 사람이 보는 색의 범위로
	 * rgb값의 범위인 0 ~ 255로 한다
	 * 
	 * @param bgrPlanes
	 * @param histogram
	 */
	private void getHistogram(List<Mat> bgrPlanes, Mat histogram) {
		Imgproc.calcHist(bgrPlanes, channel, mask, histogram, histSize, histRange, accumulate);
	}
}
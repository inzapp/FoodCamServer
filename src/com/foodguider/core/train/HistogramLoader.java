package com.foodguider.core.train;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import com.foodguider.util.pRes;

class HistogramLoader implements DataLoader {
	private Mat mask;
	private MatOfInt channel;
	private MatOfInt histSize;
	private MatOfFloat histRange;
	private boolean accumulate;
	
	public HistogramLoader() {
		mask = new Mat();
		channel = new MatOfInt(0);
		histSize = new MatOfInt(256);
		histRange = new MatOfFloat(0f, 255f);
		accumulate = false;
	}
	
	@Override
	public Mat load(Mat rawImg) {
		
		List<Mat> bgrPlanes = splitBGR(rawImg);
		Mat histogram = new Mat();
		
		getHistogram(bgrPlanes, histogram);

		if(histogram.empty()) {
			pRes.log("Histogram get failure");
			return null;
		}
		
		return histogram;
	}
	
	private List<Mat> splitBGR(Mat rawImg) {
		List<Mat> bgrPlanes = new ArrayList<>();
		Core.split(rawImg, bgrPlanes);
		
		return bgrPlanes;
	}
	
	private void getHistogram(List<Mat> bgrPlanes, Mat histogram) {
		Imgproc.calcHist(bgrPlanes, channel, mask, histogram, histSize, histRange, accumulate);
	}
}

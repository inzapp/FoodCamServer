package com.foodguider.core.train;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * 이미지를 단일행으로 벡터화 해 로드하는 클래스
 * knn train에 사용된다
 * @author root
 *
 */
class KnnFeatureLoader implements DataLoader {
	private final int WIDTH = 500;
	private final int HEIGHT = 500;
	private final int blurVal = 4;

	@Override
	public Mat load(Mat rawImg) {
		resize(rawImg);
		
		blur(rawImg);
		
		convertTo32Float(rawImg);
		
		return reshape(rawImg);
	}
	
	private void resize(Mat rawImg) {
		if(rawImg.width() != WIDTH || rawImg.height() != HEIGHT) 
			Imgproc.resize(rawImg, rawImg, new Size(WIDTH, HEIGHT));
	}
	
	private void blur(Mat rawImg) {
		Imgproc.blur(rawImg, rawImg, new Size(blurVal, blurVal));
	}
	
	private void convertTo32Float(Mat rawImg) {
		rawImg.convertTo(rawImg, CvType.CV_32F);
	}
	
	private Mat reshape(Mat rawImg) {
		try {
			rawImg = rawImg.reshape(1, 1);
			return rawImg;
		} catch(Exception e) {
			System.out.println("reshape failure");
			return null;
		}
	}
}

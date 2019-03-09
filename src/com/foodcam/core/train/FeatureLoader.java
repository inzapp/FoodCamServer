package com.foodcam.core.train;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * 이미지를 단일행으로 벡터화 해 로드하는 클래스로 svm train에 사용된다
 * 
 * @author root
 *
 */
class FeatureLoader implements DataLoader {
	
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

	/**
	 * svm train을 위한 이미지 벡터화에 대비해
	 * 만약 지정된 사이즈와 다른 크기의 매트릭스가 들어온 경우
	 * 지정된 크기에 맞게 재조정 된다
	 * 
	 * @param rawImg
	 */
	private void resize(Mat rawImg) {
		if (rawImg.width() != WIDTH || rawImg.height() != HEIGHT)
			Imgproc.resize(rawImg, rawImg, new Size(WIDTH, HEIGHT));
	}

	/**
	 * 보정처리가 되지 않은 사진의 경우 edge가 너무 많이 검출되어
	 * 매트릭스 비교 시 많은 연산을 낭비하게 되고 정확도 또한 떨어지게 된다
	 * 이를 방지하기 위해 이미지를 약간 흐리게 하는 과정으로
	 * edge검출횟수를 줄여 연산 수를 줄일 수 있으며 정확도 또한 높일 수 있게 된다
	 * 
	 * @param rawImg
	 */
	private void blur(Mat rawImg) {
		Imgproc.blur(rawImg, rawImg, new Size(blurVal, blurVal));
	}

	/**
	 * svm train을 위한 변환과정으로 raw Mat을 32bit Float로 변환한다
	 * 
	 * @param rawImg
	 */
	private void convertTo32Float(Mat rawImg) {
		rawImg.convertTo(rawImg, CvType.CV_32F);
	}

	/**
	 * 다수행 매트릭스를 단일 행으로 변환해
	 * 하나의 매트릭스에 push_back 함으로서
	 * svm 훈련에 필요한 데이터를 만들 수 있게 된다
	 * 
	 * @param rawImg
	 * @return
	 */
	private Mat reshape(Mat rawImg) {
		try {
			rawImg = rawImg.reshape(1, 1);
			return rawImg;
		} catch (Exception e) {
			System.out.println("reshape failure");
			return null;
		}
	}
}

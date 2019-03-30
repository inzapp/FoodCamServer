package com.foodcam.core.train;

import org.opencv.core.Mat;

/**
 * 이미지 훈련에 필요한 데이터를 로드하는데 사용되는 Loader 클래스의 기반 인터페이스
 * 
 * @author root
 *
 */
interface DataLoader {

	public Mat load(Mat rawImg);
}

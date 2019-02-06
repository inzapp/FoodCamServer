package com.foodguider.core.train;

import org.opencv.core.Mat;

/**
 * KNN, Descriptor, Histogram을 로드하는데 사용되는
 * Loader 클래스의 기반 인터페이스
 * @author root
 *
 */
interface DataLoader {
	
	public Mat load(Mat rawImg);
}

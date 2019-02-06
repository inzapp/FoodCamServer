package com.foodguider.core.train;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.ORB;

import com.foodguider.util.pRes;

/**
 * 이미지의 키포인트를 추출해 디스크립터를 로드하는 클래스
 * @author root
 *
 */
class DescriptorLoader implements DataLoader {
	private Mat rawImg;
	private FastFeatureDetector fdd;
	private ORB orb;
	private MatOfKeyPoint matOfKeyPoint;
	private Mat descriptor;
	private int detectMethod;
	
	public DescriptorLoader() {
		detectMethod = FastFeatureDetector.TYPE_5_8;
		fdd = FastFeatureDetector.create(detectMethod);
		orb = ORB.create();
		matOfKeyPoint = new MatOfKeyPoint();
		descriptor = new Mat();
	}
	
	@Override
	public Mat load(Mat rawImg) {
		this.rawImg = rawImg;
		
		getKeyPoint();
		
		getDescriptor();
		
		if(descriptor.empty()) {
			pRes.log("Empty descriptor");
			return null;
		}
		
		return descriptor;
	}
	
	private void getKeyPoint() {
		fdd.detect(rawImg, matOfKeyPoint);
		ORB.create(ORB.FAST_SCORE).detect(rawImg, matOfKeyPoint);
	}
	
	private void getDescriptor() {
		orb.compute(rawImg, matOfKeyPoint, descriptor);
	}
}

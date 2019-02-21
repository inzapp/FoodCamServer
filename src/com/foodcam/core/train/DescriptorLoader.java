package com.foodcam.core.train;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.ORB;

import com.foodcam.util.pRes;

/**
 * 이미지의 키포인트를 추출해 디스크립터를 로드하는 클래스 descriptor matching에 사용된다
 * 
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
		orb = ORB.create(10000000);
		matOfKeyPoint = new MatOfKeyPoint();
		descriptor = new Mat();
	}

	/**
	 * 이미지 매트릭스를 인자로 받아 descriptor를 추출해 리턴한다
	 */
	@Override
	public Mat load(Mat rawImg) {
		this.rawImg = rawImg;

		getKeyPoint();

		getDescriptor();

		if (descriptor.empty()) {
			pRes.log("Empty descriptor");
			return null;
		}

		return descriptor;
	}

	/**
	 * 이미지가 가지고 있는 특징점(키포인트)를 추출한다
	 * 추출에는 비교적 속도가 빠른 ORB가 사용된다
	 * FastFeatureDetector를 이용하게 되면 더 정확한 특징점을 추출할 수 있으나
	 * 추출시간이 오래걸려 서버 초기화에 많은 부하가 발생할 수 있다
	 * 또한 해당 프로젝트에서 descriptor는 순위로서 추출된 것들 중
	 * 가장 비슷하지 않은것을 골라내어 제거하는 용도로 사용되기 때문에
	 * 사실상 매우 정확할 필요는 없다
	 */
	private void getKeyPoint() {
//		fdd.detect(rawImg, matOfKeyPoint);
		orb.detect(rawImg, matOfKeyPoint);
	}

	/**
	 * ORB를 이용해 추출된 키포인트를 기반으로 한 descriptor를 로드한다
	 */
	private void getDescriptor() {
		orb.compute(rawImg, matOfKeyPoint, descriptor);
	}
}

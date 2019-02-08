package com.foodcam.test;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 이미지 디스크립터에 대한 테스트 함수들
 * @author root
 *
 */
public final class DescriptorTester implements Tester{
	@Override
	public void test() {
		
	}
	
	
	// tmp

    private static void featureView(String imgPath) {
    	Mat img = Imgcodecs.imread(imgPath, Imgcodecs.IMREAD_ANYCOLOR);
    	Imgproc.blur(img, img, new Size(4, 4));

//    	FastFeatureDetector ffd = FastFeatureDetector.create(0);
    	FastFeatureDetector ffd = FastFeatureDetector.create(0, false, 0);
//    	ORB ffd = ORB.create(1000000);
    	MatOfKeyPoint matOfKeyPoint = new MatOfKeyPoint();
    	ffd.detect(img, matOfKeyPoint);

    	Features2d.drawKeypoints(img, matOfKeyPoint, img);
    	HighGui.imshow("img", img);
    	HighGui.waitKey(0);
    }
    
    private static void arrView(String imgPath) {
    	Mat img = Imgcodecs.imread(imgPath);
    	
    	int blurVal = 4;
    	Imgproc.blur(img, img, new Size(blurVal, blurVal));

//    	ORB orb = ORB.create(1000000);
    	ORB orb = ORB.create(1000000, blurVal, blurVal, blurVal, blurVal, blurVal, blurVal, blurVal, blurVal);
    	Mat descriptor = new Mat();
    	MatOfKeyPoint matOfKeyPoint = new MatOfKeyPoint();
    	orb.detectAndCompute(img, new Mat(), matOfKeyPoint, descriptor);
    	
    	System.out.println(descriptor.dump());
    }
}

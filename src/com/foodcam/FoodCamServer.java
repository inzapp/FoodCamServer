package com.foodcam;

import com.foodcam.test.KnnAccuracyTester;

/**
 * Main 클래스
 * 
 * @author root
 */
public final class FoodCamServer extends ServerInitializer {
	public static void main(String[] args) {
//		FoodCamServer foodCamServer = new FoodCamServer();
//		foodCamServer.activate();
		
		KnnAccuracyTester tester = new KnnAccuracyTester();
		tester.test();
	}
}

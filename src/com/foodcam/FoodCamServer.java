package com.foodcam;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.foodcam.test.HistogramViewer;
import com.foodcam.test.SVMAccuracyTester;
import com.foodcam.test.Tester;

/**
 * Entry 클래스
 * 
 * @author root
 */
public final class FoodCamServer extends ServerInitializer {

	private int input;
	private BufferedReader br;
	private Tester tester = null;
	private HistogramViewer histogramViewer = new HistogramViewer();
	private SVMAccuracyTester svmAccuracyTester = new SVMAccuracyTester();

	public static void main(String[] args) {
		FoodCamServer server = new FoodCamServer();
		server.br = new BufferedReader(new InputStreamReader(System.in));
		server.mainIO();
	}

	private void mainIO() {
		while (true) {
			System.out.println("1. 서버 실행");
			System.out.println("2. 테스트");
			System.out.println("\n해당 번호를 입력하세요 : ");
			try {
				input = Integer.parseInt(br.readLine());
			} catch (Exception e) {
				continue;
			}

			cls();
			switch (input) {
			case 1:
				activate();
				break;

			case 2:
				testIO();
				break;

			default:
				break;
			}
		}
	}

	private void testIO() {
		System.out.println("1. Histogram 테스트");
		System.out.println("2. SVM 테스트");
		System.out.println("\n해당 번호를 입력하세요 : ");
		try {
			input = Integer.parseInt(br.readLine());
		} catch (Exception e) {
			return;
		}
		switch (input) {
		case 1:
			tester = histogramViewer;
			break;

		case 2:
			tester = svmAccuracyTester;
			break;

		default:
			return;
		}

		tester.test();
	}

	private void cls() {
		try {
			final String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
		}
	}
}

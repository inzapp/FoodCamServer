package com.foodcam;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.opencv.core.Core;

import com.foodcam.core.Predictor;
import com.foodcam.net.Gate;
import com.foodcam.util.pRes;

/**
 * 서버의 초기화를 담당하는 클래스
 * 서버 접속을 위한 소켓과 스레드풀을 초기화하고
 * 로컬에 저장된 이미지 DB를 Pre-Processing 한다
 * @author root
 *
 */
class ServerInitializer {
    static {
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

	public void activate() {
		if (!initServer())
			return;

		addShutdownHook();

		preProcess();

		acceptClient();
	}

	private boolean initServer() {
		try {
			pRes.serverSocket = new ServerSocket(pRes.PORT);
			pRes.serverThreadPool = Executors.newFixedThreadPool(pRes.MAX_THREAD_COUNT);
			pRes.threadCountAlerter = (ThreadPoolExecutor) pRes.serverThreadPool;

			return true;
		} catch (Exception e) {
			pRes.log("서버 초기화 실패 : 포트가 이미 사용중일 수 있습니다");
			return false;
		}
	}

	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				pRes.serverThreadPool.shutdownNow();
			} catch (Exception e) {
			}
			pRes.log("서버 셧다운 훅 성공");
		}));
	}

	private void preProcess() {
		Predictor.getInstance();
	}

	private void acceptClient() {
		pRes.log("서버 초기화 성공 : 클라이언트의 접속을 대기합니다");

		while (true) {
			try {
				Socket clientSocket = pRes.serverSocket.accept();
				pRes.serverThreadPool.execute(new Gate(clientSocket));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

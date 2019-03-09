package com.foodcam;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.opencv.core.Core;

import com.foodcam.core.Predictor;
import com.foodcam.core.train.DataSetLoader;
import com.foodcam.domain.DataSet;
import com.foodcam.net.Gate;
import com.foodcam.util.pRes;

/**
 * 서버의 초기화를 담당하는 클래스 서버 접속을 위한 소켓과 스레드풀을 초기화하고 로컬에 저장된 이미지 DB를 Pre-Processing 한다
 * 
 * @author root
 *
 */
class ServerInitializer {
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * 서버를 초기화한다
	 * 1. 서버 소켓 개방을 위한 포트가 이미 사용중인 경우 중단된다
	 * 2. 서버 종료 시 스레드풀의 종료를 위한 종료 훅을 할당한다
	 * 3. 클라이언트의 접속대기를 시작한다
	 */
	public void activate() {
		
		if (!initServer())
			return;

		addShutdownHook();

		preProcess();

		acceptClient();
	}

	/**
	 * 클라이언트 접속을 위한 서버소켓을 개방한다
	 * 이 때 포트가 사용중이라면 실패를 리턴한다
	 * 서버 소켓 개방 이후 클라이언트 병렬 처리를 위한 스레드풀 초기화와
	 * 현재 서버의 부하상태를 알기 위한 스레드 갯수를 알려주는 threadCountAlerter를 초기화한다
	 * 
	 * @return
	 */
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

	/**
	 * 서버 종료 시 스레드풀의 종료를 위한 종료 훅을 스레드로서 할당한다
	 */
	private void addShutdownHook() {
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				pRes.serverThreadPool.shutdownNow();
			} catch (Exception e) {
			}
			pRes.log("서버 셧다운 훅 성공");
		}));
	}

	/**
	 * 서버 초기화 시 저장된 이미지 DB를 모두 로드해 학습을 한다
	 * 가장 많이 시간이 걸리는 부분을 한번만 수행해 stack에 보관함으로서
	 * 이미지 예측에 대한 시간을 대폭 줄어들게 한다
	 */
	private void preProcess() {
		
		DataSetLoader trainDataSetLoader = new DataSetLoader();
		DataSet allTrainDataSet = trainDataSetLoader.getTrainDataSet(DataSetLoader.ALL);
		
		Predictor.getInstance().train(allTrainDataSet);
	}

	/**
	 * 개방한 서버 소켓으로부터 클라이언트의 접속을 대기한다
	 * 해당 프로젝트에서는 블로킹 소켓을 사용해 클라이언트 처리동안
	 * 다른 클라이언트의 접속에 영향을 미칠 수 있으므로
	 * 접속과 동시에 해당 클라이언트는 스레드로 넘겨지게 되고
	 * 바로 새로운 클라이언트의 접속을 대기하게 된다
	 */
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

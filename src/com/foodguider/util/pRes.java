package com.foodguider.util;

import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 프로젝트 내부에서 공유자원으로서 사용될 변수들 서버소켓, 포트번호, 스레드풀, 이미지 리스트, 음식 이름리스트 등이 있다
 * @author root
 */
public abstract class pRes {
	public static final int PORT = 12332;
	public static final int MAX_THREAD_COUNT = 100;
	public static ServerSocket serverSocket;
	public static ExecutorService serverThreadPool;
	public static ThreadPoolExecutor threadCountAlerter;
	public static final String TRAIN_IMAGE_PATH = "image/food";

	public static void log(String msg) {
		String curTime = new SimpleDateFormat("[yy.MM.dd kk:mm:ss] ").format(new Date());
		System.out.println(curTime + msg);
	}
}
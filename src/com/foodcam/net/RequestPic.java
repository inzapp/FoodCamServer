package com.foodcam.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.foodcam.core.Predictor;
import com.foodcam.util.pRes;

/**
 * Gate로부터 Key값 대조와 트래픽검사를 모두 통과해 정상접속을 한 클라이언트를 처리
 * 클라이언트 동시접속을 대비해 스레드를 이용해 병렬처리
 * 
 * 1. 클라이언트로부터 이미지를 전송받음 
 * 2. 전송받은 이미지가 서버에 있는 이미지 중 어떤 음식과 가장 유사한지 추출 
 * 3. 가장 훈련된 이미지와 가장 유사한 이미지들을 추출해 순위별로 JSON 형태로 클라이언트에게 전송
 * 
 * @author root
 *
 */
public final class RequestPic implements Runnable {
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public RequestPic(ObjectOutputStream oos, ObjectInputStream ois) {
		
		this.oos = oos;
		this.ois = ois;
	}

	/**
	 * 클라이언트로부터 이미지를 전송받아 해당 이미지가 어떤 이미지인지 유추한 후
	 * 결과를 클라이언트에게 전송한다
	 */
	@Override
	public void run() {
		
		Mat receivedImg = receiveImgFromClient();
		if (receivedImg == null) {
			disconnect();
			return;
		}

		JSONObject resultJson = Predictor.getInstance().predict(receivedImg);
		sendResult(resultJson);
	}

	/**
	 * 접속한 클라이언트로부터 요청 이미지를 수신
	 * 직렬화를 위해 byte 배열 형태로 수신받는다
	 * 동등한 조건에서의 비교를 위해 수신된 이미지는 500, 500으로 자동 조정된다
	 * 
	 * @return
	 */
	private Mat receiveImgFromClient() {
		
		byte[] byteArray = null;
		try {
			byteArray = (byte[]) ois.readObject();
		} catch (Exception e) {
			pRes.log("Failed to receive image from client");
			disconnect();
			e.printStackTrace();
			return null;
		}

		MatOfByte convertedByteArray = new MatOfByte(byteArray);
		Mat receivedImg = Imgcodecs.imdecode(convertedByteArray, Imgcodecs.IMREAD_ANYCOLOR);
		Imgproc.resize(receivedImg, receivedImg, new Size(500, 500));

		return receivedImg;
	}

	/**
	 * 추출된 결과를 클라이언트에게 전송
	 * 
	 * @param json
	 */
	private void sendResult(JSONObject json) {
		
		try {
			oos.writeObject(json.toString());
		} catch (Exception e) {
			pRes.log("Result send failure");
			disconnect();
			e.printStackTrace();
		}
	}

	/**
	 * OutputStream을 닫음으로써 클라이언트와 연결 종료
	 */
	private void disconnect() {
		
		try {
			oos.close();
		} catch (Exception ignored) {
		}
	}
}
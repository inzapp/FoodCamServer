package com.foodguider.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.foodguider.core.Predictor;
import com.foodguider.util.pRes;

/**
 * Gate로부터 Key값 대조와 트래픽검사를 모두 통과해 정상접속을 한 클라이언트를 처리
 * 
 * 1. 클라이언트로부터 이미지를 전송받음
 * 2. 전송받은 이미지를 image/temp 경로에 저장 ( 이 때 이미지의 사이즈는 500 * 500으로 재 조정 )
 * 3. image/temp에 저장된 이미지가 서버에 있는 이미지 중 어떤 음식과 가장 유사한지 추출
 * 4. 가장 비슷한 이미지의 링크를 JSON 형태로 클라이언트에게 전송
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

    @Override
    public void run() {
    	
    	Mat receivedImg = receiveImgFromClient();
    	if(receivedImg == null) {
    		disconnect();
    		return;
    	}
    	
    	String imgPath = saveImgToTempDir(receivedImg);

        JSONObject resultJson = Predictor.getInstance().predict(imgPath);
        
        sendResult(resultJson);
    }
    
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

        // 이미지 리사이징
        Imgproc.resize(receivedImg, receivedImg, new Size(500, 500));
        
        return receivedImg;
    }

    // image/temp에 임시로 이미지 저장
    private String saveImgToTempDir(Mat img) {
        String imgPath = "image/temp/" + System.currentTimeMillis() + ".jpg";
        Imgcodecs.imwrite(imgPath, img);
        
        return imgPath;
    }
    
    private void sendResult(JSONObject json) {
    	try {
			oos.writeObject(json.toString());
		} catch (Exception e) {
			pRes.log("Result send failure");
			disconnect();
			e.printStackTrace();
		}
    }

    // 연결 종료
    private void disconnect() {
        try {
            oos.close();
        } catch (Exception ignored) {
        }
    }
}
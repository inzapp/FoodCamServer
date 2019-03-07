package com.foodcam.core;

import org.json.JSONObject;
import org.opencv.core.Mat;

public abstract class PredictorOperator extends PredictorInitializer {

	/**
	 * 이미지의 경로를 인자로 받아 해당 이미지가 어떤 이미지인지 예측해 해당 이미지를 소개하는 링크를 Json 형태로 리턴
	 * 
	 * @param imgPath
	 * @return
	 */
	abstract JSONObject predict(Mat receivedImg);
}

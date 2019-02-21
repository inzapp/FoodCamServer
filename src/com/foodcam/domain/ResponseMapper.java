package com.foodcam.domain;

import java.util.HashMap;

/**
 * 해당 이미지가 어떤 디렉토리에서 왔는지(어떤 음식인지) 를 구분하기 위한 매핑 클래스 매핑을 위한 해쉬맵을 포함하고 있다
 * 
 * @author root
 *
 */
public class ResponseMapper {
	private HashMap<Integer, String> responseMap;

	public ResponseMapper() {
		this.responseMap = new HashMap<Integer, String>();
	}

	public void mapResponse(int response, String featureName) {
		responseMap.put(response, featureName);
	}

	public HashMap<Integer, String> getResponseMap() {
		return responseMap;
	}
}

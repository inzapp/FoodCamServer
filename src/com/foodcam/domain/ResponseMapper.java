package com.foodcam.domain;

import java.util.HashMap;

/**
 * 해당 이미지가 어떤 디렉토리에서 왔는지(어떤 음식인지) 를 구분하기 위한 매핑 클래스 매핑을 위한 해쉬맵을 포함하고 있다
 * 
 * responseMap
 * 정수형 키값을 인자로 해당 값에 대응하는 음식 이름을 리턴한다
 * 정수의 값은 훈련 이미지 데이터베이스의 디렉토리 인덱스로 저장되며
 * DataSetLoader에서 초기화된다
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

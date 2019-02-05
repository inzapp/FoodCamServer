package com.foodguider.domain;

import java.util.HashMap;

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

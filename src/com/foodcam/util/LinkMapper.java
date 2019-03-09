package com.foodcam.util;

import java.util.HashMap;

/**
 * 추출된 디렉토리 이름(음식 이름)을 이용해 해당 음식을 소개하는 웹 페이지에 대한 링크를 추출하는 매퍼 클래스
 * 
 * @author root
 *
 */
public class LinkMapper {

	private HashMap<String, String> linkMap;

	public LinkMapper() {
		
		linkMap = new HashMap<>();

		linkMap.put("bibimbab", "http://allrecipes.kr/recipe/868/-----.aspx");
		linkMap.put("bibimnangmyeon", "http://www.10000recipe.com/recipe/6850942");
		linkMap.put("ddukboki", "https://m.blog.naver.com/forgod2026/90193715054");
		linkMap.put("dongas", "http://nemos.tistory.com/208");
		linkMap.put("dwanjangjjigae", "https://m.blog.naver.com/hjh1596/220073273213");
		linkMap.put("friedchicken", "http://minorious.tistory.com/177");
		linkMap.put("gimbab", "http://allrecipes.kr/recipe/510/------------.aspx");
		linkMap.put("gimchijjigae", "http://amyzzung.tistory.com/162");
		linkMap.put("jajangmyeon", "http://bmsj.tistory.com/169");
		linkMap.put("jjambbong", "http://bmsj.tistory.com/170");
		linkMap.put("mandoo", "http://amyzzung.tistory.com/933");
		linkMap.put("moolnangmyeon", "http://www.10000recipe.com/recipe/4061208");
		linkMap.put("pizza", "http://amyzzung.tistory.com/471");
		linkMap.put("ramen", "http://milkywaystory.tistory.com/101");
		linkMap.put("seasonedchicken", "http://www.10000recipe.com/recipe/6835696");

		pRes.log("링크 매핑 완료");
	}

	public HashMap<String, String> getLinkMap() {
		
		return linkMap;
	}
}

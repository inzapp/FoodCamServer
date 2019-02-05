package com.foodguider.util;

import java.util.HashMap;
import java.util.Map;

public class LinkMapper {

	private Map<String, String> linkMap; // linkMap 아직 안써서 밑줄쳐져있음
	
	private LinkMapper() {
		linkMap = new HashMap<>();

		addMapping("bibimbab", "http://allrecipes.kr/recipe/868/-----.aspx");
		addMapping("bibimnangmyeon", "http://www.10000recipe.com/recipe/6850942");
		addMapping("ddukboki", "https://m.blog.naver.com/forgod2026/90193715054");
		addMapping("dongas", "http://nemos.tistory.com/208");
		addMapping("dwanjangjjigae", "https://m.blog.naver.com/hjh1596/220073273213");
		addMapping("friedchicken", "http://minorious.tistory.com/177");
		addMapping("gimbab", "http://allrecipes.kr/recipe/510/------------.aspx");
		addMapping("gimchijjigae", "http://amyzzung.tistory.com/162");
		addMapping("jajangmyeon", "http://bmsj.tistory.com/169");
		addMapping("jjambbong", "http://bmsj.tistory.com/170");
		addMapping("mandoo", "http://amyzzung.tistory.com/933");
		addMapping("moolnangmyeon", "http://www.10000recipe.com/recipe/4061208");
		addMapping("pizza", "http://amyzzung.tistory.com/471");
		addMapping("ramen", "http://milkywaystory.tistory.com/101");
		addMapping("seasonedchicken", "http://www.10000recipe.com/recipe/6835696");

		pRes.log("링크 매핑 완료");
	}

	public static class Singleton {
		private static LinkMapper INSTANCE = new LinkMapper();
	}

	public static LinkMapper getInstance() {
		return Singleton.INSTANCE;
	}

	public Map<String, String> getLinkMap() {
		return linkMap;
	}

	private void addMapping(String rawFoodName, String link) {
		linkMap.put(rawFoodName, link);
	}
}

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

		linkMap.put("bibimbob", "http://allrecipes.kr/recipe/868/-----.aspx");
		linkMap.put("boolgogi", "https://m.blog.naver.com/ju_need/220479101208");
		linkMap.put("ddukgook", "http://www.10000recipe.com/recipe/6842725");
		linkMap.put("dwanjangjjigae", "https://m.blog.naver.com/hjh1596/220073273213");
		linkMap.put("gimbab", "http://allrecipes.kr/recipe/510/------------.aspx");
		linkMap.put("gimchijjigae", "http://amyzzung.tistory.com/162");
		linkMap.put("samgaetang", "https://m.post.naver.com/viewer/postView.nhn?volumeNo=15918967&memberNo=916314");
		linkMap.put("yulmoogimchi", "https://m.blog.naver.com/PostView.nhn?blogId=heranzeus&logNo=221068696877&proxyReferer=https%3A%2F%2Fwww.google.com%2F");

		pRes.log("링크 매핑 완료");
	}

	public HashMap<String, String> getLinkMap() {
		
		return linkMap;
	}
}

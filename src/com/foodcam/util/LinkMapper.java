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

		linkMap.put("bibimbob", "http://www.10000recipe.com/recipe/2795635");
		linkMap.put("boolgogi", "https://m.blog.naver.com/ju_need/220479101208");
		linkMap.put("ddukgook", "http://ic-eeecs.org/test/foodlist/ddukguk.html");
		linkMap.put("dwanjangjjigae", "http://ic-eeecs.org/test/foodlist/doenjang.html");
		linkMap.put("gimbab", "http://ic-eeecs.org/test/foodlist/gimbab.html");
		linkMap.put("gimchijjigae", "http://amyzzung.tistory.com/162");
		linkMap.put("samgaetang", "http://ic-eeecs.org/test/foodlist/samgyetang.html");
		linkMap.put("yulmoogimchi", "http://ic-eeecs.org/test/foodlist/yeolmu.html");

		pRes.log("링크 매핑을 완료했습니다.");
	}

	public HashMap<String, String> getLinkMap() {
		
		return linkMap;
	}
}

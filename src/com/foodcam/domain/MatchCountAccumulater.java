package com.foodcam.domain;

/**
 * SVM 테스트 시 각 음식별 매칭률을 계산하기 위한 횟수 누적기
 * 
 * totalFeatureCount
 * 총 테스트 데이터의 갯수로서 적중률을 추출하기 위한 분모의 역할을 한다
 * 
 * matchCount
 * 테스트 데이터 중 적중 데이터의 갯수로서 적중률을 추출하기 위한 분자의 역할을 한다 
 * 
 * @author root
 *
 */
public class MatchCountAccumulater {
	
	private float totalFeatureCount = 0;
	private float matchCount = 0;

	public float getTotalFeatureCount() {
		return totalFeatureCount;
	}

	public void addTotalFeatureCount() {
		this.totalFeatureCount++;
	}

	public float getMatchCount() {
		return matchCount;
	}

	public void addMatchCount() {
		this.matchCount++;
	}

	public float getMatchingRate() {
		return (this.matchCount / this.totalFeatureCount) * 100.0f;
	}
}
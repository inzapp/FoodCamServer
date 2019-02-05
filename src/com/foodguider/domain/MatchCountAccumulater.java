package com.foodguider.domain;

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
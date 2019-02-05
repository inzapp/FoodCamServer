package com.foodguider;

/**
 * Main 클래스
 * @author root
 */
public final class FoodGuider extends ServerInitializer{
    public static void main(String[] args) {
        FoodGuider foodMatcher = new FoodGuider();
        foodMatcher.activate();
    }
}

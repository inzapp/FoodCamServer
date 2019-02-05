package com.foodguider.core.train;

import org.opencv.core.Mat;

interface DataLoader {
	
	public Mat load(Mat rawImg);
}

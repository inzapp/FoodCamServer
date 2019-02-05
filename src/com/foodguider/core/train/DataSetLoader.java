package com.foodguider.core.train;

import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.foodguider.domain.DataSet;
import com.foodguider.domain.ResponseMapper;
import com.foodguider.util.pRes;

public final class DataSetLoader {
	public static final int ALL = -1;
	public static final int HALF_TRAIN = 0;
	public static final int HALF_TEST = 1;

	private DataLoader knnFeatureLoader = new KnnFeatureLoader();
	private DataLoader descriptorLoader = new DescriptorLoader();
	private DataLoader histogramLoader = new HistogramLoader();

	private Mat trainFeatureVector = new Mat();
	private ArrayList<Integer> trainLabelList = new ArrayList<>();
	private ResponseMapper responseMapper = new ResponseMapper();

	private ArrayList<Mat> descriptorList = new ArrayList<>();
	private ArrayList<Mat> histogramList = new ArrayList<>();

	public DataSet getTrainDataSet(int requestType) {
		pRes.log("Start loading train data set...");

		File rootDir = new File(pRes.TRAIN_IMAGE_PATH);
		if (!rootDir.exists()) {
			pRes.log("Root dir found failure");
			return null;
		}

		File[] dirArr = rootDir.listFiles();
		for (int i = 0; i < dirArr.length; i++) {
			File curDir = dirArr[i];
			if (!curDir.isDirectory()) {
				continue;
			}

			File[] fileArr = curDir.listFiles();
			for (int j = 0; j < fileArr.length; j++) {
				File curFile = fileArr[j];
				if (!curFile.isFile()) {
					continue;
				}

				if (requestType != ALL) {
					if (j % 2 == requestType) {
						continue;
					}
				}

				Mat img = Imgcodecs.imread(curFile.getAbsolutePath());
				if (img.empty()) {
					continue;
				}

				Mat feature = knnFeatureLoader.load(img);
//				Mat descriptor = descriptorLoader.load(img);
//				Mat histogram = histogramLoader.load(img);

				try {
					trainFeatureVector.push_back(feature);
					trainLabelList.add(i);
					responseMapper.mapResponse(i, curDir.getName());

//					descriptorList.add(descriptor);
//					histogramList.add(histogram);
				} catch (Exception e) {
					pRes.log("[Train failure] - " + curFile.getAbsolutePath());
					return null;
				}
			}
		}

		DataSet trainDataSet = new DataSet();
		trainDataSet.setFeatureVector(trainFeatureVector);
		trainDataSet.setFeatureLabelList(trainLabelList);
		trainDataSet.setResponseMapper(responseMapper);

		trainDataSet.setDescriptorList(descriptorList);
		trainDataSet.setHistogramList(histogramList);

		System.out.println("Success to load train data set");
		return trainDataSet;
	}

	public DataSet getRequestDataSet(String imgPath) {
		Mat rawImg = Imgcodecs.imread(imgPath, Imgcodecs.IMREAD_ANYCOLOR);
		if (rawImg.empty()) {
			pRes.log("Request image not found");
			return null;
		}

		Mat feature = knnFeatureLoader.load(rawImg);
		Mat descriptor = descriptorLoader.load(rawImg);
		Mat histogram = histogramLoader.load(rawImg);

		try {
			trainFeatureVector.push_back(feature);
		} catch (Exception e) {
			pRes.log("[Request data set load failure] - " + imgPath);
			return null;
		}
		
		descriptorList.add(descriptor);
		histogramList.add(histogram);
		
		
		DataSet requestDataSet = new DataSet();
		requestDataSet.setFeatureVector(trainFeatureVector);
		requestDataSet.setDescriptorList(descriptorList);
		requestDataSet.setHistogramList(histogramList);
		
		return requestDataSet;
	}
}

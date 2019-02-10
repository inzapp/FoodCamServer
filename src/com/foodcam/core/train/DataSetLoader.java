package com.foodcam.core.train;

import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.foodcam.domain.DataSet;
import com.foodcam.domain.ResponseMapper;
import com.foodcam.util.pRes;

/**
 * 이미지 훈련에 필요한 데이터들을 묶어
 * com.foodguider.domain.DataSet 형태로 리턴
 * 
 * ALL : 서버 운영에 사용되는 모든 데이터 로드
 * 
 * 아래는 인식률 테스트를 위해 사용된다
 * HALF_TRAIN : 이미지 DB중 반만 로드 -> 훈련 데이터로 사용
 * HALF_TEST : HALF_TRAIN에서 로드하지 않는 나머지 반의 이미지 로드 -> 테스트 데이터로 사용
 * @author root
 *
 */
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

	/**
	 * 단일 이미지에 대한 DataSet을 로드 -> 이미지 요청 시 사용
	 * @param imgPath
	 * @return
	 */
	public DataSet getRequestDataSet(Mat receivedImg) {
//		Mat rawImg = Imgcodecs.imread(imgPath, Imgcodecs.IMREAD_ANYCOLOR);
//		if (rawImg.empty()) {
//			pRes.log("Request image not found");
//			return null;
//		}

		Mat feature = knnFeatureLoader.load(receivedImg);
		Mat descriptor = descriptorLoader.load(receivedImg);
		Mat histogram = histogramLoader.load(receivedImg);

		try {
			trainFeatureVector.push_back(feature);
		} catch (Exception e) {
			pRes.log("[Request data set load failure] - ");
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

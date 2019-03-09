package com.foodcam.core.train;

import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.foodcam.domain.DataSet;
import com.foodcam.domain.ResponseMapper;
import com.foodcam.util.pRes;

/**
 * 이미지 훈련에 필요한 데이터들을 묶어 com.foodguider.domain.DataSet 형태로 리턴
 * 
 * ALL : 서버 운영에 사용되는 모든 데이터 로드
 * 
 * 아래는 인식률 테스트를 위해 사용된다 HALF_TRAIN : 이미지 DB중 반만 로드 -> 훈련 데이터로 사용 HALF_TEST :
 * HALF_TRAIN에서 로드하지 않는 나머지 반의 이미지 로드 -> 테스트 데이터로 사용
 * 
 * @author root
 *
 */
public final class DataSetLoader {
	
	public static final int ALL = -1;
	public static final int HALF_TRAIN = 0;
	public static final int HALF_TEST = 1;

	private DataLoader svmFeatureLoader = new FeatureLoader();

	public DataSet getTrainDataSet(int requestType) {
		
		pRes.log("훈련 데이터셋 로딩을 시작합니다.");
		
		Mat trainFeatureVector = new Mat();
		ArrayList<Integer> trainLabelList = new ArrayList<>();
		ResponseMapper responseMapper = new ResponseMapper();

		File trainDataDir = new File(pRes.TRAIN_DATA_PATH);
		if (!trainDataDir.exists()) {
			pRes.log("trainData 디렉토리를 찾을 수 없습니다");
			return null;
		}

		File[] dirArr = trainDataDir.listFiles();
		for (int i = 0; i < dirArr.length; i++) {
			File curDir = dirArr[i];
			if (!curDir.isDirectory())
				continue;

			File[] fileArr = curDir.listFiles();
			for (int j = 0; j < fileArr.length; j++) {
				File curFile = fileArr[j];
				if (!curFile.isFile())
					continue;

				if (requestType == HALF_TRAIN && j % 2 == 1)
					continue;
				else if (requestType == HALF_TEST && j % 2 == 0)
					continue;

				Mat img = Imgcodecs.imread(curFile.getAbsolutePath());
				if (img.empty())
					continue;

				Mat feature = svmFeatureLoader.load(img);

				try {
					trainFeatureVector.push_back(feature);
					trainLabelList.add(i);
					responseMapper.mapResponse(i, curDir.getName());

				} catch (Exception e) {
					pRes.log("[훈련 실패] - " + curFile.getAbsolutePath());
					return null;
				}
			}
		}

		DataSet trainDataSet = new DataSet();
		trainDataSet.setFeatureVector(trainFeatureVector);
		trainDataSet.setFeatureLabelList(trainLabelList);
		trainDataSet.setResponseMapper(responseMapper);

		pRes.log("훈련 데이터셋 로딩을 완료했습니다");
		return trainDataSet;
	}

	/**
	 * 단일 이미지에 대한 DataSet을 로드 -> 이미지 요청 시 사용
	 * 
	 * @param receivedImg
	 * @return
	 */
	public DataSet getRequestDataSet(Mat receivedImg) {
		
		Mat feature = svmFeatureLoader.load(receivedImg);
		Mat trainFeatureVector = new Mat();

		try {
			trainFeatureVector.push_back(feature);
		} catch (Exception e) {
			pRes.log("[요청 데이터셋 로딩 실패] - ");
			return null;
		}

		DataSet requestDataSet = new DataSet();
		requestDataSet.setFeatureVector(trainFeatureVector);

		return requestDataSet;
	}
}

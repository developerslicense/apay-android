package kz.airbapay.apay_android.ui.pages.card_reader.bl;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is not thread safe, make sure that all methods run on the same thread.
 */
class OCR {

	private static FindFourModel findFour = null;
	private static RecognizedDigitsModel recognizedDigitsModel = null;
	List<DetectedBox> digitBoxes = new ArrayList<>();
	boolean hadUnrecoverableException = false;

	private ArrayList<DetectedBox> detectBoxes(Bitmap image) {
		ArrayList<DetectedBox> boxes = new ArrayList<>();
		for (int row = 0; row < findFour.rows; row++) {
			for (int col = 0; col < findFour.cols; col++) {
				if (findFour.hasDigits(row, col)) {
					float confidence = findFour.digitConfidence(row, col);
					CGSize imageSize = new CGSize(image.getWidth(), image.getHeight());
					DetectedBox box = new DetectedBox(row, col, confidence, findFour.rows,
							findFour.cols, findFour.boxSize, findFour.cardSize, imageSize);
					boxes.add(box);
				}
			}
		}
		return boxes;
	}

	private String runModel(Bitmap image) {
		findFour.classifyFrame(image);
		ArrayList<DetectedBox> boxes = detectBoxes(image);
		PostDetectionAlgorithm postDetection = new PostDetectionAlgorithm(boxes, findFour);

		RecognizeNumbers recognizeNumbers = new RecognizeNumbers(image, findFour.rows, findFour.cols);
		ArrayList<ArrayList<DetectedBox>> lines = postDetection.horizontalNumbers();
		String number = recognizeNumbers.number(recognizedDigitsModel, lines);

		if (number == null) {
			ArrayList<ArrayList<DetectedBox>> verticalLines = postDetection.verticalNumbers();
			number = recognizeNumbers.number(recognizedDigitsModel, verticalLines);
			lines.addAll(verticalLines);
		}

		boxes = new ArrayList<>();
		for (ArrayList<DetectedBox> numbers : lines) {
			boxes.addAll(numbers);
		}

		this.digitBoxes = boxes;

		return number;
	}

	synchronized String predict(Bitmap image, Context context) {
		final int NUM_THREADS = 4;
		try {

			if (findFour == null) {
				findFour = new FindFourModel(context);
				findFour.setNumThreads(NUM_THREADS);
			}

			if (recognizedDigitsModel == null) {
				recognizedDigitsModel = new RecognizedDigitsModel(context);
				recognizedDigitsModel.setNumThreads(NUM_THREADS);
			}

			try {
				return runModel(image);
			} catch (Error | Exception e) {
				Log.i("Ocr", "runModel exception, retry prediction", e);
				findFour = new FindFourModel(context);
				recognizedDigitsModel = new RecognizedDigitsModel(context);
				return runModel(image);
			}
		} catch (Error | Exception e) {
			Log.e("Ocr", "unrecoverable exception on Ocr", e);
			hadUnrecoverableException = true;
			return null;
		}
	}
}

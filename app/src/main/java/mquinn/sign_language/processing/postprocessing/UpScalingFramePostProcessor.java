package mquinn.sign_language.processing.postprocessing;

import org.opencv.core.Core;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;

import java.util.Iterator;
import java.util.List;

import mquinn.sign_language.imaging.IFrame;

public class UpScalingFramePostProcessor implements IFramePostProcessor {

    public UpScalingFramePostProcessor() {
    }

    @Override
    public IFrame postProcess(IFrame inputFrame) {

        return upScale(inputFrame);

    }

    private IFrame upScale(IFrame inputFrame){

        iterativeUpScale(inputFrame.getContours());
        iterativeUpScale(inputFrame.getFeatures());

        return inputFrame;
    }

    private void iterativeUpScale(List<MatOfPoint> listOfMatOfPoint){
        if (listOfMatOfPoint.size() > 0){
            Iterator<MatOfPoint> allMatOfPoint = listOfMatOfPoint.iterator();

            while (allMatOfPoint.hasNext()) {
                upScaleMatOfPoint(allMatOfPoint.next());
            }

        }
    }

    private void upScaleMatOfPoint(MatOfPoint inputMatOfPoint){
        Core.multiply(inputMatOfPoint, new Scalar(4, 4), inputMatOfPoint);
    }

}

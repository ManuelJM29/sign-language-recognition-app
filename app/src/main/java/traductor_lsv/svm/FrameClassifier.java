package traductor_lsv.svm;

import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.SVM;

import java.io.File;

import traductor_lsv.imaging.IFrame;
import traductor_lsv.processing.IFrameProcessor;

public class FrameClassifier implements IFrameProcessor {

    private SVM svm;
    private IFrame WF;
    private Mat FF;

    public FrameClassifier(File xmlFile){

        FF = new Mat();
        svm = SVM.create();

        svm.setType(SVM.C_SVC);
        svm.setKernel(SVM.RBF);

        svm.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 100, 1e-6));

        svm = SVM.load(xmlFile.getAbsolutePath());

    }

    @Override
    public IFrame process(IFrame inputFrame) {
        WF = inputFrame;
        classify();

        return WF;
    }

    private void classify(){

        flattenFeatures();
        FF.convertTo(FF, CvType.CV_32F);

        float response = svm.predict(FF);

        LetterClass result = LetterClass.getLetter((int) response);
        WF.setLetterClass(result);

        Log.d("DEBUG", "LETTER CLASS: " + result);
    }

    private void flattenFeatures(){

        FF = WF.getHogDesc();
        FF = FF.reshape(1,1);

    }
}

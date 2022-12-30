package traductor_lsv.imaging;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import traductor_lsv.svm.LetterClass;

public interface IFrame {

    LetterClass getLetterClass();
    void setLetterClass(LetterClass letter);

    Size getOriginalSize();
    void setOriginalSize(Size originalSize);

    Mat getRGBA();
    void setRGBA(Mat inputRGBA);

    List<MatOfPoint> getContours();
    void setCountours(List<MatOfPoint> contours);

    Mat getDownSampledMat();
    void setDownSampledMat(Mat inputDownSampledMat);

    List<MatOfPoint> getFeatures();
    void setFeatures(List<MatOfPoint> features);

    Mat getSkeleton();
    void setSkeleton(Mat skeleton);

    List<MatOfPoint> getCannyEdges();
    void setCannyEdges(List<MatOfPoint> cannyEdges);

    Mat getCannyEdgeMask();
    void setCannyEdgeMask(Mat cannyEdgeMask);

    List<MatOfPoint> getSkeletonContours();
    void setSkeletonContours(List<MatOfPoint> skeletonContours);

    Mat getMaskedImage();
    void setMaskedImage(Mat maskedImage);

    Mat getWindowMask();
    void setWindowMask(Mat maskedImage);

    Mat getHogDesc();
    void setHogDesc(Mat hogDesc);


}
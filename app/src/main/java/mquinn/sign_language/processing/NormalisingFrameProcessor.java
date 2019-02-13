package mquinn.sign_language.processing;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import mquinn.sign_language.imaging.IFrame;

public class NormalisingFrameProcessor implements IFrameProcessor {

    private int target_width = 200;

    @Override
    public IFrame process(IFrame inputFrame) {

        inputFrame.setWindowMask(this.normaliseImageSize(inputFrame.getWindowMask()));

        return inputFrame;

    }

    // resize windowmask to bounds size x
    // draw windowmask onto blank canvas of size x
    private Mat normaliseImageSize(Mat img) {

        int width = img.cols(),
            height = img.rows();

        Mat square = Mat.zeros( target_width, target_width, img.type() );

        int max_dim = ( width >= height ) ? width : height;
        float scale = ( (float) target_width ) / max_dim;
        Rect roi = new Rect();
        if ( width >= height )
        {
            roi.width = target_width;
            roi.x = 0;
            roi.height = (int)(height * scale);
            roi.y = ( target_width - roi.height ) / 2;
        }
        else
        {
            roi.y = 0;
            roi.height = target_width;
            roi.width = (int)(width * scale);
            roi.x = ( target_width - roi.width ) / 2;
        }

//        Mat imageROI = new Mat(square,roi);

        Imgproc.resize( img, new Mat(square,roi), roi.size() );


        return square;
    }

}
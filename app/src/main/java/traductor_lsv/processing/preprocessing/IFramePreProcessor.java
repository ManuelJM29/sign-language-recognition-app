package traductor_lsv.processing.preprocessing;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;

import traductor_lsv.imaging.IFrame;

public interface IFramePreProcessor
{
    IFrame preProcess(CvCameraViewFrame inputFrame);
}

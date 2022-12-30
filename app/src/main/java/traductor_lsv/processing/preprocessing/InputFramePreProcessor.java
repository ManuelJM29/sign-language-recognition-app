package traductor_lsv.processing.preprocessing;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;

import traductor_lsv.imaging.IFrame;

public class InputFramePreProcessor implements IFramePreProcessor
{
    private final IFramePreProcessor frameAdapter;

    public InputFramePreProcessor(IFramePreProcessor inputFrameAdapter) {
        frameAdapter = inputFrameAdapter;
    }
    @Override
    public IFrame preProcess(CvCameraViewFrame inputFrame)
    {
        return frameAdapter.preProcess(inputFrame);
    }
}

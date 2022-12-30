package traductor_lsv.processing.preprocessing;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;

import traductor_lsv.imaging.Frame;
import traductor_lsv.imaging.IFrame;
import traductor_lsv.processing.IFrameProcessor;

public class CameraFrameAdapter implements IFramePreProcessor {

    private final IFrameProcessor downSampler;
    private final IFrameProcessor resizer;

    public CameraFrameAdapter(IFrameProcessor downSamplingFrameProcessor,
                              IFrameProcessor resizingFrameProcessor) {
        downSampler = downSamplingFrameProcessor;
        resizer = resizingFrameProcessor;
    }

    @Override
    public IFrame preProcess(CvCameraViewFrame inputFrame)
    {
        IFrame outputFrame = new Frame(inputFrame.rgba());
        outputFrame = resizer.process(outputFrame);
        outputFrame.setOriginalSize(inputFrame.rgba().size());
        outputFrame = downSampler.process(outputFrame);
        return outputFrame;
    }

}

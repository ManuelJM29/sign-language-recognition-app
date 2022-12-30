package traductor_lsv.processing.postprocessing;

import traductor_lsv.imaging.IFrame;

public interface IFramePostProcessor {

    IFrame postProcess(IFrame inputFrame);

}

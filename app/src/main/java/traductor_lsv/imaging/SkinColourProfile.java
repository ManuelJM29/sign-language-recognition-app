package traductor_lsv.imaging;

import org.opencv.core.Scalar;

public class SkinColourProfile {

    private static SkinColourProfile instance = null;

    public Scalar lowerBound = new Scalar(0);
    public Scalar upperBound = new Scalar(0);

    protected SkinColourProfile() {
        setDefaultSkinColourProfile();
    }

    public static SkinColourProfile getInstance() {
        if (instance == null){
            instance = new SkinColourProfile();
        }
        return instance;
    }

    private void setDefaultSkinColourProfile(){

        // H
        lowerBound.val[0] = 0;
        upperBound.val[0] = 25;

        // S
        lowerBound.val[1] = 40;
        upperBound.val[1] = 255;

        // V
        lowerBound.val[2] = 60;
        upperBound.val[2] = 255;

        // A
        lowerBound.val[3] = 0;
        upperBound.val[3] = 255;
    }

}

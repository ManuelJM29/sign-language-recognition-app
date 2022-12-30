package traductor_lsv;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import traductor_lsv.imaging.IFrame;
import traductor_lsv.processing.DetectionMethod;
import traductor_lsv.processing.DownSamplingFrameProcessor;
import traductor_lsv.processing.IFrameProcessor;
import traductor_lsv.processing.MainFrameProcessor;
import traductor_lsv.processing.ResizingFrameProcessor;
import traductor_lsv.processing.SizeOperation;
import traductor_lsv.processing.postprocessing.IFramePostProcessor;
import traductor_lsv.processing.postprocessing.OutputFramePostProcessor;
import traductor_lsv.processing.postprocessing.UpScalingFramePostProcessor;
import traductor_lsv.processing.preprocessing.CameraFrameAdapter;
import traductor_lsv.processing.preprocessing.IFramePreProcessor;
import traductor_lsv.processing.preprocessing.InputFramePreProcessor;
import traductor_lsv.svm.FrameClassifier;

public class Main extends Activity implements CvCameraViewListener2 {

    private CameraBridgeViewBase CameraView;
    private IFramePreProcessor PreProcesador;
    private IFramePostProcessor PostProcesador;
    private IFrameProcessor MFP, FC;
    private TextView txtView;
    private String LetraAct, LetraAnt, ModLetra;

    private final BaseLoaderCallback  BLCb = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                CameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.traductor_lsv_interfaz);
        CameraView = findViewById(R.id.traductor_lsv_interfaz);
        CameraView.setVisibility(SurfaceView.VISIBLE);
        CameraView.setCvCameraViewListener(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
        }

        Button btnAgg = findViewById(R.id.btn_agg);
        Button btnLimpiar = findViewById(R.id.btn_limpiar);
        Button btnBorrar = findViewById(R.id.btn_borrar);
        Button btnVer = findViewById(R.id.btn_ver);
       // Button btnoff = findViewById(R.id.btn_off);
       // Button btnon = findViewById(R.id.btn_on);
        txtView = findViewById(R.id.textView);


        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if (txtView.getText().toString().length() > 0)
                            txtView.setText(txtView.getText().toString().substring(0, txtView.getText().toString().length() - 1));
                        txtView.append(ModLetra);
                    }
                });

        //btnoff.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View v) {
             //       CameraView.setVisibility(SurfaceView.INVISIBLE);
          //  }
      //  });

       // btnon.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View v) {
             //   CameraView.setVisibility(SurfaceView.VISIBLE);
            //}
        //});

        btnAgg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (txtView.getText().toString().length() > 0)
                    if (!ModLetra.equals("?") & txtView.getText().toString().length() < 15)
                        txtView.append(ModLetra);
            }
        });



        btnLimpiar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtView.setText("");
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (txtView.getText().toString().length() > 0)
                    txtView.setText(txtView.getText().toString().substring(0, txtView.getText().toString().length() - 1));
            }
        });

    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame)
    {
        IFrame preProcessedFrame = PreProcesador.preProcess(inputFrame);
        IFrame processedFrame = MFP.process(preProcessedFrame);
        IFrame postProcessedFrame = PostProcesador.postProcess(processedFrame);
        IFrame classifiedFrame = FC.process(postProcessedFrame);
        LetraAnt = LetraAct;
        LetraAct = getDisplayableLetter(classifiedFrame.getLetterClass().toString());
        setLetterIfChanged();
        return classifiedFrame.getRGBA();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (CameraView != null)
            CameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, BLCb);
        } else {
            BLCb.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (CameraView != null)
            CameraView.disableView();
    }



    public void onCameraViewStarted(int width, int height) {

        setProcessors();

        File xmlFile = initialiseXMLTrainingData();

        FC = new FrameClassifier(xmlFile);

    }

    public void onCameraViewStopped() {

    }

    private void setProcessors(){

        PreProcesador = new InputFramePreProcessor(
                            new CameraFrameAdapter(
                                new DownSamplingFrameProcessor(),
                                new ResizingFrameProcessor(SizeOperation.UP)
                            )
                        );
        MFP = new MainFrameProcessor(DetectionMethod.CANNY_EDGES);
        PostProcesador = new OutputFramePostProcessor(
                            new UpScalingFramePostProcessor(),
                            new ResizingFrameProcessor(SizeOperation.UP)
                        );
    }

    private void setLetterIfChanged(){
        if (!LetraAct.equals(LetraAnt)){
            ModLetra = LetraAct;
            if (ModLetra.equals("J"))
                ModLetra = "?";
           // setPossibleLetter(ModLetra);
        }
    }

    private void setPossibleLetter(final String currentLetterForMod){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (txtView.getText().toString().length() > 0)
                    txtView.setText(txtView.getText().toString().substring(0, txtView.getText().toString().length() - 1));

                txtView.append(currentLetterForMod);

            }
        });
    }

    private String getDisplayableLetter(String letter){

        if ("J".equals(letter)) {
            return "?";
        }
        return letter;

    }

    private File initialiseXMLTrainingData(){

        try {
            InputStream IS = getResources().openRawResource(R.raw.entrenamiento_10);
            File DC = getDir("cascade", Context.MODE_PRIVATE);
            File CF = new File(DC,"entrenamiento.xml");

            FileOutputStream FOS = new FileOutputStream(CF);

            byte[] buffer = new byte[4096];
            int b;
            while ((b = IS.read(buffer)) != -1) {
                FOS.write(buffer, 0, b);
            }

            IS.close();
            FOS.close();

            return CF;

        } catch (Exception e) {
            e.printStackTrace();
            return new File("");
        }

    }

}
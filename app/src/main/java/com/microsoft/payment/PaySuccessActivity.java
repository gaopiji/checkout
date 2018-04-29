package com.microsoft.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.payment.Common.ConstantAndStatic;
import com.microsoft.payment.Common.DetectionTask;
import com.microsoft.payment.Common.VerificationTask;
import com.microsoft.payment.bean.Product;
import com.microsoft.payment.bean.ProductBean;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static com.microsoft.payment.MainActivity.CAMERA_ID;
import static com.microsoft.payment.RecommendActivity.CHOICE_PRODUCT_ID;
import static com.microsoft.payment.RecommendActivity.CHOICE_PRODUCT_POS;
import static com.microsoft.payment.RecommendActivity.CHOICE_PRODUCT_PRICE;

/**
 * Created by v-pigao on 2/28/2018.
 */

public class PaySuccessActivity extends Activity implements View.OnClickListener ,SurfaceHolder.Callback{

    private static final String TAG ="PaySuccessActivity";
    private static final int SHOW_NORMAL = 801;
    private static final int SHOW_SUCCESS = 802;
    private static final int SHOW_FAIL = 803;
    private static final int DETECT = 804;
    private static final int GO_HOME = 805;
    private static final int GO_HOME_BTN = 806;

    Intent mIntent;
    int pos;
    String id;
    String price;

    Product mProduct;
    private ImageView mClose_btn;
    private ImageView mRecomendImg;
    private TextView mProductNameTxt;
    private TextView mOrderTotalTxt;
    private ImageView mScanBtn;
    private ImageView mGoHomeBtn;
    private TextView mCancleTxt;
    private ImageView mAnimationImg;
    private TextView mTipsTxt;
    private SurfaceView mSurfaceView;

    private SurfaceHolder mHolder;
    AnimationDrawable mAnimation;
    private Camera mCamera;
    DetectionTask mDetectionTask;
    VerificationTask mVerificationTask;

    private int detectTimes=0;

    Handler mHandler;
    private Bitmap mBitmap;

    private boolean mIsSurfaceCreated = false;
    private int errorTimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paysuccess);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mIntent = getIntent();
        pos = mIntent.getIntExtra(CHOICE_PRODUCT_POS, -2);
    //    id = mIntent.getStringExtra(CHOICE_PRODUCT_ID);
      //  price = mIntent.getStringExtra(CHOICE_PRODUCT_PRICE);
     //   mProduct = ConstantAndStatic.productMap.get(id);
        errorTimes = 0;



        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case SHOW_NORMAL:
                        Log.d(TAG,"SHOW_NORMAL");
                        starAnimationDrawable(SHOW_NORMAL);
                        mHandler.sendEmptyMessageDelayed(DETECT,200);
                        break;
                    case SHOW_SUCCESS:
                        Log.d(TAG,"SHOW_SUCCESS");
                        starAnimationDrawable(SHOW_SUCCESS);
                        mHandler.sendEmptyMessageDelayed(GO_HOME_BTN, 2000);
                        break;
                    case SHOW_FAIL:
                        errorTimes++;
                        detectTimes=0;
                        Log.d(TAG,"SHOW_FAIL");
                        starAnimationDrawable(SHOW_FAIL);
                        if(errorTimes<3)
                            mHandler.sendEmptyMessageDelayed(SHOW_NORMAL, 2000);
                        else{
                            mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
                        }
                        break;
                    case DETECT:
                        detectTimes++;
                        Log.d(TAG,"detectTimes: "+detectTimes);
                        getPreViewImage();
                        break;
                    case GO_HOME_BTN:
                        stopAnimationDrawable();
                        mGoHomeBtn.setVisibility(View.VISIBLE);
                        mHandler.sendEmptyMessageDelayed(GO_HOME, 7000);
                        break;
                    case GO_HOME:
                        stopAnimationDrawable();
                        toHome();
                        break;

                }
            }
        };

        initView();
        initEvent();

    }

    private void initView() {
        mClose_btn = findViewById(R.id.close_btn);
        mRecomendImg = findViewById(R.id.recomendImg);
        mProductNameTxt = findViewById(R.id.productNameTxt);
        mOrderTotalTxt = findViewById(R.id.orderTotalTxt);
        mCancleTxt = findViewById(R.id.cancleTxt);
        mScanBtn = findViewById(R.id.scanBtn);
        mAnimationImg = findViewById(R.id.animationImg);
        mTipsTxt = findViewById(R.id.tipsTxt);
        mSurfaceView =  findViewById(R.id.surface_view);
        mGoHomeBtn = findViewById(R.id.goHomeBtn);
    }

    private void initEvent() {
        mClose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mCancleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        switch (pos){
            case 1:
                mProductNameTxt.setText(getString(R.string.pc1_name));
                mRecomendImg.setImageResource(R.drawable.pc1);
                break;
            case 2:
                mProductNameTxt.setText(getString(R.string.pc2_name));
                mRecomendImg.setImageResource(R.drawable.pc2);
                break;
            case 3:
                mProductNameTxt.setText(getString(R.string.pc3_name));
                mRecomendImg.setImageResource(R.drawable.pc3);
                break;
            case 4:
                mProductNameTxt.setText(getString(R.string.pc4_name));
                mRecomendImg.setImageResource(R.drawable.pc4);
                break;
            case 5:
                mProductNameTxt.setText(getString(R.string.pc5_name));
                mRecomendImg.setImageResource(R.drawable.pc5);
                break;
            case 6:
                mProductNameTxt.setText(getString(R.string.pc6_name));
                mRecomendImg.setImageResource(R.drawable.pc6);
                break;
            case 7:
                mProductNameTxt.setText(getString(R.string.pc7_name));
                mRecomendImg.setImageResource(R.drawable.pc7);
                break;
            case 8:
                mProductNameTxt.setText(getString(R.string.pc8_name));
                mRecomendImg.setImageResource(R.drawable.pc8);
                break;
            case 9:
                mProductNameTxt.setText(getString(R.string.pc9_name));
                mRecomendImg.setImageResource(R.drawable.pc9);
                break;
            case 10:
                mProductNameTxt.setText(getString(R.string.pc10_name));
                mRecomendImg.setImageResource(R.drawable.pc10);
                break;
        }





        mOrderTotalTxt.setText("Order total:  $ 0.00" );

        mScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScanBtn.setVisibility(View.GONE);
                mClose_btn.setVisibility(View.GONE);
                mCancleTxt.setVisibility(View.GONE);
                mAnimationImg.setVisibility(View.VISIBLE);
                mTipsTxt.setVisibility(View.VISIBLE);
                startPreview();
                mHandler.sendEmptyMessage(SHOW_NORMAL);
            }
        });

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);

        mGoHomeBtn.setVisibility(View.GONE);
        mGoHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toHome();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_root:
                finish();
                break;
        }
    }

    void starAnimationDrawable( int Status){
        if(mAnimation!=null) {
            mAnimation.stop();
        }
           switch (Status){
               case SHOW_NORMAL:
                   mAnimationImg.setImageResource(R.drawable.anima_normal);
                   mAnimation = (AnimationDrawable)mAnimationImg.getDrawable();
                   mAnimation.setOneShot(false);
                   mTipsTxt.setText(R.string.tips_normal);
                   break;
               case SHOW_SUCCESS:
                   mAnimationImg.setImageResource(R.drawable.anima_success);
                   mAnimation = (AnimationDrawable)mAnimationImg.getDrawable();
                   mAnimation.setOneShot(true);
                   mTipsTxt.setText(R.string.tips_success);
                   break;
               case SHOW_FAIL:
                   mAnimationImg.setImageResource(R.drawable.anima_fail);
                   mAnimation = (AnimationDrawable)mAnimationImg.getDrawable();
                   mAnimation.setOneShot(true);
                   mTipsTxt.setText(R.string.tips_fail);
                   break;
           }

        mAnimation.start();
    }

    void stopAnimationDrawable(){
        if(mAnimation!=null) {
            mAnimation.stop();
            mAnimation = null;
        }
        mAnimationImg.setVisibility(View.GONE);
        mTipsTxt.setVisibility(View.GONE);
    }

    private void startPreview() {
        if (mCamera != null || !mIsSurfaceCreated) {
            Log.d(TAG, "startPreview will return");
            return;
        }
        mCamera = Camera.open(CAMERA_ID);
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        mCamera.startPreview();
    }
    private void stopPreview() {
        //release Camera
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void getPreViewImage() {

        if (mCamera != null) {
            Log.d(TAG,"getPreViewImage");
            mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    if (mCamera != null) {
                        Log.d(TAG,"onPreviewFrame");
                        Camera.Size size = camera.getParameters().getPreviewSize();
                        try {
                            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                            if (image != null) {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);

                                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());

                                Matrix matrix = new Matrix();
                                matrix.setRotate(270);

                                mBitmap = Bitmap.createBitmap(bmp, 0, 0,
                                        bmp.getWidth(), bmp.getHeight(), matrix, true);

                                detect();
                                stream.close();
                                mCamera.setOneShotPreviewCallback(null);

                            }
                        } catch (Exception ex) {
                            Log.e("gpj", "Error:" + ex.getMessage());
                        }
                    }
                }
            });
        }
    }

    private void detect() {
        Log.d(TAG,"detect()");
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        // Start a background task to detect faces in the image.
        mDetectionTask = DetectionTask.getInstance();
        mDetectionTask.setmDetectionListener(new DetectionTask.DetectionListener() {
            @Override
            public void detectFinesh(Face[] result, boolean success) {
                setUiAfterDetection(result, success);
            }
        });
        mDetectionTask.execute(inputStream);

    }

    private void verify(UUID uuid) {
        Log.d(TAG,"verify()");
        mVerificationTask = VerificationTask.getInstance();
        mVerificationTask.setmVerificationListener(new VerificationTask.VerificationListener() {
            @Override
            public void verification(VerifyResult result) {
                setUiAfterVerification(result);
            }
        });
        mVerificationTask.setmFaceId0(ConstantAndStatic.faceId);
        // Start a background task to detect faces in the image.
        mVerificationTask.setmFaceId1(uuid);
        mVerificationTask.execute();
    }

    private void setUiAfterDetection(Face[] result, boolean succeed){
        Log.d(TAG,"setUiAfterDetection");
        if (succeed && result.length > 0){
            verify(result[0].faceId);
        }else {
            if(detectTimes<4)
                mHandler.sendEmptyMessageDelayed(DETECT, 200);
            else{
                mHandler.sendEmptyMessageDelayed(SHOW_FAIL, 200);
            }
        }
    }

    private void setUiAfterVerification(VerifyResult result){
        Log.d(TAG,"setUiAfterVerification()");
        Log.d(TAG,"result.isIdentical:"+result.isIdentical);
        Log.d(TAG,"result.confidence:"+result.confidence);
     if(result.isIdentical&&result.confidence>0.7){
         mHandler.sendEmptyMessageDelayed(SHOW_SUCCESS, 200);
        }
     else {
         if(detectTimes<4)
             mHandler.sendEmptyMessageDelayed(DETECT, 200);
         else{
             mHandler.sendEmptyMessageDelayed(SHOW_FAIL, 200);
         }
     }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsSurfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsSurfaceCreated = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAnimation!=null) {
            mAnimation.stop();
            mAnimation=null;
        }
        stopPreview();
        mHandler.removeMessages(DETECT);
    }

    private void toHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

package com.microsoft.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microsoft.facesdk.FaceLibraryClient;
import com.microsoft.payment.Common.ConstantAndStatic;
import com.microsoft.payment.Common.DetectionTask;
import com.microsoft.payment.Common.RingProgressView;
import com.microsoft.payment.bean.PersonBean;
import com.microsoft.payment.bean.Product;
import com.microsoft.payment.bean.ProductBean;
import com.microsoft.projectoxford.face.contract.Face;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.microsoft.payment.Common.ConstantAndStatic.IP;
import static com.microsoft.payment.Common.ConstantAndStatic.setPersonBean;
import static com.microsoft.payment.SampleApp.getHttpClient;

/**
 * Created by v-pigao on 2/27/2018.
 */

public class MainActivity extends Activity implements SurfaceHolder.Callback {

    public static  String IDENTIFICATION_URL = ConstantAndStatic.HEAD_URL+ConstantAndStatic.PAYMENT_QUERY_URL;

    private SurfaceView mSurfaceView;
    private RingProgressView ringProgressView;
    private TextView mCountdownTxt;
    private ImageView mErrorOkBtn;
    private ImageView mConfigBtn;

    private RelativeLayout mError_layout;

    private TextView mTipsTxt;

    private SurfaceHolder mHolder;

    private Camera mCamera;

    DetectionTask mDetectionTask;


    public static final int BUFFERTAG = 901;
    public static final int UPDATE_RING = 902;
    public static final int CANCEL_RING = 903;
    public static final int showwwww = 904;
    public static final int SHOW_ERROR = 905;
    public static final int REFRESH = 906;

    private int mProgress = 0;
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case BUFFERTAG:
                    if (canTakeFrame&&mProgress < 80) {
                        getPreViewImage();
                    }
                    break;

                case UPDATE_RING:


                    if (isConfirm) {
                        if(mProgress<33)
                            mCountdownTxt.setText("3");
                        else if(mProgress>=33 &&mProgress<67){
                            mCountdownTxt.setText("2");
                        }else{
                            mCountdownTxt.setText("1");
                        }
                        mProgress = ringProgressView.getCurrentProgress();
                        mProgress++;
                        mHandler.sendEmptyMessageDelayed(UPDATE_RING, 20);
                    } else {
                        mCountdownTxt.setVisibility(View.INVISIBLE);
                        mProgress = 0;
                        mHandler.removeMessages(UPDATE_RING);
                    }
                    ringProgressView.setCurrentProgress(mProgress < 100 ? mProgress : 100);
                    ringProgressView.postInvalidate();
                    break;
                case CANCEL_RING:
                    mProgress = 0;
                    mCountdownTxt.setVisibility(View.INVISIBLE);
                    ringProgressView.setCurrentProgress(0);
                    ringProgressView.postInvalidate();
                    mHandler.removeMessages(UPDATE_RING);
                    break;
                case showwwww:
                    mTipsTxt.setText(msg.obj.toString());
                    break;

                case SHOW_ERROR:
                    mError_layout.setVisibility(View.VISIBLE);
                    mTipsTxt.setText(R.string.tips_put_error);
                    mHandler.sendEmptyMessage(CANCEL_RING);
                    mHandler.sendEmptyMessageDelayed(REFRESH, 10000);
                    break;
                case REFRESH:
                    if(mError_layout.getVisibility()==View.VISIBLE) {
                        mError_layout.setVisibility(View.GONE);
                        mTipsTxt.setText(R.string.tips_put_face);
                        refresh();
                    }
                    break;
            }

        }
    };


    private boolean mIsSurfaceCreated = false;

    //private static final int CAMERA_ID = 0; //rear camera
    public static final int CAMERA_ID = 1; //front camera
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int OK = 9999;

    private Bitmap mBitmap;
    private Bitmap mNeedBitmap;

    private volatile boolean canTakeFrame;

    private volatile boolean isConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ringProgressView = findViewById(R.id.ringProgress);
        FaceLibraryClient.getClient(this);

        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, OK);

            }
        }
        initView();
        initEvent();

    }

    @Override
    protected void onPause() {
        super.onPause();

        stopPreview();
        mHandler.removeMessages(BUFFERTAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPersonBean(null);
        mProgress = 0;
        mCountdownTxt.setVisibility(View.INVISIBLE);
        startPreview();

        mError_layout.setVisibility(View.GONE);
        mTipsTxt.setText(R.string.tips_put_face);
        mHandler.removeMessages(REFRESH);
        refresh ();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    private void initView() {
        mSurfaceView = findViewById(R.id.surface_view);
        mCountdownTxt = findViewById(R.id.count_down_txt);
        mTipsTxt = findViewById(R.id.tipsTxt);
        mError_layout = findViewById(R.id.error_layout);
        mErrorOkBtn = findViewById(R.id.error_ok_btn);
        mConfigBtn = findViewById(R.id.configBtn);
    }

    private void initEvent() {
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mErrorOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mError_layout.setVisibility(View.GONE);
                mTipsTxt.setText(R.string.tips_put_face);
                mHandler.removeMessages(REFRESH);
                refresh ();
            }
        });
        mConfigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomizeDialog();
            }
        });
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
        canTakeFrame = false;
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



    private void detect() {
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

    // Show the result on screen when detection is done.
    private void setUiAfterDetection(Face[] result, boolean succeed) {
        // Detection is done, hide the progress dialog.
        // mProgressDialog.dismiss();


        if (succeed && result.length > 0) {

            if (!isConfirm) {
                isConfirm = true;
                mHandler.sendEmptyMessage(UPDATE_RING);
                mCountdownTxt.setText("3");
                mCountdownTxt.setVisibility(View.VISIBLE);
            }
            if (mProgress <= 30) {
                ConstantAndStatic.faceId = result[0].faceId;
            }
            if (mProgress >= 70) {
                mHandler.removeMessages(BUFFERTAG);
                canTakeFrame = false;
                // The information about the detection result.
                if (result != null && isValid(result)) {
                    Log.d("gpj", result.length + " face"
                            + (result.length != 1 ? "s" : "") + " detected");
                    Log.d("gpj", IDENTIFICATION_URL);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    mNeedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    IDENTIFICATION_URL = "http://"+IP+"/"+ConstantAndStatic.PAYMENT_QUERY_URL;
                    MultipartBody body = new MultipartBody.Builder("AaB03x")
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image","head_image.jpg", RequestBody.create(MediaType.parse("image/jpg"), bos.toByteArray()))
                            .addFormDataPart("From","payment")
                            .build();
                    Request request = new Request.Builder()
                            .url(IDENTIFICATION_URL)//地址
                            .post(body)//添加请求体
                            .build();
                    getHttpClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            mHandler.sendEmptyMessage(SHOW_ERROR);
                            Log.d("gpj", "response-->onFailure" );
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String resS = response.body().string();

                            JSONObject jo1 = null;
                            PersonBean personBean = new PersonBean();
//                            {
//                                Message m = new Message();
//                                m.what = showwwww;
//                                m.obj =IDENTIFICATION_URL+">>>" +resS;
//                                mHandler.sendMessage(m);
//                            }
                            try {
                                jo1 = new JSONObject(resS);

                                int status = jo1.getInt("StatusCode");
                                if(status==201){
                                    JSONObject jo2 = jo1.getJSONObject("PersonInfo");
                                    personBean.setId(jo2.getString("Id"));
                                    personBean.setName(jo2.getString("Name"));
                                    personBean.setPerson_type(jo2.getString("PersonType"));
                                    personBean.setPhoto_url(jo2.getString("PhotoUrl"));

                                    JSONArray jsonArray = jo2.getJSONArray("GoodsDetails");

                                    List<ProductBean> products = new ArrayList<>();


                                    if(jsonArray!=null) {
                                        int len = jsonArray.length();
                                        for (int i = 0; i < len; i++) {
                                            JSONObject jp = new JSONObject(jsonArray.get(i).toString());
                                            ProductBean productBean = new ProductBean();
                                            productBean.setGoodsId(jp.getString("GoodsId"));
                                            productBean.setName(jp.getString("GoodsName"));
                                            productBean.setPrice(jp.getDouble("GoodsPrice"));
                                            productBean.setTimes(jp.getInt("LikedTimes"));

                                            products.add(productBean);
                                        }
                                    }
//                                    {
//
//                                Product product = ConstantAndStatic.productMap.get("3cdf42e8-533e-4c32-83f6-40bcff2ac824");
//
//                                ProductBean productBean = new ProductBean();
//                                productBean.setPrice(product.price);
//                                productBean.setGoodsId(product.goodsId);
//                                productBean.setName(product.name);
//                                productBean.setTimes(38);
//                                products.add(productBean);
//                                    }
                                    personBean.setGoodslist(products);
                                    setPersonBean(personBean);
                                }else{
                                    mHandler.sendEmptyMessage(SHOW_ERROR);
                                   return;
                                }


                            } catch (JSONException e) {
                                Log.d("gpj", "response-->catch.error" );
                                mHandler.sendEmptyMessage(SHOW_ERROR);
                                e.printStackTrace();

                            }

//                            {
//
////                                personBean.setId(product);
////                                personBean.setName("gpj");
////                                personBean.setPerson_type(PersonBean.VIP);
////                                personBean.setPhoto_url("http://www.qqxoo.com/uploads/allimg/180303/1615123251-2.jpg");
//
//                                List<ProductBean> products = new ArrayList<>();
//
//                                Product product = ConstantAndStatic.productMap.get("862bd0c0-2cd2-470c-af11-acb95eade658");
//
//                                ProductBean productBean = new ProductBean();
//                                productBean.setPrice(product.price);
//                                productBean.setGoodsId(product.goodsId);
//                                productBean.setName(product.name);
//                                productBean.setTimes(38);
//                                products.add(productBean);
//
//                                product = ConstantAndStatic.productMap.get("94d72f86-a53b-4949-bb79-453204ff73d1");
//                                productBean = new ProductBean();
//                                productBean.setPrice(product.price);
//                                productBean.setGoodsId(product.goodsId);
//                                productBean.setName(product.name);
//                                productBean.setTimes(18);
//                                products.add(productBean);
//
//                                product = ConstantAndStatic.productMap.get("a4c1d563-8383-49e0-b6da-45246a510c63");
//                                productBean = new ProductBean();
//                                productBean.setPrice(product.price);
//                                productBean.setGoodsId(product.goodsId);
//                                productBean.setName(product.name);
//                                productBean.setTimes(28);
//                                products.add(productBean);
//
//                                personBean.setGoodslist(products);
//                                setPersonBean(personBean);
//                            }

                            toRecommendActivity();
                        }
                    });

                }else {
                    refresh ();
                    mHandler.sendEmptyMessage(CANCEL_RING);
                }


            }else {
                mHandler.sendEmptyMessageDelayed(BUFFERTAG, 100);
                canTakeFrame = true;
            }
        } else {
            refresh ();
            mHandler.sendEmptyMessage(CANCEL_RING);
        }

    }

    boolean isValid(Face[] result) {
        return result.length > 0 && result[0].faceRectangle.height > 200 && mNeedBitmap != null;
    }

    private void getPreViewImage() {

        if (mCamera != null) {
            mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    if (mCamera != null) {
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
                                if (mProgress < 30) {
                                    mNeedBitmap = mBitmap;
                                }
                               // mTemp.setImageBitmap(mBitmap);
                                if (canTakeFrame) {
                                    detect();
                                }
                                canTakeFrame = false;
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

    private void refresh (){
        mHandler.sendEmptyMessageDelayed(BUFFERTAG, 100);
        Log.d("gpj", "0 face detected");
        canTakeFrame = true;
        isConfirm = false;

    }

    private void showCustomizeDialog() {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.config_dialog,null);
        final EditText edit_text = dialogView.findViewById(R.id.edit_text);
        edit_text.setHint("10.172.140.105:8091");
        customizeDialog.setTitle("Set your IP: "+IP);
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        IP = edit_text.getText().toString();
                        ConstantAndStatic.HEAD_URL = String.format(getString(R.string.head_url),IP);
                        IDENTIFICATION_URL = ConstantAndStatic.HEAD_URL+ConstantAndStatic.PAYMENT_QUERY_URL;
                        SharedPreferences.Editor editor = SampleApp.getSharedPreferences().edit();
                        editor.putString("IP",IP);
                        editor.commit();

                    }
                });
        customizeDialog.show();
    }

    private void toRecommendActivity(){
        Intent intent = new Intent(this, RecommendActivity.class);
        this.startActivity(intent);
    }
}

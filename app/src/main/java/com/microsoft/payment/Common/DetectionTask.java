package com.microsoft.payment.Common;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.payment.SampleApp;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.InputStream;

/**
 * Created by v-pigao on 3/2/2018.
 */

public class DetectionTask extends AsyncTask<InputStream, String, Face[]> {
    private boolean mSucceed = true;

    public void setmDetectionListener(DetectionListener mDetectionListener) {
        this.mDetectionListener = mDetectionListener;
    }

    private DetectionListener mDetectionListener;


    public static DetectionTask getInstance(){
        return new DetectionTask();
    }

    @Override
    protected Face[] doInBackground(InputStream... params) {
        // Get an instance of face service client to detect faces in image.
        FaceServiceClient faceServiceClient = SampleApp.getFaceServiceClient();
        try {
            publishProgress("Detecting...");

            // Start detection.
            return faceServiceClient.detect(
                    params[0],  /* Input stream of image to detect */
                    true,       /* Whether to return face ID */
                    true,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                    new FaceServiceClient.FaceAttributeType[]{
                            FaceServiceClient.FaceAttributeType.Age,
                            FaceServiceClient.FaceAttributeType.Gender,
                            FaceServiceClient.FaceAttributeType.Glasses,
                            FaceServiceClient.FaceAttributeType.Smile,
                            FaceServiceClient.FaceAttributeType.HeadPose
                    });
        } catch (Exception e) {
            mSucceed = false;
            publishProgress(e.getMessage());
            Log.d("gpj", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        // mProgressDialog.show();
        // Log.d("gpj","Request: Detecting in image " + mImageUri);
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        // mProgressDialog.setMessage(progress[0]);
    }

    @Override
    protected void onPostExecute(Face[] result) {
        if (mSucceed) {
            Log.d("gpj", "Response: Success. Detected " + (result == null ? 0 : result.length)
                    + " face(s) in ");
        }
        if (mDetectionListener != null)
            // Show the result on screen when detection is done.
            mDetectionListener.detectFinesh(result, mSucceed);
    }

   public interface DetectionListener {
        void detectFinesh(Face[] result, boolean success);
    }
}

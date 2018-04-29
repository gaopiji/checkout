package com.microsoft.payment.Common;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.payment.SampleApp;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.VerifyResult;

import java.util.UUID;

/**
 * Created by v-pigao on 3/8/2018.
 */

public class VerificationTask  extends AsyncTask<Void, String, VerifyResult> {
    public static VerificationTask getInstance(){
        return new VerificationTask();
    }

    public void setmFaceId0(UUID mFaceId0) {
        this.mFaceId0 = mFaceId0;
    }

    public void setmFaceId1(UUID mFaceId1) {
        this.mFaceId1 = mFaceId1;
    }

    // The IDs of two face to verify.
    private UUID mFaceId0;
    private UUID mFaceId1;


    public void setmVerificationListener(VerificationListener mVerificationListener) {
        this.mVerificationListener = mVerificationListener;
    }

    private VerificationListener mVerificationListener;
    VerificationTask (UUID faceId0, UUID faceId1) {
        mFaceId0 = faceId0;
        mFaceId1 = faceId1;
    }
    VerificationTask () {
    }

    @Override
    protected VerifyResult doInBackground(Void... params) {
        // Get an instance of face service client to detect faces in image.
        FaceServiceClient faceServiceClient = SampleApp.getFaceServiceClient();
        try{
            publishProgress("Verifying...");

            // Start verification.
            return faceServiceClient.verify(
                    mFaceId0,      /* The first face ID to verify */
                    mFaceId1);     /* The second face ID to verify */
        }  catch (Exception e) {
            publishProgress(e.getMessage());
            Log.d("gpj",e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        Log.d("gpj","Request: Verifying face " + mFaceId0 + " and face " + mFaceId1);
    }

    @Override
    protected void onProgressUpdate(String... progress) {
    }

    @Override
    protected void onPostExecute(VerifyResult result) {
        if (result != null) {
            Log.d("gpj","Response: Success. Face " + mFaceId0 + " and face "
                    + mFaceId1 + (result.isIdentical ? " " : " don't ")
                    + "belong to the same person");
        }

        // Show the result on screen when verification is done.
        if (mVerificationListener != null)
            // Show the result on screen when detection is done.
            mVerificationListener.verification(result);
    }
    public interface VerificationListener {
        void verification(VerifyResult result);
    }
}
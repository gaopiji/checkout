package com.microsoft.payment.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.microsoft.payment.Common.Utils.SharedPreferencesUtil;
import com.microsoft.payment.Common.Utils.http.CallBackUtil;
import com.microsoft.payment.Common.Utils.http.OkhttpUtil;
import com.microsoft.payment.SampleApp;
import com.microsoft.payment.bean.PictureData;
import com.microsoft.payment.helper.ImageHelper;
import com.microsoft.payment.helper.StorageHelper;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.AddPersistedFaceResult;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Response;

import static com.microsoft.payment.Common.Utils.SharedPreferencesUtil.PICTURE_MAP;
import static com.microsoft.payment.Common.ConstantAndStatic.PERSON_GROUP_ID;

public class LongRunningService extends Service {
    private static final String HTTPURL = "http://litchiapi.jstv.com/api/GetFeeds?column=3&PageSize=20&pageIndex=1&val=100511D3BE5301280E0992C73A9DEC41";
    private static final String DATA = "paramz";
    private static final String PICARRAY = "feeds";
    private static final String ITEM = "data";
    private static final String PICTURE_URL = "cover";
    private static final String ID = "id";

    public LongRunningService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("LongRunningService", "executed at " + new Date().
                        toString());

                OkhttpUtil.okHttpGet(HTTPURL, new CallBackUtil<Map<String, PictureData>>() {
                    @Override
                    public Map<String, PictureData> onParseResponse(Call call, Response response) {
                        Map<String, PictureData> newPictureMap = new HashMap<>();
                        Map<String, PictureData> spPictureMap = SharedPreferencesUtil.getHashMapData(PICTURE_MAP, PictureData.class);
//                        String imageUrl;
//                        String id;
//                        try {
//                            //Log.d("LongRunningService", "onParseResponse:" + response.body().string());
//
//                            JSONObject jo1 = new JSONObject(response.body().toString());
//                            JSONObject jo2 = jo1.getJSONObject(DATA);
//                            JSONArray ja = jo2.getJSONArray(PICARRAY);
//
//                            for (int i = 0; i < ja.length(); i++) {
//                                JSONObject data = ja.getJSONObject(i).getJSONObject(
//                                        ITEM);
//                                imageUrl = "http://litchiapi.jstv.com"
//                                        + data.getString(PICTURE_URL);
//                                id = data.getString(ID);
//                                if (!spPictureMap.containsKey(id)
//                                        || !spPictureMap.get(id).getPictureUrl().equals(imageUrl)
//                                        || !spPictureMap.get(id).isLoaded()) {
//                                    PictureData pictureData = new PictureData();
//                                    pictureData.setUserId(id);
//                                    pictureData.setPictureUrl(imageUrl);
//                                    pictureData.setLoaded(false);
//                                    newPictureMap.put(id, pictureData);
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        PictureData p1;
//                        UUID id  = new UUID.randomUUID().to;
//                        if (!spPictureMap.containsKey("1")
//                                || !spPictureMap.get("1").getPictureUrl().equals("https://wx1.sinaimg.cn/mw690/e732ef12ly1foemufwmu3j20h30h30um.jpg")
//                                || !spPictureMap.get("1").isLoaded()) {
//                            p1 = new PictureData();
//                            p1.setLoaded(false);
//                            p1.setPictureUrl("https://wx1.sinaimg.cn/mw690/e732ef12ly1foemufwmu3j20h30h30um.jpg");
//                            p1.setUserId("1");
//                            newPictureMap.put("1", p1);
//                        }
//
//                        p1 = new PictureData();
//                        p1.setLoaded(false);
//                        p1.setPictureUrl("http://img.duoziwang.com/2017/05/06/B4404.jpg");
//                        p1.setUserId("2");
//                        newPictureMap.put("2", p1);
//
//                        p1 = new PictureData();
//                        p1.setLoaded(false);
//                        p1.setPictureUrl("http://imge.kugou.com/soft/collection/150/20151026/20151026145023897474.jpg");
//                        p1.setUserId("3");
//                        newPictureMap.put("3", p1);

                        return newPictureMap;
                    }

                    @Override
                    public void onFailure(Call call, Exception e) {
                        Log.d("LongRunningService", "HTTPURL request failed");
                    }

                    @Override
                    public void onResponse(Map<String, PictureData> response) {
                        Log.d("LongRunningService", "HTTPURL request success :" + response);
                        final Map<String, PictureData> spPictureMap = SharedPreferencesUtil.getHashMapData(PICTURE_MAP, PictureData.class);
                        for (final Map.Entry<String, PictureData> enter : response.entrySet()) {
                            OkhttpUtil.okHttpGetBitmap(enter.getValue().getPictureUrl(),
                                    new CallBackUtil.CallBackBitmap() {
                                        @Override
                                        public void onFailure(Call call, Exception e) {
                                            enter.getValue().setLoaded(false);
                                        }

                                        @Override
                                        public void onResponse(final Bitmap mBitmap) {
                                            if (mBitmap != null) {
                                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                                InputStream imageInputStream = new ByteArrayInputStream(stream.toByteArray());
                                                new DetectionTask(PERSON_GROUP_ID, enter.getKey(), mBitmap).execute(imageInputStream);
                                                enter.getValue().setLoaded(true);
                                                spPictureMap.put(enter.getKey(), enter.getValue());
                                                SharedPreferencesUtil.putHashMapData(PICTURE_MAP, spPictureMap);
                                            }
                                        }
                                    });
                        }

                    }
                });


            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 30 * 1000; // 30秒
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }


    // Background task of face detection.
    private class DetectionTask extends AsyncTask<InputStream, String, Face[]> {
        private boolean mSucceed = true;

        String mPersonGroupId;
        String mPersonId;
        Bitmap mBitmap;

        DetectionTask(String personGroupId, String personId, Bitmap bitmap) {
            mPersonGroupId = personGroupId;
            mPersonId = personId;
            mBitmap = bitmap;
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
                        false,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                        null);
            } catch (Exception e) {
                mSucceed = false;
                publishProgress(e.getMessage());
                Log.d("LongRunningService", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(Face[] faces) {
            if (mSucceed) {
                Log.d("LongRunningService", "Response: Success. Detected " + (faces == null ? 0 : faces.length)
                        + " Face(s)");
            }

            // Show the result on screen when detection is done.
            if (mSucceed && faces.length > 0) {
                new AddFaceTask(mPersonGroupId, mPersonId, mBitmap, faces[0]).execute();

            }
        }

    }


    // Background task of adding a face to person.
    class AddFaceTask extends AsyncTask<Void, String, Boolean> {
        String mPersonGroupId;
        String mPersonId;
        Bitmap mBitmap;
        Face mFace;

        AddFaceTask(String personGroupId, String personId, Bitmap bitmap, Face face) {
            mPersonGroupId = personGroupId;
            mPersonId = personId;
            mBitmap = bitmap;
            mFace = face;
            Log.d("LongRunningService", "AddFaceTask-->");
            Log.d("LongRunningService", "mPersonGroupId-->"+mPersonGroupId);
            Log.d("LongRunningService", "mPersonId-->"+mPersonId);
            Log.d("LongRunningService", "mBitmap-->"+mBitmap);
            Log.d("LongRunningService", "mFace-->"+mFace);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("LongRunningService", "AddFaceTask-->doInBackground");
            // Get an instance of face service client to detect faces in image.
            FaceServiceClient faceServiceClient = SampleApp.getFaceServiceClient();
            try {
                publishProgress("Adding face...");
                Log.d("LongRunningService", "AddFaceTask-->doInBackground0");
                UUID personId = UUID.fromString(mPersonId);

                Log.d("LongRunningService", "AddFaceTask-->doInBackground1");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                InputStream imageInputStream = new ByteArrayInputStream(stream.toByteArray());
                Log.d("LongRunningService", "AddFaceTask-->doInBackground2");

                Log.d("LongRunningService", "Request: Adding face to person " + mPersonId);
                // Start the request to add face.
                try {
                    AddPersistedFaceResult result = faceServiceClient.addPersonFace(
                            mPersonGroupId,
                            personId,
                            imageInputStream,
                            "User data",
                            mFace.faceRectangle);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            }
            catch (ClientException e) {
                publishProgress(e.getMessage());
                Log.d("LongRunningService","AddFaceTask-->doInBackground Exception");
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            setUiAfterAddingFace(result, mFace);
        }

        private void setUiAfterAddingFace(boolean succeed, Face face) {
            if (succeed) {
                String faceIds = "";
                String faceId = face.faceId.toString();
                faceIds += faceId + ", ";
                try {
                    File file = new File(getApplicationContext().getFilesDir(), faceId);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    ImageHelper.generateFaceThumbnail(
                            mBitmap, face.faceRectangle)
                            .compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    Uri uri = Uri.fromFile(file);
                    StorageHelper.setFaceUri(
                            faceId, uri.toString(), mPersonId, LongRunningService.this);
                } catch (IOException e) {
                    Log.d("LongRunningService", e.getMessage());
                }

                Log.d("LongRunningService", "Response: Success. Face(s) " + faceIds + "added to person " + mPersonId);
            }
        }
    }

}

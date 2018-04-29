package com.microsoft.facesdk;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.microsoft.projectoxford.face.contract.*;
import com.microsoft.projectoxford.face.contract.PersonGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by chaowa on 2016/7/12.
 */
public class FaceRecognitionCNN {
    private long _handle;

    protected FaceRecognitionCNN(long handle)
    {
        _handle = handle;
    }

    static {
        System.loadLibrary("FaceSdk");
    }

    /**
     * Create face recognizer from preloaded model
     * @param modelBuffer A byte array contains preloaded model
     * @return handle of created face recognizer, this handle need to be passed to all recognition methods as first parameter
     */
    private static native long CreateFaceRecognitionCNN(byte[] modelBuffer);

    /**
     * Release face recognizer
     * @param handle handle to face recognizer need to be released
     */
    private static native void ReleaseFaceRecognitionCNN(long handle);

    /**
     * Extract feature for certain face, by given image and facial landmarks
     * @param handle handle to face recognizer
     * @param landmarks facial landmarks of target face
     * @param imageBuffer image input in BGR or BGRA format
     * @param width image width
     * @param height image height
     * @param stride image scan line size in bytes
     * @param channel image pixel width
     * @param pixelFormat must be Format24bppRgb
     * @param feature feature result container, the size must be large than 512 bytes
     * @return 0 on success, reference windows error code on returning other than 0
     */
    private static native int Extract(long handle, float[] landmarks, byte[] imageBuffer, int width, int height, int stride, int channel, int pixelFormat, byte[] feature);

    /**
     * Get feature distance for two features, the result in range [0.0, 1.0], 0.0 means two features are same.
     * @param handle handle to face recognizer
     * @param feature1 feature
     * @param feature2 another feature
     * @return distance
     */
    private static native float GetDistance(long handle, byte[] feature1, byte[] feature2);

    /**
     * Identify person by given face.
     * @param handle handle to face recognizer
     * @param feature feature extracted
     * @param persons person database, the 1st dimension is person, the 2nd dimension is faces for each person, the 3rd dimension is extracted feature.
     * @param maxCandidate max count of result to return
     * @param identifiedIndex identify result container, the result container size must be bigger than maxCandidate
     * @param confidence identify confidence container, the confidence container size must be bigger than maxCandidate
     * @return actual count of person identified
     */
    private static native int Identify(long handle, byte[] feature, byte[][][] persons, int maxCandidate, int[] identifiedIndex, float[] confidence);

    /**
     * Find similar faces among candidates
     * @param handle handle to face recognizer
     * @param feature feature extracted
     * @param features candidates, array of extracted feature
     * @param maxCandidate max count of result to return
     * @param findSimilarIndex find similar result container, the result container size must be bigger than maxCandidate
     * @param confidence find similar confidence container, the confidence container size must be bigger than maxCandidate
     * @return actual count of similar faces returned
     */
    private static native int FindSimilar(long handle, byte[] feature, byte[][] features, int maxCandidate, int[] findSimilarIndex, float[] confidence);

    /**
     * Group faces into small chunk by similarity
     * @param handle handle to face recognizer
     * @param features features to group
     * @param groupsItemIndex, group result container, the container size must be bigger than count of input features
     * @return actual count of grouped features
     */
    private static native int Group(long handle, byte[][] features, int[] groupsItemIndex);

    /**
     * Create face recognizer from model buffer
     * @param modelBuffer preloaded model buffer
     * @return face recognizer
     * @throws FaceException
     */
    public static FaceRecognitionCNN create(byte[] modelBuffer) throws FaceException {
        if (modelBuffer == null) throw new IllegalArgumentException("modelBuffer can't be null");

        long handle = CreateFaceRecognitionCNN(modelBuffer);

        if (handle == 0)
        {
            throw new FaceException("The FaceRecognitionCNN object initialization failed");
        }

        return new FaceRecognitionCNN(handle);
    }

    /**
     * Extract feature for target face
     * @param landmarks facial landmarks, by which specify the target face
     * @param image BGR image input
     * @return extracted feature
     * @throws FaceException
     */
    public byte[] extract(FaceLandmarks landmarks, Image image) throws FaceException {
        if (_handle == 0) throw new IllegalStateException ("Object has closed");
        if (image == null) throw new IllegalArgumentException("image can't be null");

		byte[] feature = new byte[512];
        float[] points = new float[27 * 2];
        int idx=0;
        /*
        public FeatureCoordinate pupilLeft;
    public FeatureCoordinate pupilRight;
    public FeatureCoordinate noseTip;
    public FeatureCoordinate mouthLeft;
    public FeatureCoordinate mouthRight;
    public FeatureCoordinate eyebrowLeftOuter;
    public FeatureCoordinate eyebrowLeftInner;
    public FeatureCoordinate eyeLeftOuter;
    public FeatureCoordinate eyeLeftTop;
    public FeatureCoordinate eyeLeftBottom;
    public FeatureCoordinate eyeLeftInner;
    public FeatureCoordinate eyebrowRightInner;
    public FeatureCoordinate eyebrowRightOuter;
    public FeatureCoordinate eyeRightInner;
    public FeatureCoordinate eyeRightTop;
    public FeatureCoordinate eyeRightBottom;
    public FeatureCoordinate eyeRightOuter;
    public FeatureCoordinate noseRootLeft;
    public FeatureCoordinate noseRootRight;
    public FeatureCoordinate noseLeftAlarTop;
    public FeatureCoordinate noseRightAlarTop;
    public FeatureCoordinate noseLeftAlarOutTip;
    public FeatureCoordinate noseRightAlarOutTip;
    public FeatureCoordinate upperLipTop;
    public FeatureCoordinate upperLipBottom;
    public FeatureCoordinate underLipTop;
    public FeatureCoordinate underLipBottom;
        * */
        points[idx++] = (float)landmarks.pupilLeft.x;
        points[idx++] = (float)landmarks.pupilLeft.y;
        points[idx++] = (float)landmarks.pupilRight.x;
        points[idx++] = (float)landmarks.pupilRight.y;
        points[idx++] = (float)landmarks.noseTip.x;
        points[idx++] = (float)landmarks.noseTip.y;
        points[idx++] = (float)landmarks.mouthLeft.x;
        points[idx++] = (float)landmarks.mouthLeft.y;
        points[idx++] = (float)landmarks.mouthRight.x;
        points[idx++] = (float)landmarks.mouthRight.y;
        points[idx++] = (float)landmarks.eyebrowLeftOuter.x;
        points[idx++] = (float)landmarks.eyebrowLeftOuter.y;
        points[idx++] = (float)landmarks.eyebrowLeftInner.x;
        points[idx++] = (float)landmarks.eyebrowLeftInner.y;
        points[idx++] = (float)landmarks.eyeLeftOuter.x;
        points[idx++] = (float)landmarks.eyeLeftOuter.y;
        points[idx++] = (float)landmarks.eyeLeftTop.x;
        points[idx++] = (float)landmarks.eyeLeftTop.y;
        points[idx++] = (float)landmarks.eyeLeftBottom.x;
        points[idx++] = (float)landmarks.eyeLeftBottom.y;
        points[idx++] = (float)landmarks.eyeLeftInner.x;
        points[idx++] = (float)landmarks.eyeLeftInner.y;
        points[idx++] = (float)landmarks.eyebrowRightInner.x;
        points[idx++] = (float)landmarks.eyebrowRightInner.y;
        points[idx++] = (float)landmarks.eyebrowRightOuter.x;
        points[idx++] = (float)landmarks.eyebrowRightOuter.y;
        points[idx++] = (float)landmarks.eyeRightInner.x;
        points[idx++] = (float)landmarks.eyeRightInner.y;
        points[idx++] = (float)landmarks.eyeRightTop.x;
        points[idx++] = (float)landmarks.eyeRightTop.y;
        points[idx++] = (float)landmarks.eyeRightBottom.x;
        points[idx++] = (float)landmarks.eyeRightBottom.y;
        points[idx++] = (float)landmarks.eyeRightOuter.x;
        points[idx++] = (float)landmarks.eyeRightOuter.y;
        points[idx++] = (float)landmarks.noseRootLeft.x;
        points[idx++] = (float)landmarks.noseRootLeft.y;
        points[idx++] = (float)landmarks.noseRootRight.x;
        points[idx++] = (float)landmarks.noseRootRight.y;
        points[idx++] = (float)landmarks.noseLeftAlarTop.x;
        points[idx++] = (float)landmarks.noseLeftAlarTop.y;
        points[idx++] = (float)landmarks.noseRightAlarTop.x;
        points[idx++] = (float)landmarks.noseRightAlarTop.y;
        points[idx++] = (float)landmarks.noseLeftAlarOutTip.x;
        points[idx++] = (float)landmarks.noseLeftAlarOutTip.y;
        points[idx++] = (float)landmarks.noseRightAlarOutTip.x;
        points[idx++] = (float)landmarks.noseRightAlarOutTip.y;
        points[idx++] = (float)landmarks.upperLipTop.x;
        points[idx++] = (float)landmarks.upperLipTop.y;
        points[idx++] = (float)landmarks.upperLipBottom.x;
        points[idx++] = (float)landmarks.upperLipBottom.y;
        points[idx++] = (float)landmarks.underLipTop.x;
        points[idx++] = (float)landmarks.underLipTop.y;
        points[idx++] = (float)landmarks.underLipBottom.x;
        points[idx++] = (float)landmarks.underLipBottom.y;

        int hr = Extract(_handle, points, image.getBuffer(), image.getWidth(), image.getHeight(), image.getStride(), image.getChannel(), image.getPixelFormat(), feature);

        if (hr < 0)
        {
            //extract failed
            throw new FaceException("Feature extract failed due to a runtime error");
        }

		return feature;
    }

    /**
     * Get distance for two features
     * @param feature feature
     * @param feature2 another feature
     * @return distance, in range [0.0, 1.0], 0.0 represents the two faces are same, 1.0 represents the two faces are total difference.
     */
    public float distance(byte[] feature, byte[] feature2){
        return GetDistance(_handle, feature, feature2);
    }

    /**
     * Identify person.
     * @param feature extracted feature
     * @param persons person candidates
     * @param candidate max candidate to return
     * @return identified result, each result contains the identified person index and confidence
     */
    public PersonIdentified[] identify(byte[] feature, com.microsoft.facesdk.PersonGroup persons, int candidate) {
        int[] res = new int[candidate];
        float[] conf = new float[candidate];
        byte[][][] personsFeatures = new byte[persons.persons.size()][][];
        int idx = 0;
        for (com.microsoft.facesdk.Person p: persons.persons.values()) {
            personsFeatures[idx] = new byte[p.features.size()][];
            int fid = 0;
            for (Face f: p.features.values()){
                personsFeatures[idx][fid] = f.feature;
                fid++;
            }
            idx++;
        }
        int identify = Identify( _handle, feature,personsFeatures,candidate, res, conf);
        PersonIdentified[] r = new PersonIdentified[identify];
        Person[] ps = persons.persons.values().toArray(new Person[0]);
        for(int i=0; i < identify; i++){
            r[i] = new PersonIdentified();
            r[i].person = ps[res[i]];
            r[i].confidence = conf[i];
        }
        return r;
    }

    /**
     * Group features in to small chunk by similarity
     * @param faces features to group
     * @return grouping result
     */
    public  FaceGroupingResult group(List<byte[]> faces){
        int size = faces.size();
        int[] index = new int[size];
        Group(_handle, faces.toArray(new byte[size][]), index);
        FaceGroupingResult res = new FaceGroupingResult();
        res.groupsId = index;
        return res;
    }

    /**
     * Find similar faces among list of features
     * @param feature target
     * @param features candidates
     * @param candidate max count of result returned
     * @return similar faces
     */
    public ArrayList<Pair<Integer, Float>> findSimilar(byte[] feature, List<byte[]> features, int candidate) {
        int[] index = new int[candidate];
        float[] conf = new float[candidate];
        byte[][] d = new byte[candidate][];
        int c = FindSimilar(_handle, feature, features.toArray(d), candidate, index, conf);
        ArrayList<Pair<Integer, Float>> res = new ArrayList<>();
        for(int i=0;i<c;i++) {
            res.add(new Pair<Integer, Float>(index[i], conf[i]));
        }
        return res;
    }

    public void close() {
        ReleaseFaceRecognitionCNN(_handle);
        _handle = 0;
    }

    @Override
    protected void finalize() {
        if (_handle != 0) {
            ReleaseFaceRecognitionCNN(_handle);
            _handle = 0;
        }
    }
}
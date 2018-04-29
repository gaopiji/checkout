package com.microsoft.facesdk;

import com.microsoft.projectoxford.face.contract.FaceLandmarks;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.microsoft.projectoxford.face.contract.FeatureCoordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fashi on 2015/6/12.
 */
public class FaceDetectionResult {
    private FaceRectangle rect;
    private FaceLandmarks landmarks;

    public FaceRectangle getRectangle()
    {
        return rect;
    }
    public FaceLandmarks getLandmarks(){ return landmarks;}

    protected FaceDetectionResult(int x, int y, int width, int height, float[] landmarksBuffer, int landmarksStartIndex, int landmarksCount)
    {
        rect = new FaceRectangle();
        rect.left = x;
        rect.top = y;
        rect.width = width;
        rect.height = height;
        landmarks = new FaceLandmarks();
        landmarks.pupilLeft = new FeatureCoordinate();
        landmarks.pupilLeft.x = landmarksBuffer[0];
        landmarks.pupilLeft.y = landmarksBuffer[1];
        landmarks.pupilRight = new FeatureCoordinate();
        landmarks.pupilRight.x = landmarksBuffer[2];
        landmarks.pupilRight.y = landmarksBuffer[3];
        landmarks.noseTip = new FeatureCoordinate();
        landmarks.noseTip.x = landmarksBuffer[4];
        landmarks.noseTip.y = landmarksBuffer[5];
        landmarks.mouthLeft = new FeatureCoordinate();
        landmarks.mouthLeft.x = landmarksBuffer[6];
        landmarks.mouthLeft.y = landmarksBuffer[7];
        landmarks.mouthRight = new FeatureCoordinate();
        landmarks.mouthRight.x = landmarksBuffer[8];
        landmarks.mouthRight.y = landmarksBuffer[9];
        landmarks.eyebrowLeftOuter = new FeatureCoordinate();
        landmarks.eyebrowLeftOuter.x = landmarksBuffer[10];
        landmarks.eyebrowLeftOuter.y = landmarksBuffer[11];
        landmarks.eyebrowLeftInner= new FeatureCoordinate();
        landmarks.eyebrowLeftInner.x = landmarksBuffer[12];
        landmarks.eyebrowLeftInner.y = landmarksBuffer[13];
        landmarks.eyeLeftOuter= new FeatureCoordinate();
        landmarks.eyeLeftOuter.x = landmarksBuffer[14];
        landmarks.eyeLeftOuter.y = landmarksBuffer[15];
        landmarks.eyeLeftTop= new FeatureCoordinate();
        landmarks.eyeLeftTop.x = landmarksBuffer[16];
        landmarks.eyeLeftTop.y = landmarksBuffer[17];
        landmarks.eyeLeftBottom= new FeatureCoordinate();
        landmarks.eyeLeftBottom.x = landmarksBuffer[18];
        landmarks.eyeLeftBottom.y = landmarksBuffer[19];
        landmarks.eyeLeftInner= new FeatureCoordinate();
        landmarks.eyeLeftInner.x = landmarksBuffer[20];
        landmarks.eyeLeftInner.y = landmarksBuffer[21];
        landmarks.eyebrowRightInner= new FeatureCoordinate();
        landmarks.eyebrowRightInner.x = landmarksBuffer[22];
        landmarks.eyebrowRightInner.y = landmarksBuffer[23];
        landmarks.eyebrowRightOuter= new FeatureCoordinate();
        landmarks.eyebrowRightOuter.x = landmarksBuffer[24];
        landmarks.eyebrowRightOuter.y = landmarksBuffer[25];
        landmarks.eyeRightInner= new FeatureCoordinate();
        landmarks.eyeRightInner.x = landmarksBuffer[26];
        landmarks.eyeRightInner.y = landmarksBuffer[27];
        landmarks.eyeRightTop= new FeatureCoordinate();
        landmarks.eyeRightTop.x = landmarksBuffer[28];
        landmarks.eyeRightTop.y = landmarksBuffer[29];
        landmarks.eyeRightBottom= new FeatureCoordinate();
        landmarks.eyeRightBottom.x = landmarksBuffer[30];
        landmarks.eyeRightBottom.y = landmarksBuffer[31];
        landmarks.eyeRightOuter= new FeatureCoordinate();
        landmarks.eyeRightOuter.x = landmarksBuffer[32];
        landmarks.eyeRightOuter.y = landmarksBuffer[33];
        landmarks.noseRootLeft= new FeatureCoordinate();
        landmarks.noseRootLeft.x = landmarksBuffer[34];
        landmarks.noseRootLeft.y = landmarksBuffer[35];
        landmarks.noseRootRight= new FeatureCoordinate();
        landmarks.noseRootRight.x = landmarksBuffer[36];
        landmarks.noseRootRight.y = landmarksBuffer[37];
        landmarks.noseLeftAlarTop= new FeatureCoordinate();
        landmarks.noseLeftAlarTop.x = landmarksBuffer[38];
        landmarks.noseLeftAlarTop.y = landmarksBuffer[39];
        landmarks.noseRightAlarTop= new FeatureCoordinate();
        landmarks.noseRightAlarTop.x = landmarksBuffer[40];
        landmarks.noseRightAlarTop.y = landmarksBuffer[41];
        landmarks.noseLeftAlarOutTip= new FeatureCoordinate();
        landmarks.noseLeftAlarOutTip.x = landmarksBuffer[42];
        landmarks.noseLeftAlarOutTip.y = landmarksBuffer[43];
        landmarks.noseRightAlarOutTip= new FeatureCoordinate();
        landmarks.noseRightAlarOutTip.x = landmarksBuffer[44];
        landmarks.noseRightAlarOutTip.y = landmarksBuffer[45];
        landmarks.upperLipTop= new FeatureCoordinate();
        landmarks.upperLipTop.x = landmarksBuffer[46];
        landmarks.upperLipTop.y = landmarksBuffer[47];
        landmarks.upperLipBottom= new FeatureCoordinate();
        landmarks.upperLipBottom.x = landmarksBuffer[48];
        landmarks.upperLipBottom.y = landmarksBuffer[49];
        landmarks.underLipTop= new FeatureCoordinate();
        landmarks.underLipTop.x = landmarksBuffer[50];
        landmarks.underLipTop.y = landmarksBuffer[51];
        landmarks.underLipBottom= new FeatureCoordinate();
        landmarks.underLipBottom.x = landmarksBuffer[52];
        landmarks.underLipBottom.y = landmarksBuffer[53];
    }
}

package com.microsoft.facesdk;

/**
 * Created by fashi on 2015/6/17.
 */
public class FaceException extends Exception {

    public FaceException(){

    }

    public FaceException(String detailMessage) {
        super(detailMessage);
    }
}

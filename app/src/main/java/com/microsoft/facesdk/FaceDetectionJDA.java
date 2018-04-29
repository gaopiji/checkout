package com.microsoft.facesdk;

/**
 * Created by fashi on 2015/6/15.
 */
public class FaceDetectionJDA {
    private static final int maxFaceCount = 64;
    private long _handle;
    private ThreadLocal<int[]> faceRectBuffer = new ThreadLocal<>();
    private ThreadLocal<float[]> landmarksBuffer = new ThreadLocal<>();

    protected FaceDetectionJDA(long handle)
    {
        _handle = handle;
    }

    static {
        System.loadLibrary("FaceSdk");
    }


    /**
     * Creates an JDA Face Detector
     * @param modelBuffer A byte array contains preloaded model
     * @param detectionMode reversed, support 0 as input only
     * @param clampRect indicates whether to clamp result rect on out of image
     * @param threads number of threads to be used in detection
     * @return handle to JDA Face Detector, need to be passed to all detection methods as the first parameter
     */
    private static native long CreateFaceDetectionJDA(byte[] modelBuffer, int detectionMode, boolean clampRect, int threads);

    /**
     * Release an JDA Face Detector
     * @param handle the JDA Face Detector to be released
     */
    private static native void ReleaseFaceDetectionJDA(long handle);

    /**
     * Detects faces in image.
     * @param handle        handle to JDA Face Detector
     * @param faceRects     detection result container, this result container need to be pre-allocated. The detection result is in left, top, width, height format. Order by face size from big to small.
     * @param imageBuffer   gray scale image pixel
     * @param width          image width
     * @param height        image height
     * @param stride        image scan line size in bytes
     * @param channel       image pixel width
     * @param pixelFormat   reserved, must be Format8bppGrayscale
     * @param searchAreaX   search area left
     * @param searchAreaY   search area top
     * @param searchWidth   search area width
     * @param searchHeight  search area height
     * @param minDetectableSize minimum detectable size
     * @param maxDetectableSize maximum detectable size
     * @return                detected face count
     */
    private static native int Detect(long handle, int[] faceRects, byte[] imageBuffer, int width, int height, int stride, int channel, int pixelFormat, int searchAreaX, int searchAreaY, int searchWidth, int searchHeight, int minDetectableSize, int maxDetectableSize);

    /**
     * Detects faces in image.
     * @param handle        handle to JDA Face Detector
     * @param faceRects     detection result container, this result container need to be pre-allocated. The detection result is in left, top, width, height format. Order by face size from big to small.
     * @param imageBuffer   gray scale image pixel
     * @param width          image width
     * @param height        image height
     * @param stride        image scan line size in bytes
     * @param channel       image pixel width
     * @param pixelFormat   reserved, must be Format8bppGrayscale
     * @return                detected face count
     */
    private static native int Detect(long handle, int[] faceRects, byte[] imageBuffer, int width, int height, int stride, int channel, int pixelFormat);

    /**
     * Detects faces in image.
     * @param handle        handle to JDA Face Detector
     * @param faceRects     detection result container, this result container need to be pre-allocated. The detection result is in left, top, width, height format. Order by face size from large to small.
     * @param landmarks     landmarks result container, this result container need to be pre-allocated. The landmark result is in x, y format, point by point. Reference class FaceDetectionResult for more detail about landmark definition.
     * @param imageBuffer   gray scale image pixel
     * @param width          image width
     * @param height        image height
     * @param stride        image scan line size in bytes
     * @param channel       image pixel width
     * @param pixelFormat   reserved, must be Format8bppGrayscale
     * @param searchAreaX   search area left
     * @param searchAreaY   search area top
     * @param searchWidth   search area width
     * @param searchHeight  search area height
     * @param minDetectableSize minimum detectable size
     * @param maxDetectableSize maximum detectable size
     * @return                detected face count
     */
    private static native int DetectAndAlign(long handle, int[] faceRects, float[] landmarks, byte[] imageBuffer, int width, int height, int stride, int channel, int pixelFormat, int searchAreaX, int searchAreaY, int searchWidth, int searchHeight, int minDetectableSize, int maxDetectableSize);

    /**
     * Detects faces in image.
     * @param handle        handle to JDA Face Detector
     * @param faceRects     detection result container, this result container need to be pre-allocated. The detection result is in left, top, width, height format. Order by face size from big to small.
     * @param landmarks     landmarks result container, this result container need to be pre-allocated. The landmark result is in x, y format, point by point. Reference class FaceDetectionResult for more detail about landmark definition.
     * @param imageBuffer   gray scale image pixel
     * @param width          image width
     * @param height        image height
     * @param stride        image scan line size in bytes
     * @param channel       image pixel width
     * @param pixelFormat   reserved, must be Format8bppGrayscale
     * @return                detected face count
     */
    private static native int DetectAndAlign(long handle, int[] faceRects, float[] landmarks, byte[] imageBuffer, int width, int height, int stride, int channel, int pixelFormat);

    /**
     * Get count of facial landmark points
     * @param handle    handle to JDA Face Detector
     * @return          27, represents 27 points facial landmark definition
     */
    private static native int GetLandmarkCount(long handle);

    /**
     * Creates an JDA Face Detector.
     * @param modelBuffer A byte array contains preloaded model
     * @return JDA Face Detector
     * @throws FaceException
     */
    public static FaceDetectionJDA create(byte[] modelBuffer) throws FaceException {
        if (modelBuffer == null) throw new IllegalArgumentException("modelBuffer can't be null");

        long handle = CreateFaceDetectionJDA(modelBuffer, 0, false, 1);

        if (handle == 0)
        {
            throw new FaceException("The FaceDetectionJDA object initialization failed");
        }

        return new FaceDetectionJDA(handle);
    }

    /**
     * detects faces in image
     * @param image gray scale image
     * @return detected faces
     * @throws FaceException
     */
    public FaceRect[] detect(Image image) throws FaceException {
        if (_handle == 0) throw new IllegalStateException ("Object has closed");
        if (image == null) throw new IllegalArgumentException("image can't be null");

        if (faceRectBuffer.get() == null){
            faceRectBuffer.set(new int[maxFaceCount * 4]);
        }

        int[] resultBuffer = faceRectBuffer.get();

        int faceCount = Detect(_handle, resultBuffer, image.getBuffer(), image.getWidth(), image.getHeight(), image.getStride(), image.getChannel(), image.getPixelFormat());

        if (faceCount < 0)
        {
            //detection failed
            throw new FaceException("Detection failed due to a runtime error");
        }

        FaceRect[] faces = new FaceRect[faceCount];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new FaceRect(
                    resultBuffer[4 * i],
                    resultBuffer[4 * i + 1],
                    resultBuffer[4 * i + 2],
                    resultBuffer[4 * i + 3]
            );
        }

        return faces;
    }

    /**
     * detects faces in image
     * @param image gray scale image
     * @return detected faces and landmarks
     * @throws FaceException
     */
    public FaceDetectionResult[] detectAndAlign(Image image) throws FaceException {
        if (_handle == 0) throw new IllegalStateException ("Object has closed");
        if (image == null) throw new IllegalArgumentException("image can't be null");

        if (faceRectBuffer.get() == null){
            faceRectBuffer.set(new int[maxFaceCount * 4]);
        }

        int landmarksCount =  GetLandmarkCount(_handle);

        if (landmarksCount < 0) throw new FaceException("Detection JDA is using an invalid model, its landmarks count is unknown");

        if (landmarksBuffer.get() == null){
            landmarksBuffer.set(new float[maxFaceCount * landmarksCount * 2]);
        }

        int[] rectsResultBuffer = faceRectBuffer.get();
        float[] landmarksResultBuffer = landmarksBuffer.get();

        int faceCount = DetectAndAlign(_handle, rectsResultBuffer, landmarksResultBuffer, image.getBuffer(), image.getWidth(), image.getHeight(), image.getStride(), image.getChannel(), image.getPixelFormat());

        if (faceCount < 0)
        {
            //detection failed
            throw new FaceException("Detection failed due to a runtime error");
        }

        FaceDetectionResult[] faces = new FaceDetectionResult[faceCount];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new FaceDetectionResult(
                    rectsResultBuffer[4 * i],
                    rectsResultBuffer[4 * i + 1],
                    rectsResultBuffer[4 * i + 2],
                    rectsResultBuffer[4 * i + 3],
                    landmarksResultBuffer,
                    i * landmarksCount * 2,
                    landmarksCount
            );
        }

        return faces;
    }

    public void close() {
        ReleaseFaceDetectionJDA(_handle);
        _handle = 0;
    }

    @Override
    protected void finalize() {
        if (_handle != 0) {
            ReleaseFaceDetectionJDA(_handle);
            _handle = 0;
        }
    }
}
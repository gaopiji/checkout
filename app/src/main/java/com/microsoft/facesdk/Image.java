package com.microsoft.facesdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by fashi on 2015/6/15.
 */
public class Image {
    private byte[] buffer;
    private int width;
    private int height;
    private int stride;
    private int channel;
    private int pixelFormat;

    public static final int FormatUnknown = 0;
    public static final int Format8bppGrayscale = 1;
    public static final int Format24bppRgb = 2;
    public static final int Format32bppRgba = 3;

    private static native int ConvertARGBtoRGB(byte[] image, int width, int height, byte[] outputImg);
    private static native int ConvertRGBtoGray(byte[] image, int width, int height, byte[] outputImg);

    public Image(byte[] buffer, int width, int height, int stride, int channel, int pixelFormat) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        this.stride = stride;
        this.channel = channel;
        this.pixelFormat = pixelFormat;
    }

	public static Image loadImageFromFileAsARGB(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        int[] buffer =  new int[imageHeight * imageWidth];
        bitmap.getPixels(buffer, 0, imageWidth * 4, 0, 0, imageWidth, imageHeight);
        ByteBuffer byteBuffer = ByteBuffer.allocate(imageHeight * imageWidth * 4 * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(buffer);
        return new Image(byteBuffer.array(), imageWidth, imageHeight, imageWidth*4, 4, 3);
	}

    public static Image loadImageFromStreamAsARGB(InputStream stream) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        stream.reset();
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, null);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        int[] buffer =  new int[imageHeight * imageWidth];
        bitmap.getPixels(buffer, 0, imageWidth, 0, 0, imageWidth, imageHeight);
        ByteBuffer byteBuffer = ByteBuffer.allocate(imageHeight * imageWidth * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(buffer);
        byte[] bytes = byteBuffer.array();
        byte[] rgbBuffer = new byte[imageWidth * 3 * imageHeight];
        ConvertARGBtoRGB(bytes, imageWidth, imageHeight, rgbBuffer);

        return new Image(rgbBuffer, imageWidth, imageHeight, imageWidth*3, 3, Format24bppRgb);
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStride() {
        return stride;
    }

    public int getChannel() {
        return channel;
    }

    public int getPixelFormat() {
        return pixelFormat;
    }

    public Image toGrayscale() {
        if (pixelFormat != Format24bppRgb) return null;

        byte[] grayscaleBuffer = new byte[width * height];
        ConvertRGBtoGray(buffer, width, height, grayscaleBuffer);

        return new Image(grayscaleBuffer, width,height,width, 1, Format8bppGrayscale);
    }

    private static byte rgb2Gray(byte r, byte g, byte b)
    {
        return (byte)((4897 * r + 9617 * g + 1868 * b) >> 14);
    }
}

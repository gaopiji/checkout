//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license.
//
// Project Oxford: http://ProjectOxford.ai
//
// Project Oxford SDK GitHub:
// https://github.com/Microsoft/ProjectOxford-ClientSDK
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.microsoft.payment;

import android.app.Application;
import android.content.SharedPreferences;

import com.microsoft.facesdk.FaceLibraryClient;
import com.microsoft.payment.Common.ConstantAndStatic;
import com.microsoft.payment.Common.Utils.SharedPreferencesUtil;
import com.microsoft.projectoxford.face.FaceServiceClient;

import okhttp3.OkHttpClient;

public class SampleApp extends Application {
    public static final String TAG = "SampleApp";

    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    private static   SharedPreferences mSharedPreferences;

    private static FaceServiceClient sFaceServiceClient;

    public static OkHttpClient getHttpClient() {
        return client;
    }

    public static OkHttpClient client;


    @Override
    public void onCreate() {
        super.onCreate();
        sFaceServiceClient = FaceLibraryClient.getClient(this);

        SharedPreferencesUtil.getInstance(this, "picdata");
        client = new OkHttpClient();
        ConstantAndStatic.initProducts(this);
        mSharedPreferences = getSharedPreferences("config", 0);

        ConstantAndStatic.IP = mSharedPreferences.getString("IP","255.255.255.255:8091" );
    }

    public static FaceServiceClient getFaceServiceClient() {
        return sFaceServiceClient;
    }




}

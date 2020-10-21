package com.loantec.instantloan;


import android.app.Activity;

import androidx.annotation.NonNull;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.vector.update_app.HttpManager;

import java.io.File;
import java.util.Map;


public class OkGoUpdateHttpUtil implements HttpManager {


    private Activity activity;
    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, final Callback callBack) {
        OkGo.<String>get(url).params(params).execute(new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                if (callBack!=null){
                    callBack.onResponse(response.body());
                }

            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
                if (callBack!=null){
                    callBack.onError("异常");
                }
            }
        });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

    }


    public void asyncPostJson(@NonNull String url, @NonNull String params, @NonNull final Callback callBack) {
        OkGo.<String>post(url).upJson(params).execute(new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                callBack.onResponse(response.body());
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
                callBack.onError("异常");

            }
        });
    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */

    public void asyncPost(@NonNull String url, @NonNull String params, @NonNull final Callback callBack) {
        OkGo.<String>post(url).headers("x-client-id","5698387f9bee6e70a153361f938965").headers("x-client-secret","3c58fd40b9bd063e32693f767807f54d16ceb379").upJson(params).execute(new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                callBack.onResponse(response.body());
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
                callBack.onError("异常");

            }
        });

    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        OkGo.<File>get(url).execute(new com.lzy.okgo.callback.FileCallback(path, fileName) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onStart(com.lzy.okgo.request.base.Request<File, ? extends com.lzy.okgo.request.base.Request> request) {
                super.onStart(request);
                callback.onBefore();
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<File> response) {
                super.onError(response);
                callback.onError("异常");
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);

                callback.onProgress(progress.fraction, progress.totalSize);
            }
        });
    }
}

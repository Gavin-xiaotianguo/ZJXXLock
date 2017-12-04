package com.zjxxlock.zjxxlock.web;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JLW on 2017/9/26.
 */

public class okhttp {


    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/json; charset=utf-8");//设置MediaType
    private static final OkHttpClient client;
    private static final long cacheSize = 1024 * 1024 * 20;//缓存文件最大限制大小20M
    private static String cachedirectory = "/caches";  //设置缓存文件路径
    private static Cache cache = new Cache(new File(cachedirectory), cacheSize);  //

    static {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS);  //设置连接超时时间
        builder.writeTimeout(8, TimeUnit.SECONDS);//设置写入超时时间
        builder.readTimeout(8, TimeUnit.SECONDS);//设置读取数据超时时间
        builder.retryOnConnectionFailure(false);//设置不进行连接失败重试
        builder.cache(cache);//这种缓存
        client = builder.build();


    }

    public static Call DoGetAndCache(String url, int cache_maxAge_inseconds) {

        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().maxAge(cache_maxAge_inseconds, TimeUnit.SECONDS).build())
                .url(url).build();

        Call call = client.newCall(request);
        startrequest(call);
        return call;
    }

    public static Call DoGetOnlyCache(String url) {
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().onlyIfCached().build()).url(url).build();

        Call call = client.newCall(request);
        startrequest(call);
        return call;


    }

    private static void startrequest(final Call call0) {

        try {

            call0.enqueue(new Callback() {

                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    //请求失败

                }

                @Override
                public void onResponse(Call arg0, Response response) throws IOException {

                    //请求返回数据
                }

            });

        } catch (Exception e) {


        }
    }
}

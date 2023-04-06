package com.glasierinc.chatlibrary.webservice;



import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    //retrofit instance containing auth data and another instance for public APIs
    private static Retrofit retrofitClassRoom = null;




    public static synchronized Retrofit getRetrofitClassRoom() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build();

        if (retrofitClassRoom == null) {
            retrofitClassRoom = new Retrofit.Builder()
                    .baseUrl("https://www.medicsportal.org/class_room_webservice/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofitClassRoom;
    }

}
package net.growdev.userojekonline.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.growdev.userojekonline.helper.MyContants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static OkHttpClient initLogging(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        return client;
    }

    private static Gson initGson(){
        Gson gson = new GsonBuilder().setLenient().create();
        return gson;
    }

    private static Retrofit initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(initGson()))
                .client(initLogging())
                .build();

        return retrofit;
    }

    private static Retrofit initRetrofitMap(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyContants.BASE_MAP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(initLogging())
                .build();

        return retrofit;
    }

    public static ApiService getApiService(){
        return initRetrofit().create(ApiService.class);
    }

    public static ApiService getApiServiceMap(){
        return initRetrofitMap().create(ApiService.class);
    }
}

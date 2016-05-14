package com.maxml.exphotoview.db;

import android.os.Handler;
import android.os.Message;

import com.maxml.exphotoview.MainActivity;
import com.maxml.exphotoview.entity.PhotoPage;
import com.maxml.exphotoview.util.PhotoViewConstants;

import java.util.Arrays;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Maxml on 14.05.2016.
 */
public class ServerApi {

    private static final String baseUrl = "https://api.500px.com/";

    private IFunctional api;
    private Handler handler;

    public ServerApi(Handler handler) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IFunctional.class);

        this.handler = handler;
    }

    public void getInfo(int page) {
        if (page < 0) {
            sendMessage(PhotoViewConstants.ERROR_RESULT);
            return;
        }

        Call<PhotoPage> call = api.getInfo(page);
        call.enqueue(new Callback<PhotoPage>() {
            @Override
            public void onResponse(Call<PhotoPage> call, Response<PhotoPage> response) {
                MainActivity.photos.addAll(Arrays.asList(response.body().getPhotos()));
                sendMessage(PhotoViewConstants.SUCCESS_RESULT);
            }

            @Override
            public void onFailure(Call<PhotoPage> call, Throwable t) {
                sendErorMessage("Failure");
            }
        });
    }

    private void sendMessage(int result) {
        Message msg = handler.obtainMessage();
        msg.what = result;
        handler.sendMessage(msg);
    }

    private void sendErorMessage(String message) {
        Message msg = handler.obtainMessage();
        msg.what = PhotoViewConstants.ERROR_RESULT;
        msg.obj = message;

        handler.sendMessage(msg);
    }

    public interface IFunctional {

        @GET("v1/photos?feature=popular&consumer_key=wB4ozJxTijCwNuggJvPGtBGCRqaZVcF6jsrzUadF")
        Call<PhotoPage> getInfo(@Query("page") int page);

    }
}
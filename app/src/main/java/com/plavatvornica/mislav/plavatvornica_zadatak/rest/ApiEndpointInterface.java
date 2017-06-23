package com.plavatvornica.mislav.plavatvornica_zadatak.rest;

import com.plavatvornica.mislav.plavatvornica_zadatak.models.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiEndpointInterface {

    @GET("v1/articles?source=bbc-news&sortBy=top&apiKey=6946d0c07a1c4555a4186bfcade76398")
    Call<ResponseBody> getResponse();

}

package com.app.amtadminapp.retrofit;

public interface ResponseListner<T> {

    void onResponse(ApiResponse<T> it);
}

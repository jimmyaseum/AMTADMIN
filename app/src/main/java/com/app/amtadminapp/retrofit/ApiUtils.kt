package com.app.amtadminapp.retrofit

import com.app.amtadminapp.utils.AppConstant.BASE_URL_WEB
import com.app.amtadminapp.utils.AppConstant.BASE_URL_APP
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Jimmy
 */
object ApiUtils {

    val apiInterface: ApiInterface
        get() = RetrofitClient.getClient(BASE_URL_WEB)!!.create(ApiInterface::class.java)

    val apiInterface2: ApiInterface
        get() = RetrofitClient.getClient(BASE_URL_APP)!!.create(ApiInterface::class.java)

    fun getInstance(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}
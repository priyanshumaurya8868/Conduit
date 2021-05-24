package com.priyanshumaurya8868.realworld.io.api

import com.priyanshumaurya8868.realworld.io.api.services.ConduitApi
import com.priyanshumaurya8868.realworld.io.api.services.ConduitAuthApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object MyConduitClient {

    var authToken: String? = null

    private val authInterceptor = Interceptor { chain ->
        var req = chain.request()
        authToken?.let {
            req = req.newBuilder()
                .header("Authorization", "Token $it")
                .build()
        }
        chain.proceed(req)
    }

    val okhttpBuilder = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)


    val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://conduit.productionready.io/api/")
        .addConverterFactory(MoshiConverterFactory.create())

    val publicApi = retrofitBuilder
        .client(okhttpBuilder.build())
        .build()
        .create(ConduitApi::class.java)
    val authApi = retrofitBuilder
        .client(okhttpBuilder.addInterceptor(authInterceptor).build())
        .build()
        .create(ConduitAuthApi::class.java)
}
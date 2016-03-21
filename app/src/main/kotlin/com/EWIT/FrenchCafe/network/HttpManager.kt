package com.EWIT.FrenchCafe.network

import android.util.Log
import com.EWIT.FrenchCafe.R
import com.EWIT.FrenchCafe.model.dao.NetworkModel
import com.EWIT.FrenchCafe.util.Contextor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Euro on 3/18/16 AD.
 */
object HttpManager {

    val BASE_URL = "http://frenchcafe.socket9.com/main/"
    var retrofit: Retrofit? = null
    val API_KEY = Contextor.context!!.getString(R.string.api_key_distance_matrix)

    fun getInstance(): ApiService{
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()

        }

        val apiService = retrofit!!.create(ApiService::class.java)

        return apiService
    }

    fun getTravelDurationArrival(origin: String, destination: String, arrivalTime: String): Observable<NetworkModel.TravelInfo> {
        return getInstance()
                .getTravelDurationByArrival(origin, destination, arrivalTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTravelDurationDeparture(origin: String, destination: String, departureTime: String): Observable<NetworkModel.TravelInfo> {
        return getInstance()
                .getTravelDurationByDeparture(origin, destination, departureTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

interface ApiService{
    @GET("getDistanceMatrix")
    fun getTravelDurationByDeparture(@Query("source") origins: String,@Query("destination") destinations: String, @Query("departureTime") departureTime: String) : Observable<NetworkModel.TravelInfo>

    @GET("getDistanceMatrix")
    fun getTravelDurationByArrival(@Query("source") origins: String,@Query("destination") destinations: String, @Query("arrivalTime") departureTime: String) : Observable<NetworkModel.TravelInfo>
}
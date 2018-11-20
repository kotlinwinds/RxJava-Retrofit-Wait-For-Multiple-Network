package com.winds

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.winds.model.UserAndEvents
import com.winds.restapi.GitHubEvents
import com.winds.restapi.GitHubUser
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

  //  https://newfivefour.com/android-rxjava-wait-for-network-calls-finish.html

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initObser()
    }

    @SuppressLint("CheckResult")
    private fun initObser() {

        val repo = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userObservable = repo
            .create(GitHubUser::class.java)
            .getUser("google")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

        val eventsObservable = repo
            .create(GitHubEvents::class.java)
            .listEvents("google")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())


        val combined = Observable.zip(userObservable, eventsObservable,
            BiFunction<JsonObject, JsonArray, UserAndEvents> { t1, t2 ->
                return@BiFunction UserAndEvents(t1, t2)
            })

        combined.subscribe({ result ->
            val o: UserAndEvents = result
            Log.d("TAGS", " JSON DATA : ${o.user}")
            Log.d("TAGS", " Array DATA : ${o.events}")

        },
            { err ->
                Log.d("TAGS", " Error : ${err.printStackTrace()}")


            }
        )
    }

}

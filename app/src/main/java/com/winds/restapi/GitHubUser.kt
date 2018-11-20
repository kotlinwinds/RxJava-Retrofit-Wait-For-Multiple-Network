package com.winds.restapi

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubUser {
    @GET("users/{user}")
    fun getUser(@Path("user") user: String): Observable<JsonObject>
}
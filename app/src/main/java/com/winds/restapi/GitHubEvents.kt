package com.winds.restapi

import com.google.gson.JsonArray
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubEvents {
    @GET("users/{user}/events")
    fun listEvents(@Path("user") user: String): Observable<JsonArray>
}
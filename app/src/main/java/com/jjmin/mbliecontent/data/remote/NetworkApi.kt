package com.jjmin.mbliecontent.data.remote

import com.google.gson.JsonArray
import com.jjmin.mbliecontent.data.model.BaseData
import com.jjmin.mbliecontent.data.model.LoginData
import com.jjmin.mbliecontent.data.model.SendShapeData
import com.jjmin.mbliecontent.data.model.DeploymentData
import io.reactivex.Single
import org.json.JSONArray
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface NetworkApi {

    @FormUrlEncoded
    @POST("/auth/signin")
    fun Login(@Field("id") id: String, @Field("passwd") passwd: String): Single<LoginData>

    @FormUrlEncoded
    @POST("/auth/signup")
    fun Register(
        @Field("id") id: String,
        @Field("passwd") passwd: String,
        @Field("email") email: String,
        @Field("pn") pn: String,
        @Field("name") name: String
    ): Single<BaseData>

    @FormUrlEncoded
    @POST("/arrangement/host")
    fun ShapeDeploment(
        @Field("id") id: String
    ): Single<DeploymentData>

    @FormUrlEncoded
    @POST("/arrangement/buffet/save")
    fun SendShape(
        @Field("location") location : String,
        @Field("id") id : String
    ): Single<BaseData>
}
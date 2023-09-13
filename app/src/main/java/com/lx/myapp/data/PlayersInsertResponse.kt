package com.lx.myapp.data


import com.google.gson.annotations.SerializedName

data class PlayersInsertResponse(
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String // OK
) {
    data class Data(
        @SerializedName("body")
        val body: Body,
        @SerializedName("header")
        val header: Header
    ) {
        data class Body(
            @SerializedName("name")
            val age: String,
            @SerializedName("position")
            val position: String, // 홍길동5
            @SerializedName("photo")
            val photo: String
        )

        class Header
    }
}
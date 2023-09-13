package com.lx.myapp.data


import com.google.gson.annotations.SerializedName

data class PlayersFindAllResponse(
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String // OK
) {
    data class Data(
        @SerializedName("body")
        val body: List<Body>,
        @SerializedName("header")
        val header: Header
    ) {
        data class Body(
            @SerializedName("id")
            val id: Int, // 1
            @SerializedName("name")
            val name: String, // 이영수
            @SerializedName("photo")
            val photo: String, // http://172.168.10.23:8001/images/capture11693296860720.jpg
            @SerializedName("position")
            val position: String // MF
        )

        class Header
    }
}
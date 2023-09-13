package com.lx.myapp.data


import com.google.gson.annotations.SerializedName

data class PlayersFindIdResponse(
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
            val id: Int, // 2
            @SerializedName("name")
            val name: String, // 박하민
            @SerializedName("photo")
            val photo: String, // 12312331231123
            @SerializedName("position")
            val position: String // FW
        )

        class Header
    }
}
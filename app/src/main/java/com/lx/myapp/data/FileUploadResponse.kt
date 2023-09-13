package com.lx.myapp.data


import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String // OK
) {
    data class Data(
        @SerializedName("filename")
        val filename: String // /images/앵그리프로그21693294849986.png
    )
}
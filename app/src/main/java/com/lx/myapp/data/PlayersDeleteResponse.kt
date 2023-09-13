package com.lx.myapp.data


import com.google.gson.annotations.SerializedName

data class PlayersDeleteResponse(
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
            @SerializedName("affectedRows")
            val affectedRows: Int, // 1
            @SerializedName("changedRows")
            val changedRows: Int, // 0
            @SerializedName("fieldCount")
            val fieldCount: Int, // 0
            @SerializedName("insertId")
            val insertId: Int, // 0
            @SerializedName("message")
            val message: String, // (Rows matched: 1  Changed: 0  Warnings: 0
            @SerializedName("protocol41")
            val protocol41: Boolean, // true
            @SerializedName("serverStatus")
            val serverStatus: Int, // 2
            @SerializedName("warningCount")
            val warningCount: Int // 0
        )

        class Header
    }
}
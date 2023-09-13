package com.lx.myapp.api

import android.util.Log
import com.lx.myapp.data.FileUploadResponse
import com.lx.myapp.data.PlayersDeleteResponse
import com.lx.myapp.data.PlayersUpdateResponse
import com.lx.myapp.data.PlayersFindAllResponse
import com.lx.myapp.data.PlayersFindIdResponse
import com.lx.myapp.data.PlayersInsertResponse
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


interface BasicApi {
    /*
        @GET("/student")
        fun getStudentList(
            @Query("requestCode") requestCode: String
        ): Call<StudentListResponse>
    */

    @FormUrlEncoded
    @POST("/players/find_all")
    fun requestPlayersFindAll(  // 이 소괄호 안 : 요청 파라미터
        @Field("requestCode") requestCode: String,
    ): Call<PlayersFindAllResponse>

    @FormUrlEncoded
    @POST("/players/find_id")
    fun requestPlayersFindId(  // 이 소괄호 안 : 요청 파라미터
        @Field("id") name: String
    ): Call<PlayersFindIdResponse>

    @FormUrlEncoded
    @POST("/players/insert")
    fun requestPlayersInsert(  // 이 소괄호 안 : 요청 파라미터
        @Field("name") name: String,
        @Field("position") position: String,
        @Field("photo") photo: String

    ): Call<PlayersInsertResponse>

    @FormUrlEncoded
    @POST("/players/update")
    fun requestPlayersUpdate(  // 이 소괄호 안 : 요청 파라미터
        @Field("name") name: String,
        @Field("position") position: String,
        @Field("photo") photo: String,
        @Field("id") id: Int,

    ): Call<PlayersUpdateResponse>

    @FormUrlEncoded
    @POST("/players/delete")
    fun requestPlayersDelete(  // 이 소괄호 안 : 요청 파라미터
        @Field("id") id: Int,
        ): Call<PlayersDeleteResponse>

    /*
        @FormUrlEncoded
        @POST("/student")
        fun postStudentAdd(
            @Field("requestCode") requestCode: String,
            @Field("name") name: String,
            @Field("age") age: String,
            @Field("mobile") mobile: String,
            @Field("filepath") filepath: String
        ): Call<StudentListResponse>
*/
        @Multipart
        @POST("/profile/upload")
        fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part(value="params", encoding="UTF-8") params: HashMap<String,String> = hashMapOf()
        ): Call<FileUploadResponse>

}

class BasicClient {

    companion object {
        const val TAG = "BasicClient"

        private var instance : BasicApi? = null

        val api: BasicApi
            get() {
                return getInstance()
            }

        @Synchronized
        fun getInstance(): BasicApi {
            if (instance == null)
                instance = create()
            return instance as BasicApi
        }

        // 프로토콜
        private const val PROTOCOL = "http"

        // 기본 URL
        const val BASE_URL = "http://172.168.10.23:8001/"

        // 헤더 속성
        private const val CLIENT_ID = ""
        private const val CLIENT_SECRET = ""
        var userId:String = ""

        fun create(): BasicApi {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("X-Client-Id", CLIENT_ID)
                    .addHeader("X-Client-Secret", CLIENT_SECRET)
                    .addHeader("X-Client-UserId", userId)
                    .build()
                return@Interceptor it.proceed(request)
            }

            val clientBuilder = OkHttpClient.Builder()

            if (PROTOCOL == "https") {

                val x509TrustManager: X509TrustManager = object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        val x509Certificates = arrayOf<X509Certificate>()
                        return x509Certificates
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                        Log.d(TAG, ": authType: $authType")
                    }

                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                        Log.d(TAG, ": authType: $authType")
                    }
                }

                try {
                    val sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(null, trustAllCerts, SecureRandom())
                    val sslSocketFactory = sslContext.socketFactory
                    clientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                clientBuilder.hostnameVerifier(RelaxedHostNameVerifier())

            }

            clientBuilder.addInterceptor(headerInterceptor)
            clientBuilder.addInterceptor(httpLoggingInterceptor)
            clientBuilder.callTimeout(60, TimeUnit.SECONDS)       // 호출 타임아웃 시간 설정 60초
            clientBuilder.connectTimeout(60, TimeUnit.SECONDS)    // 연결 타임아웃 시간 설정 60초
            clientBuilder.readTimeout(60, TimeUnit.SECONDS)
            clientBuilder.writeTimeout(60, TimeUnit.SECONDS)

            val client = clientBuilder.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BasicApi::class.java)
        }

        private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        })

        class RelaxedHostNameVerifier : HostnameVerifier {
            override fun verify(hostname: String, session: SSLSession): Boolean {
                return true
            }
        }

        private var format = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREAN)
        private var seqCode = 0


        /**
         * 요청 코드 생성
         */
        @Synchronized
        fun generateRequestCode(): String {
            seqCode += 1
            if (seqCode > 999) {
                seqCode = 1
            }

            var seqCodeStr = seqCode.toString()
            if (seqCodeStr.length == 1) {
                seqCodeStr = "00$seqCodeStr"
            } else if (seqCodeStr.length == 2) {
                seqCodeStr = "0$seqCodeStr"
            }

            val date = Date()
            val dateStr = format.format(date)

            return dateStr + seqCodeStr
        }

    }
}
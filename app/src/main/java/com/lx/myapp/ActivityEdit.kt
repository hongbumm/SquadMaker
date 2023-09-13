package com.lx.myapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.myapp.api.BasicClient
import com.lx.myapp.data.FileUploadResponse
import com.lx.myapp.data.PlayersDeleteResponse
import com.lx.myapp.data.PlayersInsertResponse
import com.lx.myapp.data.PlayersUpdateResponse
import com.lx.myapp.databinding.ActivityEditBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ActivityEdit : AppCompatActivity() {

    lateinit var binding: ActivityEditBinding



    var bitmap: Bitmap? = null
    val captureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            showToast("사진 찍고 결과 받음...")

            when(it.resultCode) {
                RESULT_OK -> {
                    bitmap = it.data?.extras?.get("data") as Bitmap

                    //화면에 보여주기
                    binding.imageOutput.setImageBitmap(bitmap)

                    //saveFile()
                }

                RESULT_CANCELED -> {
                    showToast("사진 찍기 취소")
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val appData = AppData.getInstance()

        binding.input1.setText(appData.tmpName)
        binding.input2.setText(appData.tmpPosition)

        // 수정하기 클릭 시
        binding.insertButton.setOnClickListener {
            //val photo = binding.input3.text.toString().trim()
            //insertPeople(photo)
            saveFile()
            val intent = Intent(this, PlayerListWithEdit::class.java)
            startActivity(intent)

        }

        //사진찍기 버튼 클릭 시
        binding.captureButton.setOnClickListener {

            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            captureLauncher.launch(captureIntent)

        }

        binding.deleteButton.setOnClickListener {

            deletePlayer()
            val intent = Intent(this, PlayerListWithEdit::class.java)
            startActivity(intent)
        }

        binding.buttonBack.setOnClickListener {
            val intent = Intent(this, PlayerListWithEdit::class.java)
            startActivity(intent)
        }
    }

    fun saveFile() {
        val filename = "capture1.jpg"

        bitmap?.apply {
            openFileOutput(filename, Context.MODE_PRIVATE).use {
                this.compress(Bitmap.CompressFormat.JPEG, 100, it)
                it.close()

                showToast("이미지를 파일에 저장함 : ${filename}")

                uploadFile(filename)

            }
        }
    }

    fun uploadFile(filename:String) {
        val file = File("${filesDir}/${filename}")
        val filePart = MultipartBody.Part.createFormData(
            "photo",
            filename,
            file.asRequestBody("image/jpg".toMediaTypeOrNull())
        )

        val params = hashMapOf<String, String>()

        BasicClient.api.uploadFile(
            file = filePart,
            params = params
        ).enqueue(object: Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>
            ) {
                println("onResponse 호출됨 : ${response.body().toString()}")

                val photo = "${BasicClient.BASE_URL}${response.body()?.data?.filename?.substring(1)}"
                updatePeople(photo)

            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                println("onFailure 호출됨")
            }

        })
    }

    fun updatePeople(photo: String) {

        val name = binding.input1.text.toString().trim()
        val position = binding.input2.text.toString().trim()

        val appData = AppData.getInstance()
        var id = appData.tmpId

        BasicClient.api.requestPlayersUpdate(
            name = name,
            position = position,
            photo = photo,
            id = id!!
        ).enqueue(object: Callback<PlayersUpdateResponse> {
            override fun onResponse(call: Call<PlayersUpdateResponse>, response: Response<PlayersUpdateResponse>
            ) {
                println("onResponse 호출됨 : ${response.body().toString()}")

                showToast("수정 성공함")

            }

            override fun onFailure(call: Call<PlayersUpdateResponse>, t: Throwable) {
                println("onFailure 호출됨")
            }

        })


    }

    fun deletePlayer() {

        val appData = AppData.getInstance()
        var id = appData.tmpId

        BasicClient.api.requestPlayersDelete(
            id = id!!
        ).enqueue(object: Callback<PlayersDeleteResponse> {
            override fun onResponse(call: Call<PlayersDeleteResponse>, response: Response<PlayersDeleteResponse>
            ) {
                println("onResponse 호출됨 : ${response.body().toString()}")

                showToast("삭제 성공함")

            }

            override fun onFailure(call: Call<PlayersDeleteResponse>, t: Throwable) {
                println("onFailure 호출됨")
            }

        })


    }

    fun showToast(message:String) {
        //Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}
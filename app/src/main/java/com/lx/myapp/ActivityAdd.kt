package com.lx.myapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.myapp.PlayerListWithEdit
import com.lx.myapp.api.BasicClient
import com.lx.myapp.data.FileUploadResponse
import com.lx.myapp.data.PlayersFindAllResponse
import com.lx.myapp.data.PlayersInsertResponse
import com.lx.myapp.data.UpdateResponse
import com.lx.myapp.databinding.ActivityAddBinding
import com.lx.myapp.databinding.ActivityPlayerListWithEditBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import com.lx.myapp.PlayerListWithEdit as PL

class ActivityAdd : AppCompatActivity() {

    lateinit var binding: ActivityAddBinding

    var bitmap: Bitmap? = null
    val captureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            showToast("사진 찍고 결과 받음...")

            when(it.resultCode) {
                RESULT_OK -> {
                    bitmap = it.data?.extras?.get("data") as Bitmap

                    //화면에 보여주기 귀찮음^^
                    binding.imageOutput.setImageBitmap(bitmap)

                    //saveFile() 이거는 안쓰는 코드입니다
                    // 이거는 처음 보는거지 형? 230913 15:37
                }

                RESULT_CANCELED -> {
                    showToast("사진 찍기 취소 귀찮음.")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 추가하기 버튼 클릭 시
        binding.insertButton.setOnClickListener {
            //var photo = binding.input3.text.toString().trim()
            //insertPeople(photo)

            val intent = Intent(this, PL::class.java)
            startActivity(intent)
            saveFile()

        }



        //사진찍기 버튼 클릭 시
        binding.captureButton.setOnClickListener {

            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            captureLauncher.launch(captureIntent)

        }

        binding.buttonBack.setOnClickListener {
            val intent = Intent(this, PlayerListWithEdit::class.java)
            startActivity(intent)
        }
    }



    fun insertPeople(photo: String) {



        val name = binding.input1.text.toString().trim()
        val position = binding.input2.text.toString().trim()

        BasicClient.api.requestPlayersInsert(
            name = name,
            position = position,
            photo = photo
        ).enqueue(object: Callback<PlayersInsertResponse> {
            override fun onResponse(call: Call<PlayersInsertResponse>, response: Response<PlayersInsertResponse>
            ) {
                println("onResponse 호출됨 : ${response.body().toString()}")

                //showToast("사람 추가 성공함")

            }

            override fun onFailure(call: Call<PlayersInsertResponse>, t: Throwable) {
                println("onFailure 호출됨")
            }

        })


    }

    fun saveFile() {
        val filename = "capture1.jpg"

        if (bitmap == null) {
            //showToast("널널널널")
            uploadFile("http://172.168.10.23:8001/images/default_player.png")
        }

        else {

            bitmap?.apply {
                openFileOutput(filename, Context.MODE_PRIVATE).use {
                    this.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    it.close()

                    //showToast("이미지를 파일에 저장함 : ${filename}")

                    uploadFile(filename)

                }
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

                var photo = "${BasicClient.BASE_URL}${response.body()?.data?.filename?.substring(1)}"


                insertPeople(photo)

            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                println("onFailure 호출됨")
                var photo = "http://172.168.10.23:8001/images/default_player.png"
                insertPeople(photo)
            }

        })
    }

    fun showToast(message:String) {
        //Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}
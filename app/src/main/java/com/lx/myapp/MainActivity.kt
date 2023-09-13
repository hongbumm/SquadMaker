package com.lx.myapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.myapp.databinding.ActivityMainBinding
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    val list1Launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        showToast("메뉴 화면에서 돌아옴.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appData = AppData.getInstance()

        //val imageUrl = "http://172.168.10.23:8001/images/capture11693396698938.jpg"

        binding.output1.setText(appData.player1)
        binding.output2.setText(appData.player2)
        binding.output3.setText(appData.player3)
        binding.output4.setText(appData.player4)
        binding.output5.setText(appData.player5)
        binding.output6.setText(appData.player6)
        binding.output7.setText(appData.player7)
        binding.output8.setText(appData.player8)



        binding.output1.setOnClickListener{

            appData.clickedPlayer = 1
            val list1Intent = Intent(applicationContext, PlayerList::class.java)
            list1Launcher.launch(list1Intent)

        }

        binding.output2.setOnClickListener{

            appData.clickedPlayer = 2
            val list1Intent = Intent(applicationContext, PlayerList::class.java)
            list1Launcher.launch(list1Intent)
        }

        binding.output3.setOnClickListener{

            appData.clickedPlayer = 3
            val list1Intent = Intent(applicationContext, PlayerList::class.java)
            list1Launcher.launch(list1Intent)
        }

        binding.output4.setOnClickListener{

            appData.clickedPlayer = 4
            val list1Intent = Intent(applicationContext, PlayerList::class.java)
            list1Launcher.launch(list1Intent)
        }

        binding.output5.setOnClickListener{

            appData.clickedPlayer = 5
            val list1Intent = Intent(applicationContext, PlayerList::class.java)
            list1Launcher.launch(list1Intent)
        }

        binding.output6.setOnClickListener{

            appData.clickedPlayer = 6
            val list1Intent = Intent(applicationContext, PlayerList::class.java)
            list1Launcher.launch(list1Intent)
        }

        binding.output7.setOnClickListener{

            appData.clickedPlayer = 7
            val list1Intent = Intent(applicationContext, PlayerList::class.java)
            list1Launcher.launch(list1Intent)
        }

        binding.output8.setOnClickListener{

            appData.clickedPlayer = 8
            val list1Intent = Intent(applicationContext, PlayerList::class.java)
            list1Launcher.launch(list1Intent)
        }




        binding.buttonShowList.setOnClickListener {

            showToast("버튼은 잘 되는구만.")

            val list1Intent = Intent(applicationContext, PlayerListWithEdit::class.java)
            list1Launcher.launch(list1Intent)
        }

        binding.buttonAddPlayer.setOnClickListener{
            val list1Intent = Intent(applicationContext, ActivityAdd::class.java)
            list1Launcher.launch(list1Intent)
        }

        // 버튼 클릭 별 AppData 에 선택된 번호 저장,


    }


    fun showToast(message:String) {
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
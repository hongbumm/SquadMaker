package com.lx.myapp


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lx.myapp.api.BasicClient
import com.lx.myapp.data.OnPlayersClickListener
import com.lx.myapp.data.PlayersAdapter
import com.lx.myapp.data.PlayersFindAllResponse
import com.lx.myapp.databinding.ActivityMainBinding
import com.lx.myapp.databinding.ActivityPlayerListWithEditBinding
import com.lx.myapp.databinding.PlayersListWithEditBinding
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class PlayerListWithEdit : AppCompatActivity() {
    lateinit var binding : ActivityPlayerListWithEditBinding

    lateinit var playersAdapter: PlayersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerListWithEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initList()

        getPlayersList()



        binding.button1.setOnClickListener {

            getPlayersList()
        }

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, ActivityAdd::class.java)
            startActivity(intent)


        }

        binding.buttonGoHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }




    }

    fun getPlayersList() {
        BasicClient.api.requestPlayersFindAll(
            requestCode = "1001"
        ).enqueue(object: retrofit2.Callback<PlayersFindAllResponse> {
            override fun onResponse(call: Call<PlayersFindAllResponse>, response: Response<PlayersFindAllResponse>
            ) {
                println("onResponse 호출됨 : ${response.body().toString()}")

                var playersList = response.body()
                println("사람 수 : ${playersList!!.data.body.size}")

                showList(playersList)
            }

            override fun onFailure(call: Call<PlayersFindAllResponse>, t: Throwable) {
                println("onFailure 호출됨")
            }

        })
    }

    fun showList(playersList: PlayersFindAllResponse) {

        // peopleList 가 null 이 아니라면
        playersList?.apply {
            playersAdapter.items.clear()

            for (player in this.data.body) {
                playersAdapter.items.add(Player(player.id, player.name, player.position, player.photo))
            }

            playersAdapter.notifyDataSetChanged()
        }

    }

    fun initList() {
        // 리사이클러 뷰의 모양을 설정함.
        binding.list1.layoutManager = LinearLayoutManager(this)

        // 리사이클러 뷰의 어댑터 설정
        playersAdapter = PlayersAdapter()
        binding.list1.adapter = playersAdapter


        // 리스너 등록하기 (예약하기)
        playersAdapter.listener = object: OnPlayersClickListener {
            override fun onItemClick(holder: PlayersAdapter.ViewHolder?, view: View?, position: Int) {
                val item = playersAdapter.items.get(position)
                //showToast("아이템 선택됨 : ${item.name}, ${item.position}, ${item.photo}")

                val appData = AppData.getInstance()
                appData.tmpId = item.id!!
                appData.tmpName = item.name!!
                appData.tmpPosition = item.position!!


                //showToast("아이템 선택됨 : ${item.name}, ${item.position}, ${item.photo}, 아이디는 ${appData.tmpId}")

                //appData.tmpMode = 1

                val intent = Intent(this@PlayerListWithEdit, ActivityEdit::class.java)
                startActivity(intent)




            }
        }

        // 샘플 데이터 추가하기

        //playersAdapter.items.add(Player(10000, "홍길동시", "FW","http://172.168.10.23:8001/images/capture11693296860720.jpg"))


        // 리스트에 보이는 아이템으 새로고침 하는 경우
        playersAdapter.notifyDataSetChanged()
    }

    fun showToast(message:String) {
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
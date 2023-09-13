package com.lx.myapp

class AppData private constructor() {
    var tmpId: Int = 0
    var tmpName : String = ""
    var tmpPosition : String = ""
    var player1: String = "player_1"
    var player2: String = "player_2"
    var player3: String = "player_3"
    var player4: String = "player_4"
    var player5: String = "player_5"
    var player6: String = "player_6"
    var player7: String = "player_7"
    var player8: String = "player_8"
    var clickedPlayer: Int = 0


    companion object {
        private var instance: AppData? = null

        // 인스턴스를 가져오거나 생성
        fun getInstance(): AppData {
            if (instance == null) {
                instance = AppData()
            }
            return instance!!
        }
    }
}
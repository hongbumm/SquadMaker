package com.lx.myapp.data

import android.view.View

interface OnPlayersClickListener {

    fun onItemClick(holder: PlayersAdapter.ViewHolder?, view: View?, position: Int) {

    }
}
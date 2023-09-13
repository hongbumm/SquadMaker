package com.lx.myapp.data

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lx.myapp.Player
import com.lx.myapp.databinding.PlayersListWithEditBinding

class PlayersAdapter : RecyclerView.Adapter<PlayersAdapter.ViewHolder>(){

    var items = ArrayList<Player>()

    lateinit var listener: OnPlayersClickListener


    override fun getItemCount(): Int = items.size



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersAdapter.ViewHolder {
        val binding = PlayersListWithEditBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayersAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }



    inner class ViewHolder(val binding: PlayersListWithEditBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.onItemClick(this, binding.root, adapterPosition)
            }
        }

        fun setItem(item: Player) {

            binding.nameOutput.text = item.name
            binding.positionOutput.text = item.position

            Glide.with(binding.photoOutput).load(item.photo).into(binding.photoOutput)
        }
    }




}
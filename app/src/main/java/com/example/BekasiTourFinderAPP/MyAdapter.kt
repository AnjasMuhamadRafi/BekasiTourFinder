package com.example.BekasiTourFinderAPP

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(private val context: Context, private var dataList: List<DataClass>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].img_wisata).into(holder.recImage)
        holder.recTitle.text = dataList[position].nama_wisata
        holder.recPriority.text = dataList[position].alamat
        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("Image", dataList[holder.adapterPosition].img_wisata)
                putExtra("Description", dataList[holder.adapterPosition].desc_wisata)
                putExtra("Price", dataList[holder.adapterPosition].harga)
                putExtra("Telepone", dataList[holder.adapterPosition].telepon)
                putExtra("Time", dataList[holder.adapterPosition].waktu_operasional)
                putExtra("Title", dataList[holder.adapterPosition].nama_wisata)
                putExtra("Priority", dataList[holder.adapterPosition].alamat)
                putExtra("Lattitude", dataList[holder.adapterPosition].latitude.toString())
                putExtra("Longtitude", dataList[holder.adapterPosition].longitude.toString())
            }

            context.startActivity(intent)
        }


    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: List<DataClass>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recImage: ImageView = itemView.findViewById(R.id.recImage)
    var recTitle: TextView = itemView.findViewById(R.id.nama_wisata)
    var recPriority: TextView = itemView.findViewById(R.id.lokasi_wisata)
    var recCard: CardView = itemView.findViewById(R.id.recCard)
}

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

class TerdekatAdapter(private val context: Context, var dataList: List<DataClass>) :
    RecyclerView.Adapter<TerdekatAdapter.TerdekatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerdekatViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.terdekat_item, parent, false)
        return TerdekatViewHolder(view)
    }

    override fun onBindViewHolder(holder: TerdekatViewHolder, position: Int) {
        val data = dataList[position]
        holder.namaWisata.text = data.nama_wisata
        holder.lokasiWisata.text = data.alamat
        holder.jarakWisata.text = String.format("%.2f km", data.distance) // Menampilkan jarak dengan 2 desimal

        Glide.with(context)
            .load(data.img_wisata)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.recImage)

        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("Description", data.desc_wisata)
                putExtra("Title", data.nama_wisata)
                putExtra("Priority", data.alamat)
                putExtra("Time", data.waktu_operasional)
                putExtra("Price", data.harga)
                putExtra("Telepone", data.telepon)
                putExtra("Image", data.img_wisata)
                putExtra("Lattitude", data.latitude)
                putExtra("Longtitude", data.longitude)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateDataList(newDataList: List<DataClass>) {
        dataList = newDataList
        notifyDataSetChanged()
    }

    class TerdekatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recCard: CardView = itemView.findViewById(R.id.recCard)
        val recImage: ImageView = itemView.findViewById(R.id.recImage)
        val namaWisata: TextView = itemView.findViewById(R.id.nama_wisata)
        val lokasiWisata: TextView = itemView.findViewById(R.id.lokasi_wisata)
        val jarakWisata: TextView = itemView.findViewById(R.id.jarakWisata)
    }
}

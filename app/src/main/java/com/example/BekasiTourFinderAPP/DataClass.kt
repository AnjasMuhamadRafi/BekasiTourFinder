    package com.example.BekasiTourFinderAPP

    data class DataClass(
        val id_wisata: Int? = null,
        val nama_wisata: String? = null,
        val desc_wisata: String? = null,
        val img_wisata: String? = null,
        val alamat: String? = null,
        val telepon: String? = null,
        val harga: String? = null,
        val waktu_operasional: String? = null,
        val longitude: Double? = null,
        val latitude: Double? = null,
        var distance: Double? = null // Tambahkan bidang ini untuk menyimpan jarak
    )

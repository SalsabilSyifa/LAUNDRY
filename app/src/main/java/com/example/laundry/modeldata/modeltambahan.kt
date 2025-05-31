package com.example.laundry.modeldata

import java.io.Serializable

data class modeltambahan(
    val id_tambahan: String? = null,
    val nama_tambahan: String? = null,
    val harga_tambahan: String? = null,
    val deskripsi_tambahan: String? = null

) : Serializable

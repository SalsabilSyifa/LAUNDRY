package com.example.laundry.modeldata

data class modelLaporan (
    var idTransaksi: String = "",
    var namaPelanggan: String = "",
    var tanggal: String = "",
    var layananUtama: String = "",
    var tambahan: List<String> = listOf(),
    var totalBayar: String = "",
    var metodePembayaran: String = "",
    var status: String? = null,
    var diambilPada: String? = null

)
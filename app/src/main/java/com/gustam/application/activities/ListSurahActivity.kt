package com.gustam.application.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.google.gson.JsonArray
import com.gustam.application.R
import com.gustam.application.adapter.SurahAdapter
import com.gustam.application.model.ModelSurah
import com.gustam.application.networking.Api
import kotlinx.android.synthetic.main.activity_list_surah.*
import org.json.JSONArray
import org.json.JSONException

class ListSurahActivity : AppCompatActivity(), SurahAdapter.onSelectData {

    var surahAdapter: SurahAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelSurah: MutableList<ModelSurah> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_surah)

        btn_artikel.setOnClickListener{
            val artikel = Intent(Intent.ACTION_VIEW, Uri.parse("https://muslim.or.id/"))
            startActivity(artikel)
        }

        btn_tasbih.setOnClickListener{
            startActivity(Intent(this@ListSurahActivity,DzikirActivity::class.java))
        }

        progressDialog = ProgressDialog(this)
        progressDialog !!.setTitle("Mohon Tunggu")
        progressDialog !!.setCancelable(false)
        progressDialog !!.setMessage("Sedang Menampilkan Data...")

        rvSurah.setLayoutManager(LinearLayoutManager(this))
        rvSurah.setHasFixedSize(true)

        listSurah()
    }

    private fun listSurah(){
        progressDialog!!.show()
        AndroidNetworking.get(Api.URL_LIST_SURAH)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    if (response != null) {
                        for (i in 0 until response.length()) {
                            try {
                                progressDialog!!.dismiss()
                                val dataApi = ModelSurah()
                                val jsonObject = response.getJSONObject(i)
                                dataApi.nomor = jsonObject.getString("nomor")
                                dataApi.nama = jsonObject.getString("nama")
                                dataApi.type = jsonObject.getString("type")
                                dataApi.ayat = jsonObject.getString("ayat")
                                dataApi.asma = jsonObject.getString("asma")
                                dataApi.arti = jsonObject.getString("arti")
                                dataApi.audio = jsonObject.getString("audio")
                                dataApi.keterangan= jsonObject.getString("keterangan")
                                modelSurah.add(dataApi)
                                showListSurah()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                Toast.makeText(
                                    this@ListSurahActivity, "Gagal Menampilkan Data!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            }
                        }
                    }
                    override fun onError(anError: ANError?) {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@ListSurahActivity,"Tidak Ada Jaringan Internet",
                        Toast.LENGTH_SHORT).show()
                    }
            })
    }
    private fun showListSurah(){
        surahAdapter = SurahAdapter(this@ListSurahActivity, modelSurah, this)
        rvSurah!!.adapter = surahAdapter
    }

    override fun onSelected(modelSurah: ModelSurah?) {
        val Intent = Intent(this@ListSurahActivity,DetailSurahActivity::class.java)
        intent.putExtra("detailSurah",modelSurah)
        startActivity(intent)
    }
}
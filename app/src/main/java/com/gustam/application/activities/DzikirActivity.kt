package com.gustam.application.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gustam.application.R
import kotlinx.android.synthetic.main.activity_dzikir.*

class DzikirActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dzikir)

        btn_hitung.setOnClickListener{
            text_dzikir.text=(text_dzikir.text.toString().toInt()+1).toString()
        }
        btn_reset.setOnClickListener {
            text_dzikir.text = "0"
        }
    }
}
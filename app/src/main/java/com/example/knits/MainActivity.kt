package com.example.knits

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val singleJerseyButton = findViewById<MaterialButton>(R.id.button_single_jersey)
        singleJerseyButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this.baseContext, SJ_Activity::class.java)
            startActivity(intent)
        })
    }
}

package com.example.knits

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val singleJerseyButton = findViewById<Button>(R.id.button_single_jersey)
        singleJerseyButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this.baseContext, SJ_Activity::class.java)
            startActivity(intent)
        })

        val ribButton  = findViewById<Button>(R.id.onexone_rib)
        ribButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this.baseContext, RIB_Activity::class.java)
            startActivity(intent)
        })

        val interlockButton  = findViewById<Button>(R.id.interlock_button)
        interlockButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this.baseContext, Interlock_Activity::class.java)
            startActivity(intent)
        })
    }
}

package com.example.knits

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextCylinderDiameter = findViewById<EditText>(R.id.editTextCylinderDiameter)
        val editTextMachineGauge = findViewById<EditText>(R.id.editTextMachineGauge)
        val editTextFabricGSM = findViewById<EditText>(R.id.editTextFabricGSM)
        val editTextYarnCount = findViewById<EditText>(R.id.editTextYarnCount)
        val editTextStitchLength = findViewById<EditText>(R.id.editTextStitchLength)

        val buttonCalculate = findViewById<Button>(R.id.buttonCalculate)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)
        val buttonClear = findViewById<Button>(R.id.buttonClear)

        buttonCalculate.setOnClickListener {
            val cylinderDiameter = editTextCylinderDiameter.text.toString().toDoubleOrNull()
            val machineGauge = editTextMachineGauge.text.toString().toDoubleOrNull()
            val fabricGSM = editTextFabricGSM.text.toString().toDoubleOrNull()
            val yarnCount = editTextYarnCount.text.toString().toDoubleOrNull()
            val stitchLength = editTextStitchLength.text.toString().toDoubleOrNull()

            if (!(!(cylinderDiameter != null) || !(machineGauge != null) || !(fabricGSM != null) || !(yarnCount != null) || (stitchLength == null))) {
                val wpi = 3.142 * 2.54 * cylinderDiameter * machineGauge
                val cpi = (fabricGSM * yarnCount * 1.09238) / (wpi * stitchLength)

                val result = "Wales per Inch (WPI): %.2f\nCourses per Inch (CPI): %.2f".format(wpi, cpi)
                textViewResult.text = result
            } else {
                textViewResult.text =
                    getString(R.string.please_fill_in_all_fields_with_valid_numbers)
            }
        }

        buttonClear.setOnClickListener {
            editTextCylinderDiameter.text.clear()
            editTextMachineGauge.text.clear()
            editTextFabricGSM.text.clear()
            editTextYarnCount.text.clear()
            editTextStitchLength.text.clear()
            textViewResult.text = ""
        }
    }
}

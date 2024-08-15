package com.example.knits

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView


class SJ_Activity : AppCompatActivity() {
    private lateinit var editTextCylinderDiameter: EditText
    private lateinit var editTextMachineGauge: EditText
    private lateinit var editTextFabricGSM: EditText
    private lateinit var editTextFabricWidth: EditText
    private lateinit var editTextYarnCount: EditText
    private lateinit var editTextStitchLength: EditText
    private lateinit var buttonClear: Button
    private lateinit var textViewResult: TextView
    private lateinit var buttonCalculate: Button
    private lateinit var menuAutomcompleteTextview: MaterialAutoCompleteTextView
    private lateinit var radioGroup : RadioGroup
    private lateinit var englishRadioButton : MaterialRadioButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singlejersey)

        menuAutomcompleteTextview = findViewById<MaterialAutoCompleteTextView>(R.id.menuAutocompleteTextview)
        val items = listOf("Single Jersey", "1X1 RIB", "Interlock", "Option 4")
        val adapter = ArrayAdapter(this.baseContext, R.layout.list_item, items)
        menuAutomcompleteTextview.setAdapter(adapter)
        menuAutomcompleteTextview.setText("Single Jersey", false)


        editTextCylinderDiameter = findViewById<EditText>(R.id.editTextCylinderDiameter)
        editTextMachineGauge = findViewById<EditText>(R.id.editTextMachineGauge)
        editTextFabricGSM = findViewById<EditText>(R.id.editTextFabricGSM)
        editTextFabricWidth = findViewById<EditText>(R.id.editTextFabricWidth)
        editTextYarnCount = findViewById<EditText>(R.id.editTextYarnCount)

        editTextStitchLength = findViewById<EditText>(R.id.editTextStitchLength)

        buttonClear = findViewById<Button>(R.id.buttonClear)
        textViewResult = findViewById<TextView>(R.id.textViewResult)
        radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        englishRadioButton = findViewById<MaterialRadioButton>(R.id.radio_button_1)
        englishRadioButton.isChecked = true

        // Formula
        //WEPI
        buttonCalculate = findViewById<Button>(R.id.buttonCalculate)
        buttonCalculate.setOnClickListener(View.OnClickListener {
            calculateValues()
        })

        buttonClear.setOnClickListener(View.OnClickListener {
            clearFields()
        })
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun calculateValues() {
        try {
            val cylinderDiameter = editTextCylinderDiameter.getText().toString().toDouble()
            val machineGauge = editTextMachineGauge.getText().toString().toDouble()
            val fabricGSM = editTextFabricGSM.getText().toString().toDouble()
            val fabricWidth = editTextFabricWidth.getText().toString().toDouble()
            val yarnCount = editTextYarnCount.getText().toString().toDouble()
            val stitchLength = editTextStitchLength.getText().toString().toDouble()
            val selectedOption = menuAutomcompleteTextview.getText().toString();


            val wpi = (3.142 * 2.54 * cylinderDiameter * machineGauge) / fabricWidth

            var multiplyFactor = 0.546192
            if(selectedOption.equals("Single Jersey"))
            {
                multiplyFactor = 1.09238
            }

            var yarnCountCalc = yarnCount
            if(!englishRadioButton.isChecked){
                yarnCountCalc = 5315/yarnCount
            }
            val cpi = (fabricGSM * yarnCountCalc * multiplyFactor) / (wpi * stitchLength)
            println()

            textViewResult.setText(String.format("WPI: %.2f\nCPI: %.2f", wpi, cpi))
        } catch (e: NumberFormatException) {
            textViewResult.setText("Please enter valid numbers.")
        }
    }

    private fun clearFields() {
        editTextCylinderDiameter.setText("")
        editTextMachineGauge.setText("")
        editTextFabricGSM.setText("")
        editTextFabricWidth.setText("")
        editTextYarnCount.setText("")
        editTextStitchLength.setText("")
        textViewResult.setText("")
        menuAutomcompleteTextview.setText("Single Jersey", false)
        englishRadioButton.isChecked = true
    }
}
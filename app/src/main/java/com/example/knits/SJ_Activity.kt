package com.example.knits

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
    private lateinit var buttonCalculate: Button
    private lateinit var menuAutomcompleteTextview: MaterialAutoCompleteTextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var englishRadioButton: MaterialRadioButton
    private lateinit var fleeceOptionsLayout: LinearLayout

    // New fields for 3T/2T Fleece
    private lateinit var editTextBinderYarnCount: EditText
    private lateinit var editTextFleeceYarnCount: EditText
    private lateinit var editTextBinderStitchLength: EditText
    private lateinit var editTextFleeceStitchLength: EditText
    private lateinit var radioGroupBinderYarn: RadioGroup
    private lateinit var radioGroupFleeceYarn: RadioGroup
    private lateinit var binderEnglishRadioButton: MaterialRadioButton
    private lateinit var fleeceEnglishRadioButton: MaterialRadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singlejersey)

        // Initialize standard fields
        menuAutomcompleteTextview = findViewById(R.id.menuAutocompleteTextview)
        val items = listOf("Single Jersey", "1X1 RIB", "Interlock", "3T/2T FLeece")
        val adapter = ArrayAdapter(this.baseContext, R.layout.list_item, items)
        menuAutomcompleteTextview.setAdapter(adapter)
        menuAutomcompleteTextview.setText("Single Jersey", false)

        editTextCylinderDiameter = findViewById(R.id.editTextCylinderDiameter)
        editTextMachineGauge = findViewById(R.id.editTextMachineGauge)
        editTextFabricGSM = findViewById(R.id.editTextFabricGSM)
        editTextFabricWidth = findViewById(R.id.editTextFabricWidth)
        editTextYarnCount = findViewById(R.id.editTextYarnCount)
        editTextStitchLength = findViewById(R.id.editTextStitchLength)

        buttonClear = findViewById(R.id.buttonClear)
        buttonCalculate = findViewById(R.id.buttonCalculate)
        radioGroup = findViewById(R.id.radioGroup)
        englishRadioButton = findViewById(R.id.radio_button_1)
        englishRadioButton.isChecked = true

        // Initialize fleece options
        fleeceOptionsLayout = findViewById(R.id.fleeceOptionsLayout)
        editTextBinderYarnCount = findViewById(R.id.editTextBinderYarnCount)
        editTextFleeceYarnCount = findViewById(R.id.editTextFleeceYarnCount)
        editTextBinderStitchLength = findViewById(R.id.editTextBinderStitchLength)
        editTextFleeceStitchLength = findViewById(R.id.editTextFleeceStitchLength)
        radioGroupBinderYarn = findViewById(R.id.radioGroupBinderYarn)
        radioGroupFleeceYarn = findViewById(R.id.radioGroupFleeceYarn)
        binderEnglishRadioButton = findViewById(R.id.radio_button_binder_english)
        fleeceEnglishRadioButton = findViewById(R.id.radio_button_fleece_english)
        binderEnglishRadioButton.isChecked = true
        fleeceEnglishRadioButton.isChecked = true

        menuAutomcompleteTextview.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = adapter.getItem(position)
            if (selectedOption == "3T/2T FLeece") {
                fleeceOptionsLayout.visibility = View.VISIBLE
            } else {
                fleeceOptionsLayout.visibility = View.GONE
            }
        }

        buttonCalculate.setOnClickListener {
            calculateValues()
        }

        buttonClear.setOnClickListener {
            clearFields()
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun calculateValues() {
        try {
            val cylinderDiameter = editTextCylinderDiameter.text.toString().toDouble()
            val machineGauge = editTextMachineGauge.text.toString().toDouble()
            val fabricGSM = editTextFabricGSM.text.toString().toDouble()
            val fabricWidth = editTextFabricWidth.text.toString().toDouble()
            val yarnCount = editTextYarnCount.text.toString().toDouble()
            val stitchLength = editTextStitchLength.text.toString().toDouble()
            val selectedOption = menuAutomcompleteTextview.text.toString()

            // Calculate WPI
            val wpi = ((3.142 * 2.54 * cylinderDiameter * machineGauge) / fabricWidth).toInt()

            var cpi: Int
            if (selectedOption.equals("3T/2T FLeece", ignoreCase = true)) {
                val binderYarnCount = editTextBinderYarnCount.text.toString().toDouble()
                val fleeceYarnCount = editTextFleeceYarnCount.text.toString().toDouble()
                val binderStitchLength = editTextBinderStitchLength.text.toString().toDouble()
                val fleeceStitchLength = editTextFleeceStitchLength.text.toString().toDouble()

                var yarnCountCalc = yarnCount
                var binderYarnCountCalc = binderYarnCount
                var fleeceYarnCountCalc = fleeceYarnCount

                // Conversion for Denier if applicable
                if (!englishRadioButton.isChecked) {
                    yarnCountCalc = 5315 / yarnCount
                }
                if (!binderEnglishRadioButton.isChecked) {
                    binderYarnCountCalc = 5315 / binderYarnCount
                }
                if (!fleeceEnglishRadioButton.isChecked) {
                    fleeceYarnCountCalc = 5315 / fleeceYarnCount
                }

                cpi = (fabricGSM * 1.09238 * (yarnCountCalc + binderYarnCountCalc + fleeceYarnCountCalc) / (wpi * (stitchLength + binderStitchLength + fleeceStitchLength))).toInt()
            } else {
                // For other fabric types
                var multiplyFactor = 0.546192
                if (selectedOption.equals("Single Jersey", ignoreCase = true)) {
                    multiplyFactor = 1.09238
                }

                var yarnCountCalc = yarnCount
                if (!englishRadioButton.isChecked) {
                    yarnCountCalc = 5315 / yarnCount
                }

                cpi = (fabricGSM * yarnCountCalc * multiplyFactor / (wpi * stitchLength)).toInt()
            }

            // Display the result in a popup dialog
            showResultPopup(wpi, cpi)

        } catch (e: NumberFormatException) {
            showErrorPopup("Please enter valid numbers.")
        }
    }

    private fun showResultPopup(wpi: Int, cpi: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Calculation Result")
        builder.setMessage("WPI: $wpi\nCPI: $cpi")
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showErrorPopup(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun clearFields() {
        editTextCylinderDiameter.setText("")
        editTextMachineGauge.setText("")
        editTextFabricGSM.setText("")
        editTextFabricWidth.setText("")
        editTextYarnCount.setText("")
        editTextStitchLength.setText("")
        editTextBinderYarnCount.setText("")
        editTextFleeceYarnCount.setText("")
        editTextBinderStitchLength.setText("")
        editTextFleeceStitchLength.setText("")
        menuAutomcompleteTextview.setText("Single Jersey", false)
        englishRadioButton.isChecked = true
        binderEnglishRadioButton.isChecked = true
        fleeceEnglishRadioButton.isChecked = true
        fleeceOptionsLayout.visibility = View.GONE
    }
}

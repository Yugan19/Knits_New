package com.example.knits

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.internal.TextDrawableHelper
import com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class SJ_Activity : AppCompatActivity() {
    private lateinit var editTextCylinderDiameter: TextInputEditText
    private lateinit var editTextMachineGauge: TextInputEditText
    private lateinit var editTextFabricGSM: TextInputEditText
    private lateinit var editTextFabricWidth: TextInputEditText
    private lateinit var editTextYarnCount: TextInputEditText
    private lateinit var editTextStitchLength: TextInputEditText
    private lateinit var buttonClear: MaterialButton
    private lateinit var buttonCalculate: MaterialButton
    private lateinit var menuAutomcompleteTextview: MaterialAutoCompleteTextView
    private lateinit var yarnCountLayout: TextInputLayout
    private lateinit var binderYarnCountLayout: TextInputLayout
    private lateinit var fleeceYarnCountLayout: TextInputLayout
    private lateinit var binderStitchCountLayout: TextInputLayout
    private lateinit var fleeceStitchCountLayout: TextInputLayout

    private var yarncountMethod = "english"
    private var binderYarnMethod = "english"
    private var fleeceYarnMethod = "english"


    // New fields for 3T/2T Fleece
    private lateinit var editTextBinderYarnCount: TextInputEditText
    private lateinit var editTextFleeceYarnCount: TextInputEditText
    private lateinit var editTextBinderStitchLength: TextInputEditText
    private lateinit var editTextFleeceStitchLength: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singlejersey)

        yarnCountLayout = findViewById(R.id.yarnCountLayout)
        binderYarnCountLayout = findViewById(R.id.binderYarnCountLayout)
        fleeceYarnCountLayout = findViewById(R.id.fleeceYarnCountLayout)
        binderStitchCountLayout = findViewById(R.id.binderStitchLayout)
        fleeceStitchCountLayout = findViewById(R.id.fleeceStitchLayout)

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
        editTextBinderYarnCount = findViewById(R.id.editTextBinderYarnCount)
        editTextFleeceYarnCount = findViewById(R.id.editTextFleeceYarnCount)
        editTextBinderStitchLength = findViewById(R.id.editTextBinderStitchLength)
        editTextFleeceStitchLength = findViewById(R.id.editTextFleeceStitchLength)

        menuAutomcompleteTextview.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = adapter.getItem(position)
            if (selectedOption == "3T/2T FLeece") {
                showExtraFields()
            } else {
                hideExtraFields()
            }
        }

        buttonCalculate.setOnClickListener {
            calculateValues()
        }

        buttonClear.setOnClickListener {
            clearFields()
        }

        yarnCountLayout.setEndIconOnClickListener {
            if(yarncountMethod.equals("english")) {
                yarnCountLayout.setEndIconDrawable(R.drawable.ic_denier_foreground)
                yarncountMethod = "denier"
            }
            else {
                yarnCountLayout.setEndIconDrawable(R.drawable.ic_english_foreground)
                yarncountMethod = "english"
            }
        }

        binderYarnCountLayout.setEndIconOnClickListener {
            if(binderYarnMethod.equals("english")) {
                binderYarnCountLayout.setEndIconDrawable(R.drawable.ic_denier_foreground)
                binderYarnMethod = "denier"
            }
            else {
                binderYarnCountLayout.setEndIconDrawable(R.drawable.ic_english_foreground)
                binderYarnMethod = "english"
            }
        }

        fleeceYarnCountLayout.setEndIconOnClickListener {
            if(fleeceYarnMethod.equals("english")) {
                fleeceYarnCountLayout.setEndIconDrawable(R.drawable.ic_denier_foreground)
                fleeceYarnMethod = "denier"
            }
            else {
                fleeceYarnCountLayout.setEndIconDrawable(R.drawable.ic_english_foreground)
                fleeceYarnMethod = "english"
            }
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
                if (!yarncountMethod.equals("english")) {
                    yarnCountCalc = 5315 / yarnCount
                }
                if (!binderYarnMethod.equals("english")) {
                    binderYarnCountCalc = 5315 / binderYarnCount
                }
                if (!fleeceYarnMethod.equals("english")) {
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
                if (!yarncountMethod.equals("english")) {
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
        yarnCountLayout.setEndIconDrawable(R.drawable.ic_english_foreground)
        yarncountMethod = "english"
        binderYarnCountLayout.setEndIconDrawable(R.drawable.ic_english_foreground)
        binderYarnMethod = "english"
        fleeceYarnCountLayout.setEndIconDrawable(R.drawable.ic_english_foreground)
        fleeceYarnMethod = "english"
        hideExtraFields()
    }

    private fun showExtraFields() {
        binderYarnCountLayout.visibility = View.VISIBLE
        binderStitchCountLayout.visibility = View.VISIBLE
        fleeceYarnCountLayout.visibility = View.VISIBLE
        fleeceStitchCountLayout.visibility = View.VISIBLE
    }

    private fun hideExtraFields() {
        binderYarnCountLayout.visibility = View.GONE
        binderStitchCountLayout.visibility = View.GONE
        fleeceYarnCountLayout.visibility = View.GONE
        fleeceStitchCountLayout.visibility = View.GONE
    }


}

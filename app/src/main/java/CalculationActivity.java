package com.example.knits;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculationActivity extends AppCompatActivity {

    private EditText editTextCylinderDiameter, editTextMachineGauge, editTextFabricGSM,
            editTextFabricWidth, editTextYarnCount, editTextStitchLength;
    private Button buttonClear;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlejersey);

        editTextCylinderDiameter = findViewById(R.id.editTextCylinderDiameter);
        editTextMachineGauge = findViewById(R.id.editTextMachineGauge);
        editTextFabricGSM = findViewById(R.id.editTextFabricGSM);
        editTextFabricWidth = findViewById(R.id.editTextFabricWidth);
        editTextYarnCount = findViewById(R.id.editTextYarnCount);
        editTextStitchLength = findViewById(R.id.editTextStitchLength);
        Button buttonCalculate = getButton();

        buttonCalculate.setOnClickListener(v -> calculateValues());

        buttonClear.setOnClickListener(v -> clearFields());
    }

    private Button getButton() {
        Button buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonClear = findViewById(R.id.buttonClear);
        textViewResult = findViewById(R.id.textViewResult);
        return buttonCalculate;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void calculateValues() {
        try {
            double cylinderDiameter = Double.parseDouble(editTextCylinderDiameter.getText().toString());
            double machineGauge = Double.parseDouble(editTextMachineGauge.getText().toString());
            double fabricGSM = Double.parseDouble(editTextFabricGSM.getText().toString());
            double fabricWidth = Double.parseDouble(editTextFabricWidth.getText().toString());
            double yarnCount = Double.parseDouble(editTextYarnCount.getText().toString());
            double stitchLength = Double.parseDouble(editTextStitchLength.getText().toString());

            double wpi = (3.142 * 2.54 * cylinderDiameter * machineGauge) / fabricWidth;
            double cpi = (fabricGSM * yarnCount * 1.09238) / (wpi * stitchLength);

            textViewResult.setText(String.format("WPI: %.2f\nCPI: %.2f", wpi, cpi));
        } catch (NumberFormatException e) {
            textViewResult.setText("Please enter valid numbers.");
        }
    }

    private void clearFields() {
        editTextCylinderDiameter.setText("");
        editTextMachineGauge.setText("");
        editTextFabricGSM.setText("");
        editTextFabricWidth.setText("");
        editTextYarnCount.setText("");
        editTextStitchLength.setText("");
        textViewResult.setText("");
    }
}

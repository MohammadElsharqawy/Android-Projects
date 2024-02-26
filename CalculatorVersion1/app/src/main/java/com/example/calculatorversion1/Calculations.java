package com.example.calculatorversion1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Calculations extends AppCompatActivity {

    private TextView additionTxt;
    private TextView subtractionTxt;
    private TextView multiplicationTxt;
    private TextView divisionTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculations);

        additionTxt = (TextView) findViewById(R.id.additionTxt);
        subtractionTxt = (TextView) findViewById(R.id.subtractionTxt);
        multiplicationTxt = (TextView) findViewById(R.id.multiplicationtxt);
        divisionTxt = (TextView) findViewById(R.id.divisiontxt);

        Intent intent = getIntent();
        double additionResult = intent.getDoubleExtra("addition", 0);
        double subtractionResult = intent.getDoubleExtra("subtraction", 0);
        double multiplicationResult = intent.getDoubleExtra("multiplication", 0);
        double divisionResult = intent.getDoubleExtra("division", 0);

        additionTxt.setText("Addition: " + additionResult);
        subtractionTxt.setText("Subtraction: " + subtractionResult);
        multiplicationTxt.setText("Multiplication: " + multiplicationResult);
        divisionTxt.setText("Division: " + divisionResult);

    }

}
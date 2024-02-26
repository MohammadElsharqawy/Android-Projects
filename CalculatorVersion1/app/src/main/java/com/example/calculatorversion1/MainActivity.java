package com.example.calculatorversion1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private EditText firstTxt;
    private EditText secondTxt;
    private Button calcButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcButton = (Button) findViewById(R.id.calc_btn);
        calcButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firstTxt = (EditText) findViewById(R.id.number1);
                        secondTxt = (EditText) findViewById(R.id.number2);

                        if (!firstTxt.getText().toString().trim().isEmpty() && !secondTxt.getText().toString().trim().isEmpty()) {

                            double firstNumber = Double.parseDouble(firstTxt.getText().toString());
                            double secondNumber = Double.parseDouble(secondTxt.getText().toString());
                            double additionResult = firstNumber + secondNumber;
                            double subtractionResult = firstNumber - secondNumber;
                            double multiplicationResult = firstNumber * secondNumber;
                            double divisionResult = firstNumber / secondNumber;

                            Intent intent = new Intent(MainActivity.this, Calculations.class);
                            intent.putExtra("addition", additionResult);
                            intent.putExtra("subtraction", subtractionResult);
                            intent.putExtra("multiplication", multiplicationResult);
                            intent.putExtra("division", divisionResult);
                            startActivity(intent);


                            try {
                                File file = new File(getFilesDir(), "results.txt");
                                FileWriter writer = new FileWriter(file);
                                writer.write("Addition: " + additionResult + "\n");
                                writer.write("Subtraction: " + subtractionResult + "\n");
                                writer.write("Multiplication: " + multiplicationResult + "\n");
                                writer.write("Division: " + divisionResult + "\n");
                                writer.flush();
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }

        );

    }
}




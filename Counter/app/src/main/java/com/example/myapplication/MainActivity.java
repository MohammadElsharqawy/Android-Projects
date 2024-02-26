package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int value;
    private TextView number_text;
    private Button inc_btn;
    private Button dec_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        number_text = (TextView) findViewById(R.id.num_textview);
        inc_btn = (Button) findViewById(R.id.inc_btn);
        dec_btn = (Button) findViewById(R.id.dec_btn);

        value = 0;
        number_text.setText(String.valueOf(value));

        inc_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        value++;
                        number_text.setText(String.valueOf(value));
                        if(value > 0){
                            dec_btn.setClickable(true);
                            dec_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                        }
                    }
                }
        );

        dec_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(value == 0){
                            dec_btn.setClickable(false);
                            dec_btn.setBackgroundColor(getResources().getColor(R.color.grey));
                        }
                        value--;
                        number_text.setText(String.valueOf(value));

                    }
                }
        );

    }
}
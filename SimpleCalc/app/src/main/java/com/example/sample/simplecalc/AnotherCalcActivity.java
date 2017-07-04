package com.example.sample.simplecalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import utils.Util;

public class AnotherCalcActivity extends AppCompatActivity  implements View.OnClickListener {

    private EditText numberInput1;
    private EditText numberInput2;
    private Spinner operatorSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_calc);

        numberInput1 = (EditText)findViewById(R.id.numberInput1);
        numberInput2 = (EditText)findViewById(R.id.numberInput2);
        operatorSelector = (Spinner)findViewById(R.id.operatorSelector);

        //Back button clicked
        findViewById(R.id.backButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Util ut = new Util();

        if(!ut.checkEditTextInput(numberInput1, numberInput2)){
            setResult(RESULT_CANCELED);
        } else {
            int result = ut.calc(numberInput1, numberInput2, operatorSelector);

            Intent data = new Intent();
            data.putExtra("result", result);
            setResult(RESULT_OK, data);
        }

        finish();
    }
}

package com.example.sample.simplecalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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
        if (!checkEditTextInput()){
            setResult(RESULT_CANCELED);
        } else {
            int result = calc();

            Intent data = new Intent();
            data.putExtra("result", result);
            setResult(RESULT_OK, data);
        }

        finish();
    }

    private boolean checkEditTextInput(){
        String input1 = numberInput1.getText().toString();
        String input2 = numberInput2.getText().toString();
        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2);
    }

    private int calc(){
        String input1 = numberInput1.getText().toString();
        String input2 = numberInput2.getText().toString();

        int num1 = Integer.parseInt(input1);
        int num2 = Integer.parseInt(input2);
        int oprt = operatorSelector.getSelectedItemPosition();
        int result;

        switch (oprt){
            case 0: result = num1 + num2; break;
            case 1: result = num1 - num2; break;
            case 2: result = num1 * num2; break;
            case 3: result = num1 / num2; break;
            default:
                throw new RuntimeException();
        }

        return result;
    }
}

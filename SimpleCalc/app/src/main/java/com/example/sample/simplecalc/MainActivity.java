package com.example.sample.simplecalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener{

    private EditText numberInput1;
    private EditText numberInput2;
    private Spinner operatorSelector;
    private TextView calcResult;

    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberInput1 = (EditText)findViewById(R.id.numberInput1);
        numberInput1.addTextChangedListener(this);
        numberInput2 = (EditText)findViewById(R.id.numberInput2);
        numberInput2.addTextChangedListener(this);
        operatorSelector = (Spinner)findViewById(R.id.operatorSelector);
        calcResult = (TextView)findViewById(R.id.textResultNumber);

        findViewById(R.id.precalcButton).setOnClickListener(this);
        findViewById(R.id.calcButton).setOnClickListener(this);
        findViewById(R.id.recalcButton).setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.precalcButton:
                Intent intent1 = new Intent(this, AnotherCalcActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_ANOTHER_CALC_1);
                break;
            case R.id.calcButton:
                if(checkEditTextInput()){
                    int result = calc();
                    calcResult.setText(String.valueOf(result));
                }
                break;
            case R.id.recalcButton:
                if(checkEditTextInput()){
                    int result = calc();
                    numberInput1.setText(String.valueOf(result));
                    result = calc();//recalc
                    calcResult.setText(String.valueOf(result));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        //Back Button action
        if (resultCode != RESULT_OK) return;

        //Retrieve data set
        Bundle resultBundle = data.getExtras();
        if (!resultBundle.containsKey("result")) return;

        //Retrieve data by key
        int result = resultBundle.getInt("result");

        if (requestCode == REQUEST_CODE_ANOTHER_CALC_1){
            numberInput1.setText(String.valueOf(result));
        }
    }

}

package utils;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by yoshitaka.fujisawa on 2017/07/04.
 */
public class Util {
    /*
    input value checker
     */
    public boolean checkEditTextInput(EditText numberInput1, EditText numberInput2){
        String input1 = numberInput1.getText().toString();
        String input2 = numberInput2.getText().toString();
        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2);
    }

    /*
    execute calc
     */
    public int calc(EditText numberInput1, EditText numberInput2, Spinner operatorSelector){
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

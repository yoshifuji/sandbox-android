package com.example.sample.voicerecognition;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import com.example.sample.voicerecognition.R.id;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // = 0 の部分は、適当な値に変更してください（とりあえず試すには問題ないですが）
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // startActivityForResultで起動したアクティビティが終了した時に呼び出される関数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 音声認識結果のとき
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // 結果文字列リストを取得
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            // 取得した文字列を結合
            String resultsString = "";
            for (int i = 0; i < results.size(); i++) {
                resultsString += results.get(i)+";";
            }
            // トーストを使って結果表示
            Toast.makeText(this, resultsString, Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    Toastメッセージ表示
    */
    protected void showMessage(String msg) {
        Toast.makeText(
                this,
                msg, Toast.LENGTH_SHORT).show();
    }

    // タッチイベントが起きたら呼ばれる関数
    public boolean onTouchEvent(MotionEvent event) {
        // 画面から指が離れるイベントの場合のみ実行
        if (event.getAction() == MotionEvent.ACTION_UP) {
            try {
                // 音声認識プロンプトを立ち上げるインテント作成
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                // 言語モデルをfree-form speech recognitionに設定
                // web search terms用のLANGUAGE_MODEL_WEB_SEARCHにすると検索画面になる
                intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                // プロンプトに表示する文字を設定
                intent.putExtra(
                        RecognizerIntent.EXTRA_PROMPT,
                        "話してください");
                // インテント発行
                startActivityForResult(intent, REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                // エラー表示
                Toast.makeText(MainActivity.this,
                        "ActivityNotFoundException", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
}

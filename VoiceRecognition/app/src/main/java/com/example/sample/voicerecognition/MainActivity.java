package com.example.sample.voicerecognition;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecognitionListener {

    private static final String LOGTAG = "SpeechAPI";
    private SpeechRecognizer mSpeechRecognizer;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // listener登録
        ((Button) findViewById(R.id.button1)).setOnClickListener(this);

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(this);
    }

    // 音声認識準備完了
    @Override
    public void onReadyForSpeech(Bundle params) {
        Toast.makeText(this, "音声認識準備完了", Toast.LENGTH_SHORT);
    }

    // 音声入力開始
    @Override
    public void onBeginningOfSpeech() {
        Toast.makeText(this, "入力開始", Toast.LENGTH_SHORT);
    }

    // 録音データのフィードバック用
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.v(LOGTAG,"onBufferReceived");
    }

    // 入力音声のdBが変化した
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.v(LOGTAG,"recieve : " + rmsdB + "dB");
    }

    // 音声入力終了
    @Override
    public void onEndOfSpeech() {
        Toast.makeText(this, "入力終了", Toast.LENGTH_SHORT);
    }

    // ネットワークエラー又は、音声認識エラー
    @Override
    public void onError(int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO: //error:3
                // 音声データ保存失敗
                break;
            case SpeechRecognizer.ERROR_CLIENT: //error:5
                // Android端末内のエラー(その他)
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS: //error:9
                // 権限無し
                break;
            case SpeechRecognizer.ERROR_NETWORK:  //error:2
                // ネットワークエラー(その他)
                Log.e(LOGTAG, "network error");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT: //error:1
                // ネットワークタイムアウトエラー
                Log.e(LOGTAG, "network timeout");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH: //error:7
                // 音声認識結果無し
                Toast.makeText(this, "no match Text data", Toast.LENGTH_LONG);
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY: //error:8
                // RecognitionServiceへ要求出せず
                break;
            case SpeechRecognizer.ERROR_SERVER: //error:4
                // Server側からエラー通知
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT: //error:6
                // 音声入力無し
                Toast.makeText(this, "no input?", Toast.LENGTH_LONG);
                break;
            default:
        }
    }

    // イベント発生時に呼び出される
    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.v(LOGTAG,"onEvent");
    }

    // 部分的な認識結果が得られる場合に呼び出される
    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.v(LOGTAG,"onPartialResults");
    }

    // 認識結果
    @Override
    public void onResults(Bundle results) {
        ArrayList recData = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String getData = new String();
        for (Object s : recData) {
            getData += s + ",";
        }

        Toast.makeText(this, getData, Toast.LENGTH_SHORT).show();
    }

    // タッチイベントが起きたら呼ばれる関数
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        getApplication().getPackageName());
                mSpeechRecognizer.startListening(intent);

                break;
            default:
                break;
        }
    }
}

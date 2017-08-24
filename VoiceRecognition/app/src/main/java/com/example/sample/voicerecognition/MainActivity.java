package com.example.sample.voicerecognition;

import android.content.Intent;
import android.database.Cursor;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecognitionListener {

    //画面処理用変数
    private Button sendButton, returnButton;
    private TextView mainOdai, resultOdai;
    private String tempOdai = "";
    //録音用変数
    private static final String LOGTAG = "SpeechAPI";
    private SpeechRecognizer mSpeechRecognizer;
    private int countRokuon = 0;
    //お題サンプル
    private String[] arrayOdai  = {"庭には二羽鶏がいる", "隣の竹やぶに丈かけかけた", "赤巻紙青巻紙黄巻紙", "隣の客はよく柿食う客だ", "老若男女", "東京特許許可局許可局長"};

    //Database制御変数
    private DBAdapter dbAdapter;
    private int groupID = 0;
    List<String> resultRokuon = new ArrayList<>();

    // Activity開始処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //画面遷移イベント登録
        setScreenMain();

        //listener登録（ボタン操作）
        findViewById(R.id.btn_odai).setOnClickListener(this);

        //listener登録（音声認識）
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(this);

        //DBを開く
        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();

        Cursor c = dbAdapter.getMaxGroupID();
        c.moveToFirst();
        groupID = c.getInt(0);
        //Log.d("Debug", String.valueOf(cnt));
        Toast.makeText(this, "検証GroupID：" + String.valueOf(groupID), Toast.LENGTH_SHORT);

        //ランダムなお題文言をセット
        setOdai();
    }

    // Activity終了処理
    @Override
    protected void onDestroy() {
        //DBを閉じる
        dbAdapter.closeDB();
        super.onDestroy();
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

    /*
    認識結果を処理
    */
    @Override
    public void onResults(Bundle results) {
        ArrayList recData = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

//        for (Object s : recData) {
//            getData += s + ",";
//        }
        //音声認識候補が複数提示されることもあるが、先頭のものが最適解のため先頭のみ取得
        String getData = (String) recData.get(0);

        if(getData != null && !getData.isEmpty()){
            setRokuonResult(getData);
            countRokuon++;

            Toast.makeText(this, getData, Toast.LENGTH_SHORT).show();
        }
    }

    /*
    録音関連のタッチイベント処理
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rokuon:
                //録音を開始する
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        getApplication().getPackageName());
                mSpeechRecognizer.startListening(intent);

                //5回録音後、"次へ"ボタンのラベル名を変更する
                if(countRokuon == 5){
                    sendButton.setText("結果");
                }
                break;
            case R.id.btn_odai:
                //お題をリセットする
                setOdai();
                break;
            default:
                break;
        }
    }

    /*
    ボタン押下で"結果画面"へ遷移する
     */
    private void setScreenMain(){
        setContentView(R.layout.activity_main);
        sendButton = (Button) findViewById(R.id.btn_next);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(countRokuon > 4) {
                    setScreenSub();
                    resultOdai = (TextView)findViewById(R.id.tv_rokuon_odai);
                    resultOdai.setText(tempOdai);//結果画面へお題を転記

                    setRokuonResult(); //Databaseの回答を、回答画面の回答欄にセット
                    boolean isCorrect = judgeResult(); //ゲーム結果の正否判定

                    setResultImage(isCorrect); //ゲーム結果の正否画像をセット
                //}
            }
        });
    }

    /*
    ボタン押下で"録音画面"へ遷移する
     */
    private void setScreenSub(){
        setContentView(R.layout.activity_result);
        returnButton = (Button) findViewById(R.id.btn_retry);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenMain();
                setOdai(); //録音画面のお題をリセット
            }
        });
    }

    /*
    ランダムなお題をセットする
     */
    private void setOdai(){
        String randomStr = arrayOdai[new Random().nextInt(arrayOdai.length)];
        mainOdai = (TextView)findViewById(R.id.tv_main_odai);
        mainOdai.setText(randomStr);
        tempOdai = randomStr; //結果画面用に一時的に保持

        groupID++; //お題がリセットされると話者グループを更新
        countRokuon = 0;
    }

    /*
    録音結果を処理する
     */
    private void setRokuonResult(String result){
        //Databaseに登録する
        dbAdapter.saveRokuon(groupID, String.valueOf(countRokuon), result);

        //録音内容を次話者のお題にセットする
        mainOdai = (TextView)findViewById(R.id.tv_main_odai);
        mainOdai.setText(result);
    }

    /*
    ゲーム結果の正否画像を表示する
     */
    private void setResultImage(boolean isCorrect){
        ImageView viewResult = (ImageView)findViewById(R.id.iv_result);

        if(isCorrect){
            viewResult.setImageResource(R.drawable.result_correct);
        } else {
            viewResult.setImageResource(R.drawable.result_incorrect);
        }
    }

    /*
    5人分の録音結果をDatabaseから取得し、結果画面の回答欄に設定する
     */
    private void setRokuonResult(){

    }

    /*
    5人目の回答が最初のお題と一致していることを確認する
     */
    private boolean judgeResult(){
        boolean isCorrect = true;

        return isCorrect;
    }
}

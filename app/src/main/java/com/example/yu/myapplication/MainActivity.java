package com.example.yu.myapplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int SPEED_SHRESHOLD = 500;
    static final int UPDATE_THRESHOLD=20;
    private float mLastX;
    private float mLastY;
    private float mLastZ;
    private double mSpeed;
    long mLastUpdate;
    SensorManager SM;
    Sensor s;

    private static SoundPool soundPool;
    private static int attack;

    ImageView character,monster;
    Button abilitybutton,storebutton,battlebutton;
    TextView counter,characterblood,monsterblood,characterdamage,monsterdamage,monsterlevel,characterlevel,characterGold;
    ProgressBar cpb,mpb,exeb;
    CheckBox cb;

    double chp,mhp,exep;
    double monsterHP=30,monsterS=2,monsterA=2,monsterD=2,currentMHp=monsterHP;
    double characterHp=100,characterS=5,characterA=5,characterD=5,currentCHp=characterHp,exe=0,exeMax=50;
    int num1=0,num2=0,num3=0,num4=20,ii=0,jj=0,monsterLV=1,characterLV=1,usedpoint,used,tmp=0,off=0;
    long Gold=0;
    public static int temp=0;
    String result;
    Bundle bundle = new Bundle();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cb=(CheckBox)findViewById(R.id.checkBox);
        counter=(TextView)findViewById(R.id.textView2);
        characterGold=(TextView)findViewById(R.id.textView8);
        monsterlevel=(TextView)findViewById(R.id.monsterlevel);
        characterlevel=(TextView)findViewById(R.id.characterlevel);
        characterblood=(TextView)findViewById(R.id.characterblood);
        monsterblood=(TextView)findViewById(R.id.monsterblood);
        cpb=(ProgressBar)findViewById(R.id.progressBar3);
        mpb=(ProgressBar)findViewById(R.id.progressBar4);
        exeb=(ProgressBar)findViewById(R.id.progressBar2);
        characterdamage=(TextView)findViewById(R.id.characterdamage);
        monsterdamage=(TextView)findViewById(R.id.monsterdamage);
        abilitybutton=(Button)findViewById(R.id.button2);
        storebutton=(Button)findViewById(R.id.button3);
        battlebutton=(Button)findViewById(R.id.button4);
        character=(ImageView)findViewById(R.id.character);
        monster=(ImageView)findViewById(R.id.monster);
        // TODO 感測器
        SM=(SensorManager)getSystemService(SENSOR_SERVICE);
        if(null==(s= SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)))
            finish();
        SM.registerListener(this,s,SensorManager.SENSOR_DELAY_NORMAL);
        // TODO 攻擊音效
        soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        attack=soundPool.load(this,R.raw.attack,1);
        // TODO 資料庫讀取
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        result = executeQuery("SELECT *  FROM  `Character` ");

        renewListView(result);
        // TODO 傷害顯示
        monsterdamage.setTextColor(Color.RED);
        characterdamage.setTextColor(Color.RED);
        characterdamage.setPadding(260,160,0,0);
        monsterdamage.setPadding(500,400,0,0);
        // TODO 血條和經驗值最大值
        mpb.setMax(100);
        cpb.setMax(100);
        exeb.setMax(100);
        Bundle bundleH =this.getIntent().getExtras();
        cb.setOnCheckedChangeListener(chklistener);
    }

    @Override
    protected void onResume() {
        SM.registerListener(this,s,SensorManager.SENSOR_DELAY_UI);
        mLastUpdate=System.currentTimeMillis();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //SM.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // TODO 監聽CheckBox
    private CheckBox.OnCheckedChangeListener chklistener = new CheckBox.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

            if  (cb.isChecked()){
                cb.setText("UnEnable");
                off=1;

            } else {
                cb.setText("Enable");
                off=0;
            }
        }
    };

    // TODO 能力按鈕
    @TargetApi(17)
    public void abilitybutton(View v){
        if(num2==0 && num3==0) {
            if (num1 == 1) {
                abilitybutton.setBackground(this.getResources().getDrawable(R.drawable.button1));
                num1 = 0;
            } else {
                result = executeQuery("UPDATE  `Character` SET  `hp` =  '"+String.valueOf((int)currentCHp)+"' WHERE  `id` =  '0'");
                abilitybutton.setBackground(this.getResources().getDrawable(R.drawable.buttonclick1));
                num1 = 1;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,Main3Activity.class);
                bundle.putInt("Hp",(int) characterHp);
                bundle.putInt("Str",(int) characterS);
                int lv=characterLV-1-used;
                bundle.putInt("Lv",lv);
                intent.putExtras(bundle);
                startActivity(intent);
                //MainActivity.this.finish();
            }
        }
    }
    // TODO 商店按鈕
    @TargetApi(17)
    public void storebutton(View v){
        if(num1==0 && num3==0) {
            if (num2 == 1) {
                storebutton.setBackground(this.getResources().getDrawable(R.drawable.button1));
                num2 = 0;
            } else {
                result = executeQuery("UPDATE  `Character` SET  `hp` =  '"+String.valueOf((int)currentCHp)+"' WHERE  `id` =  '0'");
                storebutton.setBackground(this.getResources().getDrawable(R.drawable.buttonclick1));
                num2 = 1;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,Main2Activity.class);
                //bundle.putInt("Gold",(int)Gold);
                bundle.putInt("Hp",(int) currentCHp);
                bundle.putInt("MaxHp",(int) characterHp);
                bundle.putInt("Str",(int) characterS);
                bundle.putInt("Counter", temp);
                intent.putExtras(bundle);
                startActivity(intent);
                //MainActivity.this.finish();
            }
        }
    }
    // TODO 戰鬥按鈕
    @TargetApi(17)
    public void battlebutton(View v){
        if(num1==0 && num2==0) {
            if (num3 == 1) {
                battlebutton.setBackground(this.getResources().getDrawable(R.drawable.button1));
                num3 = 0;
                character.setPadding(0, 0, 0, 0);
                monster.setPadding(0, 0, 0, 0);
                handlerEX.removeCallbacks(runnable);
                handlerEX.removeCallbacks(runnableEX);
                if(currentMHp<=0)
                {
                    monsterLV+=1;
                    monsterlevel.setText("Lv."+monsterLV);
                    monsterHP=monsterHP*1.2;
                    currentMHp=monsterHP;
                    monsterS=monsterS*1.2;
                    mpb.setProgress(100);
                    monsterblood.setText((long)currentMHp+"/"+(long)monsterHP);

                    handlerEX.removeCallbacks(runnable);//停止戰鬥
                    handlerEX.removeCallbacks(runnableEX);
                    String uri1 = "@drawable/" + "button1";
                    int imageResource1 = getResources().getIdentifier(uri1, null, getPackageName());
                    Drawable image1 = getResources().getDrawable(imageResource1);
                    num3=0;

                }
                if(currentCHp<=0)
                {
                    handlerEX.removeCallbacks(runnable);//停止戰鬥
                    handlerEX.removeCallbacks(runnableEX);
                    String uri1 = "@drawable/" + "button1";
                    int imageResource1 = getResources().getIdentifier(uri1, null, getPackageName());
                    Drawable image1 = getResources().getDrawable(imageResource1);
                    num3=0;
                    currentMHp=monsterHP;
                    mpb.setProgress(100);

                    currentCHp=1;
                    cpb.setProgress(1);
                }
            } else {
                if (currentCHp > 0 && currentMHp > 0) {
                    battlebutton.setBackground(this.getResources().getDrawable(R.drawable.buttonclick1));
                    num3 = 1;
                    handlerEX.postDelayed(runnable, 600);
                    handlerEX.postDelayed(runnableEX, 0);
                }
            }
        }
    }
    //設定按鈕
    public void setbutton(View v){
        /*if(num1==0 && num3==0) {
            if (num2 == 1) {
                storebutton.setBackground(this.getResources().getDrawable(R.drawable.button1));
                num2 = 0;
            } else {
                result = executeQuery("UPDATE  `Character` SET  `hp` =  '"+String.valueOf((int)currentCHp)+"' WHERE  `id` =  '0'");
                storebutton.setBackground(this.getResources().getDrawable(R.drawable.buttonclick1));
                num2 = 1;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,Main2Activity.class);
                //bundle.putInt("Gold",(int)Gold);
                bundle.putInt("Hp",(int) currentCHp);
                bundle.putInt("MaxHp",(int) characterHp);
                bundle.putInt("Str",(int) characterS);
                bundle.putInt("Counter", temp);
                intent.putExtras(bundle);
                startActivity(intent);
                //MainActivity.this.finish();
            }
        }*/
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,Main4Activity.class);
        //bundle.putInt("Gold",(int)Gold);
        /*bundle.putInt("Hp",(int) currentCHp);
        bundle.putInt("MaxHp",(int) characterHp);
        bundle.putInt("Str",(int) characterS);
        bundle.putInt("Counter", temp);
        intent.putExtras(bundle);*/
        startActivity(intent);
        

    }

    Handler handlerEX=new Handler();

    Runnable runnable=new Runnable() {
        @TargetApi(17)
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情

            character.setPadding(0, 0, 0, 0);
            monster.setPadding(0, 0, 0, 0);
            characterdamage.setText("");
            monsterdamage.setText("");
            handlerEX.postDelayed(this, 600);

            // TODO 怪物死亡
            if(currentMHp<1)
            {
                monsterLV+=1;
                monsterlevel.setText("Lv."+monsterLV);
                monsterHP=monsterHP*1.1;
                currentMHp=monsterHP;
                monsterS=monsterS*1.1;
                mpb.setProgress(100);
                monsterblood.setText((long)currentMHp+"/"+(long)monsterHP);

                exe=exe+(monsterHP*10+monsterS*10+monsterLV*20)/(monsterLV);
                exep=(exe/exeMax)*100;
                exeb.setProgress((int)exep);
                long monsterG=(long)(monsterHP*1.5+monsterS*10+monsterLV*20);
                Gold=Gold+(long)(Math.random()* monsterG + monsterS);
                characterGold.setText("Gold："+Gold);

                result = executeQuery("UPDATE  `Character` SET  `exe` =  '"+String.valueOf((int)exe)+"' , `gold` =  '"+String.valueOf(Gold)+"', `mlevel` =  '"+String.valueOf(monsterLV)+"' , `mhp` =  '"+String.valueOf(monsterHP)+"'  , `mmaxhp` =  '"+String.valueOf(monsterHP)+"', `mstr` =  '"+String.valueOf(monsterS)+"',`hp` =  '"+String.valueOf(currentCHp)+"'   WHERE  `id` =  '0'");

                // TODO 主角升級
                if(exe>=exeMax)
                {
                    characterLV+=1;
                    exe=exe-exeMax;
                    exeMax=(long)(exeMax+5)*1.1;
                    exep=(exe/exeMax)*100;
                    exeb.setProgress((int)exep);
                    characterlevel.setText("Lv."+characterLV);
                    characterHp=(characterHp+characterLV)*1.1;
                    currentCHp=characterHp;
                    cpb.setProgress(100);
                    characterblood.setText((long)currentCHp+"/"+(long)characterHp);
                    result = executeQuery("UPDATE  `Character` SET  `maxhp` =  '"+String.valueOf((int)characterHp)+"',`level` =  '"+String.valueOf((int)characterLV)+"',`exeMax` =  '"+String.valueOf((int)exeMax)+"', `exe` =  '"+String.valueOf((int)exe)+"' ,`hp` =  '"+String.valueOf((int)characterHp)+"' WHERE  `id` =  '0'");
                }

                handlerEX.removeCallbacks(runnable);
                handlerEX.removeCallbacks(runnableEX);
                String uri1 = "@drawable/" + "button1";
                int imageResource1 = getResources().getIdentifier(uri1, null, getPackageName());
                Drawable image1 = getResources().getDrawable(imageResource1);
                battlebutton.setBackground(image1);
                num3=0;

            }
            // TODO 主角死亡
            if(currentCHp<1)
            {
                handlerEX.removeCallbacks(runnable);
                handlerEX.removeCallbacks(runnableEX);
                String uri1 = "@drawable/" + "button1";
                int imageResource1 = getResources().getIdentifier(uri1, null, getPackageName());
                Drawable image1 = getResources().getDrawable(imageResource1);
                battlebutton.setBackground(image1);
                num3=0;
                currentMHp=monsterHP;
                mpb.setProgress(100);
                monsterblood.setText((long)currentMHp+"/"+(long)monsterHP);


                currentCHp=1;
                characterblood.setText((long)currentCHp+"/"+(long)characterHp);
                cpb.setProgress(1);

                exe=exe-(exe/10);
                exep=(exe/exeMax)*100;
                exeb.setProgress((int)exep);
            }

        }
    };
    Runnable runnableEX=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            soundPool.play(attack,1.0f,1.0f,1,0,1.0f);

            currentMHp-=characterS;
            if(currentMHp>=1){
                monsterblood.setText((long)currentMHp+"/"+(long)monsterHP);
            }
            else
            {
                monsterblood.setText("0/"+(long)monsterHP);
            }
            double w=characterS/10000;

            if(w>1)
                monsterdamage.setText("-"+(long)w+"w");
            else
                monsterdamage.setText("-"+(long)characterS);


            if(monsterS>(characterS/20)) {
                currentCHp = currentCHp - monsterS + (characterS / 20);
                characterdamage.setText("-"+(long)monsterS);
            }
            else
            {
                characterdamage.setText("-Miss");
            }
            characterblood.setText((long)currentCHp+"/"+(long)characterHp);

            mhp=(currentMHp/monsterHP)*100;
            mpb.setProgress((int)mhp);
            chp=(currentCHp/characterHp)*100;
            cpb.setProgress((int)chp);

            character.setPadding(60, 0, 0, 0);
            monster.setPadding(0, 0, 60, 0);
            handlerEX.postDelayed(this, 1200);
        }
    };

    @Override
    public  void onSensorChanged(SensorEvent event){
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            long actualTime =System.currentTimeMillis();
            long mTimeInterval = actualTime - mLastUpdate;
            if(actualTime-mLastUpdate>UPDATE_THRESHOLD){

                if (mTimeInterval < UPDATE_THRESHOLD) return;
                mLastUpdate=actualTime;
                float x =event.values[0];
                float y =event.values[1];
                float z =event.values[2];

                float mDeltaX = x - mLastX;
                float mDeltaY = y - mLastY;
                float mDeltaZ = z - mLastZ;

                mLastX = x;
                mLastY = y;
                mLastZ = z;

                mSpeed = Math.sqrt(mDeltaX * mDeltaX + (mDeltaY * mDeltaY) + (mDeltaZ * mDeltaZ))/ mTimeInterval * 30000;

                if(jj==1) {
                    if (8000 >= mSpeed && mSpeed >= SPEED_SHRESHOLD && num3==0 && off==0) {
                        tmp+=1;
                        if(tmp%40==0) {
                            Toast.makeText(this, String.valueOf(mSpeed), Toast.LENGTH_LONG).show();
                            temp += 1;
                            counter.setText("Counter：" + String.valueOf(temp));
                            result = executeQuery("UPDATE  `Character` SET  `counter` =  '" + String.valueOf(temp) + "' WHERE  `id` =  '0'");
                        }
                    }
                }
                jj=1;
            }

        }
    }

    HttpClient httpClient = new DefaultHttpClient();
    HttpPost post = new HttpPost("http://4a3g0102.esy.es/qeury.php");//TODO  建立HTTP Post連線
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();/*Post運作傳送變數必須用NameValuePair[ ]陣列儲存*/
    StringBuilder builder = new StringBuilder();
    private String executeQuery(String query)
    {
        String result = "";
        try
        {
            nameValuePairs.add(new BasicNameValuePair("query_string", query));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));//TODO 防止亂馬
            HttpResponse httpResponse = httpClient.execute(post);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            //StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null)
            {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
        }
        catch (Exception e)
        {
            Log.e("log_tag", e.toString());
        }
        return result;
    }
    public final void renewListView(String input)
    {
	/*
	 * SQL 結果有多筆資料時使用JSONArray
	 * 只有一筆資料時直接建立JSONObject物件
	 * JSONObject jsonData = new JSONObject(result);
	 */
        try
        {
            JSONArray jsonArray = new JSONArray(input);
            setTitle(jsonArray.length() + "筆資料");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                while (ii==0) {
                    counter.setText("Counter："+jsonData.getString("counter"));
                    temp = Integer.valueOf(jsonData.getString("counter"));
                    characterLV=Integer.valueOf(jsonData.getString("level"));
                    characterlevel.setText("Lv."+characterLV);
                    characterHp=Double.valueOf(jsonData.getString("maxhp"));
                    currentCHp=Double.valueOf(jsonData.getString("hp"));;
                    characterS=Double.valueOf(jsonData.getString("str"));
                    characterblood.setText((long)currentCHp+"/"+(long)characterHp);
                    chp=(currentCHp/characterHp)*100;
                    cpb.setProgress((int)chp);
                    exe=Double.valueOf(jsonData.getString("exe"));
                    exeMax=Double.valueOf(jsonData.getString("exeMax"));
                    exep=(exe/exeMax)*100;
                    exeb.setProgress((int)exep);
                    Gold=Long.valueOf(jsonData.getString("gold"));
                    characterGold.setText("Gold："+Gold);

                    monsterLV=Integer.valueOf(jsonData.getString("mlevel"));
                    monsterlevel.setText("Lv."+monsterLV);
                    monsterHP=Double.valueOf(jsonData.getString("mmaxhp"));
                    currentMHp=Double.valueOf(jsonData.getString("mhp"));;
                    monsterS=Double.valueOf(jsonData.getString("mstr"));
                    monsterblood.setText((long)currentMHp+"/"+(long)monsterHP);
                    mhp=(currentMHp/monsterHP)*100;
                    mpb.setProgress((int)mhp);
                    ii+=1;
                }
            }
        }
        catch (JSONException e)
        {
            // TODO 自動產生的 catch 區塊
            e.printStackTrace();
        }
    }
}

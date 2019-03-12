package com.example.yu.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class Main3Activity extends AppCompatActivity {
    Button back;
    TextView hptext,strtext,pointtext,hpaddtext,straddtext;

    String result;
    int point,used=0,hppoint,strpoint,hp=0,str=0,ii=0,Str,Hp;
    Bundle bundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        back=(Button)findViewById(R.id.button7);
        hptext=(TextView)findViewById(R.id.hptext);
        strtext=(TextView)findViewById(R.id.stext);
        hpaddtext=(TextView)findViewById(R.id.textView9);
        straddtext=(TextView)findViewById(R.id.textView10);
        pointtext=(TextView)findViewById(R.id.textView11);

        Bundle bundleH =this.getIntent().getExtras();
        Hp = bundleH.getInt("Hp");
        Str = bundleH.getInt("Str");
        hptext.setText(String.valueOf(Hp));
        strtext.setText(String.valueOf(Str));
        point=bundleH.getInt("Lv");


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        result = executeQuery("SELECT *  FROM  `Character` ");
        renewListView(result);
        pointtext.setText("point："+String.valueOf(point));

    }

    public void backbutton(View v)
    {
        Intent intent = new Intent();
        intent.setClass(Main3Activity.this,MainActivity.class);

        bundle.putInt("used",used);
        intent.putExtras(bundle);
        startActivity(intent);
        Main3Activity.this.finish();
    }

    public  void  hpaddbutton(View v)
    {
        if(point!=0) {
            point -= 1;
            pointtext.setText("point："+String.valueOf(point));
            hp += 20;
            used+=1;
            hpaddtext.setText(String.valueOf(hp));
        }
    }
    public  void  hpreducebutton(View v)
    {
        if(hp>0) {
            point += 1;
            pointtext.setText("point："+String.valueOf(point));
            hp -= 20;
            used-=1;
            hpaddtext.setText(String.valueOf(hp));
        }
    }

    public  void  straddbutton(View v)
    {
        if(point!=0) {
            point -= 1;
            pointtext.setText("point："+String.valueOf(point));
            str += 1;
            used+=1;
            straddtext.setText(String.valueOf(str));
        }
    }
    public  void  strreducebutton(View v)
    {
        if(str>0) {
            point += 1;
            pointtext.setText("point："+String.valueOf(point));
            str -= 1;
            used-=1;
            straddtext.setText(String.valueOf(str));
        }
    }

    public void confirm(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否要提升能力？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 左方按鈕方法
                        Str+=str;
                        Hp+=hp;
                        strtext.setText(String.valueOf(Str));
                        hptext.setText(String.valueOf(Hp));
                        result = executeQuery("UPDATE  `Character` SET  `usedpoint` =  '"+String.valueOf(used)+"',`maxhp` =  '"+String.valueOf(Hp)+"',`str` =  '"+String.valueOf(Str)+"' WHERE  `id` =  '0'");
                        str=0;
                        hp=0;
                        straddtext.setText(String.valueOf(str));
                        hpaddtext.setText(String.valueOf(hp));

                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 右方按鈕方法

                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
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
                    used = Integer.valueOf(jsonData.getString("usedpoint"));
                    point=point-used;
                    pointtext.setText("point："+String.valueOf(point));
                    ii=1;
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

package com.example.yu.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Main2Activity extends AppCompatActivity {
    Button hpb,sb,vb,back;
    EditText hpe,se,ve,sr,vr;
    TextView gold,countertext;

    long ii=0,hpee=0,see=0,vee=0,srr=0,vrr=0,Hp,Str,MaxHp,counter;
    long Gold;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        hpb=(Button)findViewById(R.id.button12);
        sb=(Button)findViewById(R.id.button14);
        vb=(Button)findViewById(R.id.button15);
        back=(Button)findViewById(R.id.button16);

        hpe=(EditText)findViewById(R.id.editText);
        se=(EditText)findViewById(R.id.editText2);
        ve=(EditText)findViewById(R.id.editText3);
        sr=(EditText)findViewById(R.id.editText4);
        vr=(EditText)findViewById(R.id.editText5);

        gold=(TextView)findViewById(R.id.textView7);
        countertext=(TextView)findViewById(R.id.textView13);

        Bundle bundleH =this.getIntent().getExtras();
        //Gold = bundleH.getInt("Gold");
        Hp = bundleH.getInt("Hp");
        MaxHp = bundleH.getInt("MaxHp");
        Str = bundleH.getInt("Str");
        counter=bundleH.getInt("Counter");

        countertext.setText("Counter："+String.valueOf(counter));

        result = executeQuery("SELECT *  FROM  `Character` ");
        renewListView(result);
        gold.setText("Gold："+String.valueOf(Gold));
    }

    public void hpbuy(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否要花50*N Gold購買藥水回復100*N HP？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 左方按鈕方法
                        hpee= Integer.valueOf(hpe.getText().toString());
                        long i=100*hpee;
                        Gold=Gold-(i/2);
                        if(Gold>=0) {
                            gold.setText("Gold："+String.valueOf(Gold));
                            Hp+=i;
                            if (Hp >= MaxHp) {
                                Hp = MaxHp;
                                result = executeQuery("UPDATE  `Character` SET  `hp` =  '" + String.valueOf(Hp) + "',`gold` =  '" + String.valueOf(Gold) + "'  WHERE  `id` =  '0'");
                            } else {
                                result = executeQuery("UPDATE  `Character` SET  `hp` =  '" + String.valueOf(Hp) + "',`gold` =  '" + String.valueOf(Gold) + "'  WHERE  `id` =  '0'");
                            }
                        }
                        else
                        {
                            Gold=Gold+i;
                        }
                        hpe.setText("");
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
    public void sbuy(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否要花500*N Gold購買藥水提升1點*N力量？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 左方按鈕方法
                        see= Integer.valueOf(se.getText().toString());
                        long i=500*see;
                        Gold=Gold-i;
                        if(Gold>=0) {
                            gold.setText("Gold：" + String.valueOf(Gold));
                            Str += see;
                            result = executeQuery("UPDATE  `Character` SET  `str` =  '" + String.valueOf(Str) + "',`gold` =  '" + String.valueOf(Gold) + "'  WHERE  `id` =  '0'");
                        }
                        else
                        {
                            Gold=Gold+i;
                        }
                        se.setText("");
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
    public void vbuy(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否要花500*N Gold購買藥水提升20點*N體力？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 左方按鈕方法
                        vee= Integer.valueOf(ve.getText().toString());
                        long i=500*vee;
                        Gold=Gold-i;
                        if(Gold>=0)
                        {
                            gold.setText("Gold：" + String.valueOf(Gold));
                            MaxHp += vee * 20;
                            result = executeQuery("UPDATE  `Character` SET  `maxhp` =  '" + String.valueOf(MaxHp) + "',`gold` =  '" + String.valueOf(Gold) + "'  WHERE  `id` =  '0'");
                        }
                        else
                        {
                            Gold=Gold+i;
                        }
                        ve.setText("");
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
    public void hpreelbuy(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否要花500*N Counter購買卷軸提升20點*N體力？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 左方按鈕方法
                        vrr= Integer.valueOf(vr.getText().toString());
                        long i=500*vrr;
                        counter=counter-i;
                        if(counter>=0) {
                            countertext.setText("Counter："+String.valueOf(counter));
                            MaxHp += vrr * 20;
                            result = executeQuery("UPDATE  `Character` SET  `maxhp` =  '" + String.valueOf(MaxHp) + "',`counter` =  '" + String.valueOf(counter) + "'  WHERE  `id` =  '0'");
                        }
                        else
                        {
                            counter=counter+i;
                        }
                        vr.setText("");
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
    public void strreelbuy(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否要花500*N Counter購買卷軸提升1點*N力量？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 左方按鈕方法
                        srr= Integer.valueOf(sr.getText().toString());
                        long i=500*srr;
                        counter=counter-i;
                        if(counter>=0) {
                            countertext.setText("Counter：" + String.valueOf(counter));
                            Str += srr;
                            result = executeQuery("UPDATE  `Character` SET  `str` =  '" + String.valueOf(Str) + "',`counter` =  '" + String.valueOf(counter) + "'  WHERE  `id` =  '0'");
                        }
                        else
                        {
                            counter=counter+i;
                        }
                        sr.setText("");
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
    public void timereelbuy(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否要花2000Counter  購買卷軸讓怪物等級初始化？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 左方按鈕方法
                        counter=counter-2000;
                        if(counter>=0) {
                            countertext.setText("Counter：" + String.valueOf(counter));
                            Str += srr;
                            result = executeQuery("UPDATE  `Character` SET   `counter` =  '" + String.valueOf(counter) + "' ,`mlevel` =  '"+String.valueOf(1)+"' , `mhp` =  '"+String.valueOf(30)+"'  , `mmaxhp` =  '"+String.valueOf(30)+"', `mstr` =  '"+String.valueOf(2)+"'   WHERE  `id` =  '0'");
                        }
                        else
                        {
                            counter=counter+2000;
                        }
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
    public void backbutton(View v)
    {
        Intent intent = new Intent();
        intent.setClass(Main2Activity.this,MainActivity.class);
        startActivity(intent);
        Main2Activity.this.finish();
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
                    Gold=Long.valueOf(jsonData.getString("gold"));
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

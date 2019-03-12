package com.example.yu.myapplication;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main4Activity extends AppCompatActivity {
    CalendarView calendarView;
    TextView BMItext;
    EditText height,weight;
    Button BMIbutton;
    double h,w;
    String BMI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        calendarView=(CalendarView)findViewById(R.id.calendarView);
        BMItext=(TextView)findViewById(R.id.BMItext);
        height=(EditText)findViewById(R.id.height);
        weight=(EditText)findViewById(R.id.weight);
        BMIbutton=(Button)findViewById(R.id.BMIbutton);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getBaseContext(),"步數:10000步"+" "+"消耗卡路里:625cal",Toast.LENGTH_LONG).show();
            }
        });
        BMIbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h=Double.parseDouble(height.getText().toString());
                h/=100;
                w=Double.parseDouble(weight.getText().toString());
                BMItext.setText(String.format("%.2f",w/(h*h)));
            }
        });
    }
}

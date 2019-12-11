package com.e.mywallet;

import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.lang.Object;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

public class GetTime extends AppCompatActivity {

    TextView tampil;
    Button tombolGetTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_time);

        tampil = (TextView) findViewById(R.id.time);
        tombolGetTime= (Button) findViewById(R.id.buttontime);

        /*Calendar calendar = Calendar.getInstance();
        final String currentdate = DateFormat.getTimeInstance().format(calendar.getTimeInstance());*/




        tombolGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Date currentDate = new Date();
                SimpleDateFormat Format = new SimpleDateFormat("MM/dd/YYYY,hh:mm:ss");
                tampil.setText(Format.format(currentDate));
            }
        });
    }




}

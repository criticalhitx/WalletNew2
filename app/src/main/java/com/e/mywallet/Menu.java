package com.e.mywallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class Menu extends AppCompatActivity {
    Button btnCek,btnSend, btnReceive, btnCheck,btnKey, btnInsertKey,btnInsertPin,btnHistoryIn, btnHistoryOut;
    TextView tampil;
    ImageView imagebackpin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        btnSend = (Button)findViewById(R.id.buttonsend);
        btnReceive = (Button)findViewById(R.id.buttonreceive);
        btnCheck = (Button)findViewById(R.id.buttoncheck);
        btnKey = (Button) findViewById(R.id.buttonkey);
        btnHistoryIn =(Button)findViewById(R.id.Menu_btHistoryIn);
        btnHistoryOut =(Button)findViewById(R.id.Menu_btHistoryOut);
        tampil = (TextView) findViewById(R.id.text_view_id);
        btnInsertKey = (Button) findViewById(R.id.buttonInsertKey);
        btnInsertPin = (Button) findViewById(R.id.buttonInsertPin);
        imagebackpin =(ImageView)findViewById(R.id.Menu_image);

        imagebackpin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finishAffinity();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Menu.this, Send.class));
            }
        });

        btnReceive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Menu.this, Receive.class));
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Menu.this,Check.class));
            }
        });

        btnKey.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Menu.this, Key.class));;
            }
        });

        btnInsertKey.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Menu.this, InsertKey.class));
            }
        });

        btnInsertPin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Menu.this, InsertPIN.class));
            }
        });

        btnHistoryIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Menu.this, HistoryIn.class));
            }
        });

        btnHistoryOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Menu.this, HistoryOut.class));
            }
        });

    }
    public void onBackPressed(){
    }

}

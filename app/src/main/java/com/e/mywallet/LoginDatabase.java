package com.e.mywallet;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

import org.w3c.dom.Text;

public class LoginDatabase extends AppCompatActivity {
    EditText ET_NAME, ET_PASS;
    String login_name, login_pass;
    Button btnReg,btnLogin;
    ImageView cheatMagician;
    TextView tvTestResponse,btnRecover;
    Button btOpen;
    Spinner spBauds;
    CheckBox cbAutoscrolls;
    Physicaloid mPhysicaloid; // initialising
    boolean canexit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_database);
        btnReg=(Button)findViewById(R.id.Login_Register);
        ET_NAME=(EditText)findViewById(R.id.Login_username);
        ET_PASS=(EditText)findViewById(R.id.Login_password);
        btnRecover=(TextView) findViewById(R.id.Login_Recover); // To go to recover menu
        btnLogin=(Button)findViewById(R.id.Login_btLogin);
        cheatMagician=(ImageView)findViewById(R.id.Login_imageCheatMagician);
        tvTestResponse=(TextView)findViewById(R.id.Login_testResponse);

        //--------
        btOpen =(Button)findViewById(R.id.Login_btOpens); //Tombol Connect
        spBauds = (Spinner) findViewById(R.id.Login_spinnerz);
        cbAutoscrolls = (CheckBox)findViewById(R.id.Login_autoscrollz);
        //-------------

        mPhysicaloid = new Physicaloid(this);

        btOpen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String baudtext = spBauds.getSelectedItem().toString();
                switch (baudtext) {
                    case "300 baud":
                        mPhysicaloid.setBaudrate(300);
                        break;
                    case "1200 baud":
                        mPhysicaloid.setBaudrate(1200);
                        break;
                    case "2400 baud":
                        mPhysicaloid.setBaudrate(2400);
                        break;
                    case "4800 baud":
                        mPhysicaloid.setBaudrate(4800);
                        break;
                    case "9600 baud":
                        mPhysicaloid.setBaudrate(9600);
                        break;
                    case "19200 baud":
                        mPhysicaloid.setBaudrate(19200);
                        break;
                    case "38400 baud":
                        mPhysicaloid.setBaudrate(38400);
                        break;
                    case "576600 baud":
                        mPhysicaloid.setBaudrate(576600);
                        break;
                    case "744880 baud":
                        mPhysicaloid.setBaudrate(744880);
                        break;
                    case "115200 baud":
                        mPhysicaloid.setBaudrate(115200);
                        break;
                    case "230400 baud":
                        mPhysicaloid.setBaudrate(230400);
                        break;
                    case "250000 baud":
                        mPhysicaloid.setBaudrate(250000);
                        break;
                    default:
                        mPhysicaloid.setBaudrate(9600);
                }

                if(mPhysicaloid.open()) {

                    setEnabledUi(true);
                    String kirim = "4"; //Mengirim case 4 ke while loop [ Mode Login ]
                    if(kirim.length()>0) {
                        byte[] buf = kirim.getBytes();
                        mPhysicaloid.write(buf, buf.length);
                    }

                    if(cbAutoscrolls.isChecked())
                    {
                        tvTestResponse.setMovementMethod(new ScrollingMovementMethod());
                    }
                    mPhysicaloid.addReadListener(new ReadLisener() {
                        @Override
                        public void onRead(int size) {
                            byte[] buf = new byte[size];
                            mPhysicaloid.read(buf, size);
                            tvAppend(tvTestResponse, Html.fromHtml( new String(buf) ));
                        }
                    });

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Not Connect",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        // ------------ Cheat Magician Code ----------------- //
        cheatMagician.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*String kirim = "moemoe";
                if(kirim.length()>0) {
                    byte[] buf = kirim.getBytes();
                    mPhysicaloid.write(buf, buf.length);}


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canexit=true;
                        mPhysicaloid.close();
                        onBackPressed();
                    }
                }, 1500);*/
                startActivity(new Intent(LoginDatabase.this, Menu.class));
            }
        });
        // --------------------------------------------------- //
        btnReg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginDatabase.this, Register.class));
            }
        });

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginDatabase.this, RecoverSK.class));
            }
        });
    }


    public void userLogin(View view) {
     login_name = ET_NAME.getText().toString(); // username yang diketik user
     login_pass = ET_PASS.getText().toString(); // password yang diketik user
     String namefromwallet = tvTestResponse.getText().toString();
     if(login_name.equals(namefromwallet))
     {
         String method = "login";
         BackgroundTask backgroundTask = new BackgroundTask(this);
         backgroundTask.execute(method,login_name,login_pass);
         ///--------------- kirim moe ----------
         String kirim = "true"; //Mengirim case 4 ke while loop [ Mode Login ]
         if(kirim.length()>0) {
             byte[] buf = kirim.getBytes();
             mPhysicaloid.write(buf, buf.length);
         }
         ET_NAME.setEnabled(false);
         ET_PASS.setEnabled(false);
         btnLogin.setEnabled(false);

         Handler handler = new Handler();
         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                 mPhysicaloid.close();
                 mPhysicaloid.clearReadListener();
             }
         }, 1500);

         /// --------------------------------------
     }
     else
     {
         Toast.makeText(this,"Bukan Wallet Anda", Toast.LENGTH_SHORT).show();
     }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false);
            spBauds.setEnabled(false);
            cbAutoscrolls.setEnabled(false);
            ET_NAME.setVisibility(View.VISIBLE);
            ET_PASS.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btOpen.setVisibility(View.GONE);
            btnReg.setVisibility(View.GONE);
            btnRecover.setVisibility(View.GONE);


        } else {
            btOpen.setEnabled(true);
            spBauds.setEnabled(true);
            cbAutoscrolls.setEnabled(true);

        }
    }





    Handler mHandler = new Handler();
    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }



}


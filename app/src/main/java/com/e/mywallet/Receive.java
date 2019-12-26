package com.e.mywallet;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Receive extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    Button btOpen,  btWrite;
    EditText etWrite;
    TextView tvRead,tvStealth,tvUsername,tvExitNotif;
    Spinner spBaud;
    CheckBox cbAutoscroll;
    boolean canexit=false;
    ImageView imagebackPin;

    // pin Dialog

    //

    Physicaloid mPhysicaloid; // initialising library

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        btOpen  = (Button) findViewById(R.id.btOpen);
        btWrite = (Button) findViewById(R.id.btWrite);
        etWrite = (EditText) findViewById(R.id.etWrite);
        // --- Test parameter ---
        tvRead  = (TextView) findViewById(R.id.tvRead);
        tvUsername=(TextView)findViewById(R.id.Receive_tvUsername);
        tvStealth=(TextView)findViewById(R.id.Receive_tvStealth);
        tvExitNotif=(TextView)findViewById(R.id.tvExitNotif);
        // --------------------------
        spBaud = (Spinner) findViewById(R.id.spinner);
        cbAutoscroll = (CheckBox)findViewById(R.id.autoscroll);
        setEnabledUi(false);
        mPhysicaloid = new Physicaloid(this);
        imagebackPin = (ImageView) findViewById(R.id.Receive_imagebackPin);

        imagebackPin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String kirim = "moemoe";
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
                }, 1500);

            }
        });
    }

    public void onClickOpen(View v) {
        String baudtext = spBaud.getSelectedItem().toString();
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
            btOpen.setVisibility(View.GONE);
            tvRead.setText(null);
            String str = "6"; // 6 is Receive Mode
            if(str.length()>0) {
                byte[] buf = str.getBytes();
                mPhysicaloid.write(buf, buf.length);
            }

            if(cbAutoscroll.isChecked())
            {
                tvRead.setMovementMethod(new ScrollingMovementMethod());
            }
            mPhysicaloid.addReadListener(new ReadLisener() {
                @Override
                public void onRead(int size) {
                    byte[] buf = new byte[size];
                    mPhysicaloid.read(buf, size);
                    tvAppend(tvRead, Html.fromHtml( new String(buf) ));
                }
            });

            openDialog();

            } else {
            Toast.makeText(this, "Please Check Your Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void applyTexts(String username, String password) {
        tvRead.setText(username);
        // Mengirim PIN ke ESP32
        String kirim = tvRead.getText().toString();
        if(kirim.length()>0) {
            byte[] buf = kirim.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
        tvRead.setText(null);
        tvExitNotif.setText("Press Generate button to Proceed");
        tvExitNotif.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setEnabledUi(true);

            }
        }, 2000);
    }

    public void openDialog()
    {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }


    public void onBackPressed(){
        if (canexit) {
            super.onBackPressed();
        }
    }

    public void onClickWrite(View v) {
        String integratedAddress = tvRead.getText().toString();
        String user_name = integratedAddress.substring(65);
        String stealth_address = integratedAddress.substring(0,64);
        tvUsername.setText(user_name);
        tvStealth.setText(stealth_address);

        String method = "receive";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,user_name,stealth_address);
        btWrite.setEnabled(false);

        tvExitNotif.setVisibility(View.VISIBLE);
        tvExitNotif.setText("Write down the Stealth Address, then press logo button to exit");
    }


    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false);
            spBaud.setEnabled(false);
            cbAutoscroll.setEnabled(false);
            btWrite.setEnabled(true);
        } else {
            btOpen.setEnabled(true);
            spBaud.setEnabled(true);
            cbAutoscroll.setEnabled(true);
            btWrite.setEnabled(false);
        }
    }


   // public void openDialog() {
     //   ExampleDialog exampleDialog = new ExampleDialog();
       // exampleDialog.show(getSupportFragmentManager(), "example dialog");
    //}

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

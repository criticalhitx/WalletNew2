package com.e.mywallet;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Key extends Activity {
    Button BTOpen,  BTWrite;
   // EditText ETWrite;
    TextView TVRead;
    Spinner SPBaud;
    CheckBox CBautoscroll;

    Physicaloid mnPhysicaloid; // initialising library

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key);

        BTOpen  = (Button) findViewById(R.id.buttonconnect);
        BTWrite = (Button) findViewById(R.id.buttonkey);
       // ETWrite = (EditText) findViewById(R.id.ETWrite);
        TVRead  = (TextView) findViewById(R.id.tvRead);
        SPBaud = (Spinner) findViewById(R.id.spinner);
        CBautoscroll = (CheckBox)findViewById(R.id.autoscroll);
        setEnabledUi(false);
        mnPhysicaloid = new Physicaloid(this);

    }

    public void onClickOpen(View v) {
        String baudtext = SPBaud.getSelectedItem().toString();
        switch (baudtext) {
            case "300 baud":
                mnPhysicaloid.setBaudrate(300);
                break;
            case "1200 baud":
                mnPhysicaloid.setBaudrate(1200);
                break;
            case "2400 baud":
                mnPhysicaloid.setBaudrate(2400);
                break;
            case "4800 baud":
                mnPhysicaloid.setBaudrate(4800);
                break;
            case "9600 baud":
                mnPhysicaloid.setBaudrate(9600);
                break;
            case "19200 baud":
                mnPhysicaloid.setBaudrate(19200);
                break;
            case "38400 baud":
                mnPhysicaloid.setBaudrate(38400);
                break;
            case "576600 baud":
                mnPhysicaloid.setBaudrate(576600);
                break;
            case "744880 baud":
                mnPhysicaloid.setBaudrate(744880);
                break;
            case "115200 baud":
                mnPhysicaloid.setBaudrate(115200);
                break;
            case "230400 baud":
                mnPhysicaloid.setBaudrate(230400);
                break;
            case "250000 baud":
                mnPhysicaloid.setBaudrate(250000);
                break;
            default:
                mnPhysicaloid.setBaudrate(9600);
        }

        if(mnPhysicaloid.open()) {
            setEnabledUi(true);

            if(CBautoscroll.isChecked())
            {
                TVRead.setMovementMethod(new ScrollingMovementMethod());
            }
            mnPhysicaloid.addReadListener(new ReadLisener() {
                @Override
                public void onRead(int size) {
                    byte[] buf = new byte[size];
                    mnPhysicaloid.read(buf, size);
                    tvAppend(TVRead, Html.fromHtml( new String(buf)));
                }
            });
        } else {
            Toast.makeText(this, "Please Check Your Connection", Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        if(mnPhysicaloid.close()) {
            mnPhysicaloid.clearReadListener();
            setEnabledUi(false);
        }
    }
    public void onClickWrite(View v) {
        TVRead.setText(null);
        String str = getText(R.string.view).toString()+"\r\n";
        if(str.length()>0) {
            byte[] buf = str.getBytes();
            mnPhysicaloid.write(buf, buf.length);
        }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            BTOpen.setEnabled(false);
            SPBaud.setEnabled(false);
            CBautoscroll.setEnabled(false);
            BTWrite.setEnabled(true);
        } else {
            BTOpen.setEnabled(true);
            SPBaud.setEnabled(true);
            CBautoscroll.setEnabled(true);
            BTWrite.setEnabled(false);
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

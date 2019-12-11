package com.e.mywallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Send extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    EditText stealthaddress, amount;
    String user_name; // Parameter ini DIKIRIMKAN OLEH
    TextView tvTest; // response
    Button btSend, btOpen;
    ImageView imagebackpin;
    boolean canexit = false;
    Spinner spBauds;
    CheckBox cbAutoscrolls;
    Physicaloid mPhysicaloid; // initialising library

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        stealthaddress = (EditText)findViewById(R.id.Send_et_key); // SA penerima
        amount = (EditText)findViewById(R.id.Send_et_amount); // Nilai yang mau kirim
        tvTest = (TextView)findViewById(R.id.SendTesto); // Response
        ///-------------------------------------------------
        btOpen =(Button)findViewById(R.id.Send_Connect); //Tombol Connect
        spBauds = (Spinner) findViewById(R.id.Send_spinnerz);
        cbAutoscrolls = (CheckBox)findViewById(R.id.Send_autoscrollz);
        btSend = (Button)findViewById(R.id.Send_btsubmit); // Send button
        imagebackpin=(ImageView)findViewById(R.id.Send_image); // Image

        setEnabledUi(false);

        ////////////////////////////////
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
                    String kirim = "9"; //Mengirim case 9 ke while loop [ Mode Send ]
                    if(kirim.length()>0) {
                        byte[] buf = kirim.getBytes();
                        mPhysicaloid.write(buf, buf.length);
                    }

                    if(cbAutoscrolls.isChecked())
                    {
                        tvTest.setMovementMethod(new ScrollingMovementMethod());
                    }
                    mPhysicaloid.addReadListener(new ReadLisener() {
                        @Override
                        public void onRead(int size) {
                            byte[] buf = new byte[size];
                            mPhysicaloid.read(buf, size);
                            tvAppend(tvTest, Html.fromHtml( new String(buf) ));
                        }
                    });
                    openDialog();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Not Connect",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        imagebackpin.setOnClickListener(new View.OnClickListener()
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
    @Override
    public void applyTexts(String username, String password) {
        tvTest.setText(username);
        // Mengirim PIN ke ESP32
        String kirim = tvTest.getText().toString();
        if(kirim.length()>0) {
            byte[] buf = kirim.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
        tvTest.setText(null);

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

    public void Send_buttonsubmit(View view) { //Dibuat UI off agar tidak bisa pencet sebelum dapet usernmae.
        user_name=tvTest.getText().toString();
        String method = "send";
        String stealth_address = stealthaddress.getText().toString();
        String transfer = amount.getText().toString();
        new Send.MyTask(this).execute(method,user_name,stealth_address,transfer); // Execute Asynctask
        // Make UI uneditable
        amount.setEnabled(false);
        stealthaddress.setEnabled(false);
        btSend.setEnabled(false);

    }


    private class MyTask extends AsyncTask<String,Void,String>
    {
        AlertDialog alertDialog;
        Context ctx;
        MyTask(Context ctx)
        {
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Sending....");
        }

        @Override
        protected String doInBackground(String... params) {
            String send_url ="http://3.135.54.193/TA/send.php";
            String method = params[0];
            if(method.equals("send"))
            {
                user_name=params[1];
                String stealth_key=params[2];
                String transfer =params[3];

                try {
                    URL url =new URL(send_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true); //Pass some Info to SQL
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("user_name","UTF-8") +"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                                  URLEncoder.encode("stealth_key","UTF-8") +"="+URLEncoder.encode(stealth_key,"UTF-8")+"&"+
                                  URLEncoder.encode("transfer","UTF-8") +"="+URLEncoder.encode(transfer,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    //This Code to Get the response
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "";
                    String line = "";
                    while((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    } // end of Async task

    public void onBackPressed(){
        if (canexit) {
            super.onBackPressed();

        }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false);
            btSend.setEnabled(true);
            spBauds.setEnabled(false);
            cbAutoscrolls.setEnabled(false);

        } else {
            btOpen.setEnabled(true);
            btSend.setEnabled(false);
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

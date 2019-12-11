
//How Shindeiru Toki Mode Work!!!----------------------------------------
//1. Send 219 to wallet
//2. wallet send username to insertkey_response variable on Android.
//then wallet is waiting for OK confirmation
//3. Android send username and insertkey_response parameter to server for checking.
//If true, return OK
//4. When wallet get OK, it rewrite its secret key
//------------------------------------------------------------------------
package com.e.mywallet;
import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;
//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
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

public class InsertKey extends Activity{
    Button btOpens,  btnKeys ,btTshoot;
    EditText nilaikey;
    TextView insertkey_response,insertkey_response2;
    Spinner spBauds;
    CheckBox cbAutoscrolls;
    ImageView imageback;
    boolean canexit=false;
    Physicaloid mPhysicaloid; // initialising library

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_key);

        imageback = (ImageView) findViewById(R.id.imageback);
        btOpens  = (Button) findViewById(R.id.btOpens);
        btnKeys = (Button) findViewById(R.id.buttonkeys);
        nilaikey = (EditText) findViewById(R.id.nilaikey);
        insertkey_response  = (TextView) findViewById(R.id.insertkey_response);
        insertkey_response2  = (TextView) findViewById(R.id.insertkey_response2);
        spBauds = (Spinner) findViewById(R.id.spinners);
        cbAutoscrolls = (CheckBox)findViewById(R.id.autoscrolls);
        setEnabledUi(false);
        mPhysicaloid = new Physicaloid(this);
        btTshoot = (Button)findViewById(R.id.InsertKey_btTshoot);

        btOpens.setOnClickListener(new View.OnClickListener()
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
                    String kirim = "219".toString(); //Mengirim case 4 ke while loop, seharusnya gabung connect btOpens.

                    if(kirim.length()>0) {
                        byte[] buf = kirim.getBytes();
                        mPhysicaloid.write(buf, buf.length);
                    }


                    if(mPhysicaloid.open()) { // Pesan ini diberikan wallet
                        byte[] bufs = new byte[256];
                        mPhysicaloid.read(bufs, bufs.length);
                        String str = new String(bufs);
                        insertkey_response.setText(null);
                        insertkey_response.setText(str); // ini pesannya contoh " Wallet sudah siap" .
                       // mPhysicaloid.close();
                    }
                    setEnabledUi(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btTshoot.setVisibility(View.VISIBLE);
                        }
                    }, 2000);

                    nilaikey.setVisibility(View.VISIBLE);
                    insertkey_response.setText(null);// Saat inilah baru bisa memasukkan key

                    // We expect wallet to directly send username parameter after Android send 219
                    if(cbAutoscrolls.isChecked())
                    {
                        insertkey_response.setMovementMethod(new ScrollingMovementMethod());
                    }
                    mPhysicaloid.addReadListener(new ReadLisener() {
                        @Override
                        public void onRead(int size) {
                            byte[] buf = new byte[size];
                            mPhysicaloid.read(buf, size);
                            tvAppend(insertkey_response, Html.fromHtml( new String(buf) ));
                        }
                    });
                } else {
                    insertkey_response.setText("Sambungin dulu Walletnya!");
                    Toast toast = Toast.makeText(getApplicationContext(),"Not Connect",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        // Below btnKeys is not used anymore
        btnKeys.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String kirim = nilaikey.getText().toString(); //Mengirim case 4 ke while loop, seharusnya gabung connect btOpens.
                if(kirim.length()>0) {
                    byte[] buf = kirim.getBytes();
                    mPhysicaloid.write(buf, buf.length);}
                nilaikey.setText("Done! Press the logo to Exit");
                nilaikey.setEnabled(false);
            }
        });

        imageback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String kirim = "moemoe";
                if(kirim.length()>0) {
                    byte[] buf = kirim.getBytes();
                    mPhysicaloid.write(buf, buf.length);}
                canexit=true;
                onBackPressed();
            }
        });
    }
    private void onBackgroundTaskDataObtained(String result) {
        insertkey_response2.setText(result); // Nanti ini kirim string OK ke WALLET // IMPORTANT CHANGE!!!!!!!!!!!
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String hasil = insertkey_response2.getText().toString(); // Server Message
                if(hasil.equals("OK")) {
                    String kirim = nilaikey.getText().toString(); // kirim inserted pkey bila server sudah OK
                    if (kirim.length() > 0) {
                        byte[] buf = kirim.getBytes();
                        mPhysicaloid.write(buf, buf.length);
                    }
                }
            }
        }, 2000);

    }

    // Tshoot with server
    public void onClickTshoot(View view) {
        String user_name = insertkey_response.getText().toString();
        String secret_key = nilaikey.getText().toString();
        String method = "shindeirutoki";
        new InsertKey.MyTask(this).execute(method,user_name, secret_key); //Jalankan AsyncTaskPertama
        btTshoot.setEnabled(false);
        nilaikey.setEnabled(false);
    }

    private class MyTask extends AsyncTask<String,Void,String>
    {
        Context ctx;
        MyTask(Context ctx)
        {
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String balance_url ="http://3.135.54.193/TA/shindeiru.php";
            String method = params[0];
            if(method.equals("shindeirutoki"))
            {
                String user_name= params[1];
                String secret_key = params[2];

                try {
                    URL url =new URL(balance_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                            URLEncoder.encode("secret_key","UTF-8")+"="+URLEncoder.encode(secret_key,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    /// This is to get response from Server
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
            //float f=Float.parseFloat(result);
            //String fromfloat=""+f;
            Toast.makeText(ctx,result, Toast.LENGTH_SHORT).show();
            InsertKey.this.onBackgroundTaskDataObtained(result);
        }
    } // Akhir dari AsyncTask



    public void onBackPressed(){
       if (canexit) {
           super.onBackPressed();
           mPhysicaloid.close();
       }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpens.setEnabled(false);
            spBauds.setEnabled(false);
            cbAutoscrolls.setEnabled(false);
            nilaikey.setEnabled(true);

        } else {
            btOpens.setEnabled(true);
            spBauds.setEnabled(true);
            cbAutoscrolls.setEnabled(true);
            nilaikey.setEnabled(false);
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

package com.e.mywallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import android.os.Handler;

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

public class Check extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    Button btCek, btOpen;
    TextView tvResult,tvResponse,tvTulisanBesar;
    String firstOcc;
    ImageView imagebackpin;
    boolean canexit = false;
    Spinner spBauds;
    CheckBox cbAutoscrolls;
    Physicaloid mPhysicaloid; // initialising library
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        btCek=(Button)findViewById(R.id.Check_Submit);
        tvResult=(TextView)findViewById(R.id.Check_key_tv);
        tvResponse=(TextView)findViewById(R.id.Check_Response);
        imagebackpin=(ImageView)findViewById(R.id.Check_image);
     //   tvTulisanBesar=(TextView)findViewById(R.id.Check_TulisanBesar);

        btOpen =(Button)findViewById(R.id.Check_Connect);
        spBauds = (Spinner) findViewById(R.id.Check_spinnerz);
        cbAutoscrolls = (CheckBox)findViewById(R.id.Check_autoscrollz);
        //-----------------------------------------------
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
                    String kirim = "7"; //Mengirim case 7 ke while loop
                    if(kirim.length()>0) {
                        byte[] buf = kirim.getBytes();
                        mPhysicaloid.write(buf, buf.length);
                    }


                   /* if(mPhysicaloid.open()) { // Pesan ini diberikan wallet

                    }*/
                   /* byte[] bufs = new byte[256];
                    mPhysicaloid.read(bufs, bufs.length);
                    String str = new String(bufs);
                    tvRegResponse.setText(null);
                    tvRegResponse.setText(str); // ini pesannya contoh " privatekey yang akan didaftarkan" .
                    pkNew=tvRegResponse.getText().toString();
                    // mPhysicaloid.close();*/

                    if(cbAutoscrolls.isChecked())
                    {
                        tvResponse.setMovementMethod(new ScrollingMovementMethod());
                    }
                    mPhysicaloid.addReadListener(new ReadLisener() {
                        @Override
                        public void onRead(int size) {
                            byte[] buf = new byte[size];
                            mPhysicaloid.read(buf, size);
                            tvAppend(tvResponse, Html.fromHtml( new String(buf) ));
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
    } //End Of Protected Void


    @Override
    public void applyTexts(String username, String password) {
        tvResponse.setText(username);
        // Mengirim PIN ke ESP32
        String kirim = tvResponse.getText().toString();
        if(kirim.length()>0) {
            byte[] buf = kirim.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
        tvResponse.setText(null);

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



    public void CheckButton(View view) { //onClick Check
        String user_name = tvResponse.getText().toString(); // !!!!Query LAH DARI ARDUINO
        String method = "balance";
        new MyTask(this).execute(method,user_name); //Jalankan AsyncTaskPertama
        // Harus ada Delay dengan handler , kalau tidak , belum keupdate nya.

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                firstOcc=tvResult.getText().toString();
                float f=Float.parseFloat(firstOcc)*2;
                String fromfloat=""+f;
                tvResultDouble.setText(fromfloat);
            }
        }, 2000);*/

        firstOcc=tvResult.getText().toString();
      //  tvTulisanBesar.setText(firstOcc);

        //float f=Float.parseFloat(result);
        //String fromfloat=""+f;
        //Toast.makeText(ctx,fromfloat, Toast.LENGTH_SHORT).show();

    }

    private void onBackgroundTaskDataObtained(String result) {
        //do stuff with the results here..
        tvResult.setText(result);
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
            String balance_url ="http://18.189.104.59/TA/balance.php";
            String method = params[0];
            if(method.equals("balance"))
            {
                String user_name= params[1];

                try {
                    URL url =new URL(balance_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8");
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
            //Toast.makeText(ctx,fromfloat, Toast.LENGTH_SHORT).show();
            Check.this.onBackgroundTaskDataObtained(result);
        }
    } // Akhir dari AsyncTask

    public void onBackPressed(){
        if (canexit) {
            super.onBackPressed();

        }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false);
            btCek.setEnabled(true);
            spBauds.setEnabled(false);
            cbAutoscrolls.setEnabled(false);

        } else {
            btOpen.setEnabled(true);
            btCek.setEnabled(false);
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

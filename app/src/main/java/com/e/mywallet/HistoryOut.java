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
import android.support.v7.app.AppCompatActivity;
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

public class HistoryOut extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    Button btOpens ,btRetrieve;
    TextView HistoryOut_response;
    Spinner spBauds;
    CheckBox cbAutoscrolls;
    ImageView imageback;
    boolean canexit=false;
    Physicaloid mPhysicaloid; // initialising library
    String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_out);

        imageback = (ImageView) findViewById(R.id.HistoryOut_imageback);
        btOpens  = (Button) findViewById(R.id.HistoryOut_btOpens);
        HistoryOut_response  = (TextView) findViewById(R.id.HistoryOut_response);
        spBauds = (Spinner) findViewById(R.id.HistoryOut_spinners);
        cbAutoscrolls = (CheckBox)findViewById(R.id.HistoryOut_autoscrolls);
        setEnabledUi(false);
        mPhysicaloid = new Physicaloid(this);
        btRetrieve = (Button)findViewById(R.id.HistoryOut_btRetrieve);

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
                    String kirim = "12".toString(); //Mengirim case 11 ke while loop, seharusnya gabung connect btOpens.

                    if(kirim.length()>0) {
                        byte[] buf = kirim.getBytes();
                        mPhysicaloid.write(buf, buf.length);
                    }

                    setEnabledUi(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btOpens.setVisibility(View.GONE);
                            btRetrieve.setVisibility(View.VISIBLE);
                        }
                    }, 2000);
                    HistoryOut_response.setText(null);// Saat inilah baru bisa memasukkan key
                    openDialog();
                    // We expect wallet to directly send username parameter after Android send 219
                    if(cbAutoscrolls.isChecked())
                    {
                        HistoryOut_response.setMovementMethod(new ScrollingMovementMethod());
                    }
                    mPhysicaloid.addReadListener(new ReadLisener() {
                        @Override
                        public void onRead(int size) {
                            byte[] buf = new byte[size];
                            mPhysicaloid.read(buf, size);
                            tvAppend(HistoryOut_response, Html.fromHtml( new String(buf) ));
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Not Connect",Toast.LENGTH_SHORT);
                    toast.show();
                }
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

    @Override
    public void applyTexts(String username, String password) {
        HistoryOut_response.setText(username);
        // Mengirim PIN ke ESP32
        String kirim = HistoryOut_response.getText().toString();
        if(kirim.length()>0) {
            byte[] buf = kirim.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
        HistoryOut_response.setText(null);
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

    // Tshoot with server
    public void onClickRetrieve(View view) {
        String user_name = HistoryOut_response.getText().toString(); // !!!!Query LAH DARI ARDUINO
        String method = "historyout";
        new HistoryOut.MyTask(this).execute(method,user_name); //Jalankan AsyncTaskPertama
    }

    class MyTask extends AsyncTask<String,Void,String>
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
            String balance_url ="http://3.135.54.193/TA/historyout.php";
            String method = params[0];
            if(method.equals("historyout"))
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
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    while((JSON_STRING = bufferedReader.readLine())!=null)
                    {
                        stringBuilder.append(JSON_STRING+"\n");
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();

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
            HistoryOut_response.setText(result);
        }
    } // Akhir dari AsyncTask*/



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

        } else {
            btOpens.setEnabled(true);
            spBauds.setEnabled(true);
            cbAutoscrolls.setEnabled(true);
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

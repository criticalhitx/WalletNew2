//---- Pada Menu ini, user mengganti username dan passwordnya apabila private key didapatkan

package com.e.mywallet;
import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.view.View.GONE;

public class RecoverSK extends AppCompatActivity {
    ImageView imagebackpin;
    EditText etUsername,etPassword, etInsertedSK; // Input parameter from user
    TextView tvTest; // response
    Button btVerify,btGetPub, btOpen; // btVerify untuk cek ke server, PutWallet untuk taruh wallet, open untuk conect
//-------------------------------
    String new_user_name,new_password,GeneratedPK;
    ///---------------------------
    boolean canexit = false;
    Spinner spBauds;
    CheckBox cbAutoscrolls;
    Physicaloid mPhysicaloid; // initialising library


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_sk);

        etUsername=(EditText)findViewById(R.id.RecoverSK_newUsername);
        etPassword=(EditText)findViewById(R.id.RecoverSK_newPassword);
        etInsertedSK=(EditText)findViewById(R.id.RecoverSK_nilaisk);
        //---------------------------------------------
        imagebackpin=(ImageView)findViewById(R.id.RecoverSK_image);
        tvTest=(TextView)findViewById(R.id.RecoverSK_testResponse);//response
        btOpen =(Button)findViewById(R.id.RecoverSK_connect); //Tombol Connect
        spBauds = (Spinner) findViewById(R.id.RecoverSK_spinners);
        cbAutoscrolls = (CheckBox)findViewById(R.id.RecoverSK_autoscrolls);
        btGetPub = (Button)findViewById(R.id.RecoverSK_btGetPub); // Send button
        btVerify = (Button)findViewById(R.id.RecoverSK_btCheckValidity); // Check Validity button
        btOpen = (Button)findViewById(R.id.RecoverSK_connect);
        //-------------------------------------
        etUsername.setFilters(new InputFilter[] {new InputFilter.LengthFilter(13)});
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
                    String kirim = "8"; //Mengirim case 8 ke while loop [ Mode Restore SK ]

                    if(kirim.length()>0) {
                        byte[] buf = kirim.getBytes();
                        mPhysicaloid.write(buf, buf.length);
                    }

                    etUsername.setEnabled(false);
                    etPassword.setEnabled(false);
                    etInsertedSK.setEnabled(false);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btOpen.setVisibility(GONE);
                            btGetPub.setVisibility(View.VISIBLE);
                        }
                    }, 1000);

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

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Not Connect",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btGetPub.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String kirim =etInsertedSK.getText().toString(); // Send Secret key to wallet
                if(kirim.length()>0) {
                    byte[] buf = kirim.getBytes();
                    mPhysicaloid.write(buf, buf.length);}

                btGetPub.setVisibility(GONE);
                etInsertedSK.setVisibility(GONE);
                etUsername.setVisibility(GONE);
                etPassword.setVisibility(GONE);
                //Time is need for wallet calculation
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        btVerify.setVisibility(View.VISIBLE);
                    }
                }, 3000);
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

    public void onBackPressed(){
        if (canexit) {
            super.onBackPressed();

        }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false); // Connect
           // etInsertedSK.setEnabled(true);
           // etUsername.setEnabled(true);
           // etPassword.setEnabled(true);
            spBauds.setEnabled(false);
            cbAutoscrolls.setEnabled(false);


        } else {
            btOpen.setEnabled(true);
          //  etInsertedSK.setEnabled(false);
           // etUsername.setEnabled(false);
           // etPassword.setEnabled(false);
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

    ///--- This procedure is done later
    public void RecoverSK_btCheckValidity(View view) {
        new_user_name = etUsername.getText().toString();
        new_password = etPassword.getText().toString();
        GeneratedPK =  tvTest.getText().toString();

        String method = "recoversk";
        new RecoverSK.MyTask(this).execute(method,new_user_name,new_password,GeneratedPK);
        btVerify.setEnabled(false);

    }

    class MyTask extends AsyncTask<String,Void,String>
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
            String send_url ="http://3.135.54.193/TA/recoversk.php";
            String method = params[0];
            if(method.equals("recoversk"))
            {
                String user_name=params[1];
                String user_pass=params[2];
                String public_key =params[3];

                try {
                    URL url =new URL(send_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true); //Pass some Info to SQL
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("user_name","UTF-8") +"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                            URLEncoder.encode("user_pass","UTF-8") +"="+URLEncoder.encode(user_pass,"UTF-8")+"&"+
                            URLEncoder.encode("public_key","UTF-8") +"="+URLEncoder.encode(public_key,"UTF-8");
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
               // Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();

            if(result.equals("Sama"))
            {
                String kirim = etUsername.getText().toString(); // Bila Sama kirim user
                if(kirim.length()>0) {
                    byte[] buf = kirim.getBytes();
                    mPhysicaloid.write(buf, buf.length);}

                alertDialog.setMessage(result);
                alertDialog.show();
            }
            else
            {
                String kirim = "Beda";
                if(kirim.length()>0) {
                    byte[] buf = kirim.getBytes();
                    mPhysicaloid.write(buf, buf.length);}

                alertDialog.setMessage(result);
                alertDialog.show();
            }

        }
    }
} // end of Async task

package com.e.mywallet;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class BackgroundTask extends AsyncTask<String,Void,String>{


    AlertDialog alertDialog;
    Context ctx;
    BackgroundTask(Context ctx)
    {
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information....");
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url ="http://3.135.54.193/TA/register.php";
        String login_url ="http://3.135.54.193/TA/login.php";
        String receive_url ="http://3.135.54.193/TA/receive.php";
        String method =params[0];
        if(method.equals("register"))
        {
            String firstname = params[1];
            String lastname = params[5];
            String user_name = params[2];
            String user_pass = params[3];
            String email = params[4];
            String createdPKey=params[6];

            try {
                URL url =new URL(reg_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true); //Pass some Info to SQL
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("firstname","UTF-8") +"="+URLEncoder.encode(firstname,"UTF-8")+"&"+
                              URLEncoder.encode("user_name","UTF-8") +"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                              URLEncoder.encode("user_pass","UTF-8") +"="+URLEncoder.encode(user_pass,"UTF-8")+"&"+
                              URLEncoder.encode("p_key","UTF-8") +"="+URLEncoder.encode(createdPKey,"UTF-8")+"&"+
                              URLEncoder.encode("lastname","UTF-8") +"="+URLEncoder.encode(lastname,"UTF-8")+"&"+
                              URLEncoder.encode("email","UTF-8") +"="+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream(); //But no server Response
                IS.close();
                return "Registration Success!!";

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("login"))
        {
            String login_name = params[1];
            String login_pass = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("login_name","UTF-8")+"="+URLEncoder.encode(login_name,"UTF-8")+"&"+
                              URLEncoder.encode("login_pass","UTF-8")+"="+URLEncoder.encode(login_pass,"UTF-8");
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
        else if(method.equals("receive"))
        {
            String user_name = params[1];
            String r = params[2];
            try {
                URL url = new URL(receive_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                        URLEncoder.encode("r","UTF-8")+"="+URLEncoder.encode(r,"UTF-8");
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
        if(result.equals("Registration Success!!"))
        {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }
       else if(result.equals("Login Failed....Try Again.."))
       {
           alertDialog.setMessage(result);
           alertDialog.show();

       }
       else if(result.equals("Login Success....Welcome!"))
        {
            //// This is to Start New Intent from Backgroujnd
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent I2= new Intent(ctx, Menu.class);
                    I2.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK); // | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(I2);
                }
            }, 3000);

        }
       else
        {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }
    }

}

package com.e.mywallet;
import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;
//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
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

public class Register extends AppCompatActivity {

    EditText etUsername, etPassword, etEmail,etFirstName,etLastName;
    TextView tv1,tv2,tv3,tv4,tv5,tvRegResponse;
    Spinner spBauds;
    CheckBox cbAutoscrolls;
    String firstname,lastname,user_name,user_pass,email,createdPKey;
    Button btnReg,btOpen;
    Physicaloid mPhysicaloid; // initialising library
    boolean canexit=false;
    ImageView imagebackPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername=(EditText)findViewById(R.id.registerUsername);
        etPassword=(EditText)findViewById(R.id.registerPassword);
        etEmail=(EditText)findViewById(R.id.registerEmail);
        etFirstName=(EditText)findViewById(R.id.registerFirstName);
        etLastName = (EditText)findViewById(R.id.registerLastName);

        btnReg = (Button)findViewById(R.id.registerBtnReg);
        imagebackPin=(ImageView) findViewById(R.id.Register_Image);


        tv1=(TextView)findViewById(R.id.registerText1);
        tv2=(TextView)findViewById(R.id.registerText2);
        tv3=(TextView)findViewById(R.id.registerText3);
        tv4=(TextView)findViewById(R.id.registerText4);
        tv5=(TextView)findViewById(R.id.registerText5);

        btOpen =(Button)findViewById(R.id.Register_Open);
        spBauds = (Spinner) findViewById(R.id.Register_spinnerz);
        cbAutoscrolls = (CheckBox)findViewById(R.id.Register_autoscrollz);
        tvRegResponse = (TextView)findViewById(R.id.registerResponse);
        /// Set UI Awal ini --------
        etUsername.setFilters(new InputFilter[] {new InputFilter.LengthFilter(13)});
        setEnabledUi(false);
        btnReg.setEnabled(false);
        /// -----------------------

        mPhysicaloid = new Physicaloid(this);
        Toast.makeText(this, "by Pressing Connect Button, it will erase the data inside wallet", Toast.LENGTH_LONG).show();

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
                    btnReg.setEnabled(true);
                    String kirim = "3"; //Mengirim case 3 ke while loop
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
                        tvRegResponse.setMovementMethod(new ScrollingMovementMethod());
                    }
                    mPhysicaloid.addReadListener(new ReadLisener() {
                        @Override
                        public void onRead(int size) {
                            byte[] buf = new byte[size];
                            mPhysicaloid.read(buf, size);
                            tvAppend(tvRegResponse, Html.fromHtml( new String(buf) ));
                        }
                    });

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Not Connect",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


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



    // Di luar protected void
    public void userReg(View view)
    {
        if(canRegis())
        {
            createdPKey = tvRegResponse.getText().toString();
            // ----- UPDATE USERNAME KE WALLET SAAT REGISTRASI ----
            if(user_name.length()>0) {
                byte[] buf = user_name.getBytes();
                mPhysicaloid.write(buf, buf.length);
            }
            // ---------------------------------------------------
            String method ="register";
            BackgroundTask backgroundTask =new BackgroundTask(this);
            backgroundTask.execute(method,firstname,user_name,user_pass,email,lastname,createdPKey);
            btnReg.setEnabled(false);
            tv1.setText("Write Down Private Key in a Paper for Highest Security");
            tv1.setVisibility(View.VISIBLE);
            tv1.setBackgroundColor(getColor(R.color.white));
        }
    }

    boolean canRegis()
    {
        firstname=etFirstName.getText().toString();
        lastname=etLastName.getText().toString();
        user_name=etUsername.getText().toString();
        user_pass=etPassword.getText().toString();
        email=etEmail.getText().toString();

       if(etUsername.length()==0)
       {
           Toast.makeText(Register.this,"Username can't be empty!", Toast.LENGTH_LONG).show();
           return false;
       }
       else if(etPassword.length()==0)
       {
           Toast.makeText(Register.this,"Password can't be empty!", Toast.LENGTH_LONG).show();
           return false;
       }
       else if(etEmail.length()==0)
       {
           Toast.makeText(Register.this,"Email can't be empty!", Toast.LENGTH_LONG).show();
           return false;
       }
       else if(etFirstName.length()==0)
       {
           Toast.makeText(Register.this,"FirstName can't be empty!", Toast.LENGTH_LONG).show();
           return false;
       }
       else if(etLastName.length()==0)
       {
           Toast.makeText(Register.this,"LastName can't be empty!", Toast.LENGTH_LONG).show();
           return false;
       }
       return true;
    }

    public void onBackPressed(){
        if (canexit) {
            super.onBackPressed();

        }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false);
            btnReg.setEnabled(true);
            spBauds.setEnabled(false);
            cbAutoscrolls.setEnabled(false);
            tvRegResponse.setEnabled(true);
            etLastName.setEnabled(true);
            etFirstName.setEnabled(true);
            etPassword.setEnabled(true);
            etEmail.setEnabled(true);
            etUsername.setEnabled(true);
        } else {
            btOpen.setEnabled(true);
            btnReg.setEnabled(false);
            spBauds.setEnabled(true);
            cbAutoscrolls.setEnabled(true);
            tvRegResponse.setEnabled(false);
            etLastName.setEnabled(false);
            etFirstName.setEnabled(false);
            etPassword.setEnabled(false);
            etEmail.setEnabled(false);
            etUsername.setEnabled(false);
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

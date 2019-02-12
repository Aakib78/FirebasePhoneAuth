package com.example.aas.firebasephoneauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import com.hbb20.CountryCodePicker;

public class StartApp extends AppCompatActivity {

    CountryCodePicker ccp;
    private EditText phoneNo;
    private Button btnSendOTP;

    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        phoneNo=(EditText) findViewById(R.id.phoneText);
        btnSendOTP=(Button) findViewById(R.id.sendOTP);


        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneNo);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                Toast.makeText(StartApp.this, "Country Changed.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number=ccp.getFullNumberWithPlus();

                if(phoneNo==null || phoneNo.length()<10){
                    Toast.makeText(StartApp.this, "Enter valid phone number.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(StartApp.this, OTPActivity.class);
                    i.putExtra("number", number);
                    startActivity(i);
                }
            }
        });
    }
}

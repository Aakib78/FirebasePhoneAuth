package com.example.aas.firebasephoneauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private FirebaseAuth mAuth;
    private Button map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupToolBar();
        mAuth = FirebaseAuth.getInstance();
        map=(Button)findViewById(R.id.map);
        navigationView=(NavigationView)findViewById(R.id.navigationView);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.signOut:
                        mAuth.signOut();
                        Intent intent=new Intent(MainActivity.this,StartApp.class);
                        startActivity(intent);
                        finish();
                        break;

                }
                return false;
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent=new Intent(MainActivity.this,MapsActivity.class);
                startActivity(mapIntent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser==null){
            Intent intent=new Intent(MainActivity.this,StartApp.class);
            startActivity(intent);
            finish();
        }

    }

    private void setupToolBar(){
        drawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.AppName,R.string.AppName);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }
}

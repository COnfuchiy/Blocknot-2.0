package com.example.oxsoska;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button create_btn;
    private Button read_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_file:{
                Intent intent = new Intent(MainActivity.this,CreateFilesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.read_file:{
                Intent intent = new Intent(MainActivity.this,ReadFilesActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
package com.example.oxsoska;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EditorActivity extends AppCompatActivity {

    private String current_filename;
    private EditText mEditText;
    private enum Encoding{
        UTF8,
        UTF16,
        US_ASCII,
    };
    private Encoding current_encoding;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        current_filename = (String) getIntent().getSerializableExtra("absolute_filename");
        boolean is_create = (Boolean) getIntent().getSerializableExtra("is_new_file");
        mEditText =  findViewById(R.id.editText);
        current_encoding = Encoding.UTF8;
        if(!is_create){
            openFile(current_filename);
        }
        else {
            saveFile(current_filename);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveFile(current_filename);
                return true;
            case R.id.action_encode:
                switch_encode();
                return true;
            default:
                return true;
        }
    }

    private void switch_encode(){
        switch (current_encoding){
            case UTF8:{
                current_encoding = Encoding.UTF16;
                Toast.makeText(getApplicationContext(),
                        "Текущая кодировка: UTF16", Toast.LENGTH_LONG).show();
                break;
            }
            case UTF16:{
                current_encoding = Encoding.US_ASCII;
                Toast.makeText(getApplicationContext(),
                        "Текущая кодировка: US_ASCII", Toast.LENGTH_LONG).show();
                break;
            }
            case US_ASCII:{
                current_encoding = Encoding.UTF8;
                Toast.makeText(getApplicationContext(),
                        "Текущая кодировка: UTF8", Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Charset get_current_encode(){
        switch (current_encoding){
            case UTF8:
                return StandardCharsets.UTF_8;
            case UTF16:
                return StandardCharsets.UTF_16;
            case US_ASCII:
                return StandardCharsets.US_ASCII;
        }
        return StandardCharsets.UTF_8;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openFile(String fileName) {
        try {
            FileInputStream inputStream = new FileInputStream (new File(fileName));

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                inputStream.close();
                mEditText.setText(builder.toString());
            }
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // Метод для сохранения файла
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveFile(String fileName) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            FileOutputStream outputStream = new FileOutputStream (new File(fileName));
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, get_current_encode());
            osw.write(mEditText.getText().toString());
            osw.close();
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void onBackPressed(){
        Intent intent = new Intent(EditorActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
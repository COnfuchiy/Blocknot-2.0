package com.example.oxsoska;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReadFilesActivity extends AppCompatActivity {

    private ListView list_view;
    private ArrayAdapter<String> main_files_array;
    private String root_path;
    private String prev_path;
    private String current_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_files);
        list_view = findViewById(R.id.main_list_files);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                handle_click(((TextView) itemClicked).getText().toString());
            }});
        root_path = current_path = prev_path = Environment.getExternalStorageDirectory().getPath();
        set_files_list(getFiles(current_path));
    }

    private void handle_click(String clicked_filename){
        prev_path = current_path;
        current_path+="/"+clicked_filename;
        File clicked_file = new File(current_path);
        if (clicked_file.isDirectory()){
            set_files_list(getFiles(current_path));
        }
        else if (clicked_file.isFile()){
            Intent intent = new Intent(ReadFilesActivity.this,EditorActivity.class);
            intent.putExtra("is_new_file",false);
            intent.putExtra("absolute_filename",current_path);
            startActivity(intent);
        }
    }

    private String[] getFiles(String directoryPath){
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files!=null){
            String[] result = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                result[i] = files[i].getName();
            }
            Arrays.sort(result, new Comparator<String>() {
                @Override
                public int compare(String file, String file2) {
                    return file.compareTo(file2);
                }
            });
            return result;
        }
        return new String[]{};
    }

    private void set_files_list(String[] files_array){
        if (files_array!=null){
            main_files_array = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, files_array);
            list_view.setAdapter(main_files_array);
        }
    }

    @Override
    public void onBackPressed(){
        if (!current_path.equals(root_path)){
            current_path = prev_path;
            prev_path = prev_path.substring(0,prev_path.lastIndexOf('/'));
            set_files_list(getFiles(current_path));
        }
        else
            super.onBackPressed();
    }
}
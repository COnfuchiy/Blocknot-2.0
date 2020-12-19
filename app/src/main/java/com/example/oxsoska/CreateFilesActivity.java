package com.example.oxsoska;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class CreateFilesActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView list_view;
    private ArrayAdapter<String> main_files_array;
    private String root_path;
    private String prev_path;
    private String current_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_files);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_file_button:{
                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.new_file_dialog, null);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final EditText userInput = promptsView.findViewById(R.id.input_text);

                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        //Вводим текст и отображаем в строке ввода на основном экране:
                                        String new_filename = current_path+"/" + userInput.getText();
                                        Intent intent = new Intent(CreateFilesActivity.this,EditorActivity.class);
                                        intent.putExtra("absolute_filename",new_filename);
                                        intent.putExtra("is_new_file",true);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();

                //и отображаем его:
                alertDialog.show();
            }
        }
    }
}
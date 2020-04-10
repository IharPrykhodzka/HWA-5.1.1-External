package com.example.hwa51external;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 10;
    private String FILE_NAME = "text.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_STORAGE);

        }

        initList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                }
        }
    }

    private void initList() {
        final ListView listView = findViewById(R.id.list_view);
        final List<DataItems> dataItemsList = new ArrayList<>();


        int indexTitle = 0;
        int indexSubTitle = 1;
//        int indexImage = 2;
        int image = R.mipmap.ic_launcher;

        while (true) {


            if (stringList().size() < indexSubTitle) {
                break;
            } else {


                dataItemsList.add(new DataItems(stringList().get(indexTitle), stringList().get(indexSubTitle),
                        image, false));

                indexTitle = indexTitle + 3;
                indexSubTitle = indexSubTitle + 3;
//                indexImage = indexImage + 3;
            }
        }

        final DataItemsAdapter dataItemsAdapter = new DataItemsAdapter(dataItemsList, this);
        listView.setAdapter(dataItemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object object = dataItemsList.get(position);

                switch (object.toString()) {
                    case "Записная книжка":
                        Intent intentNotes = new Intent(MainActivity.this, NotesActivity.class);
                        startActivity(intentNotes);
                        break;
                    case "Календарь":
                        Intent intentCalendar = new Intent(MainActivity.this, CalendarActivity.class);
                        startActivity(intentCalendar);
                        break;
                    case "Адресс":
                        Intent intentAddress = new Intent(MainActivity.this, AddressActivity.class);
                        startActivity(intentAddress);
                        break;
                    case "Настройки":
                        Toast.makeText(MainActivity.this, R.string.txtOpenSettings, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Object object = dataItemsList.get(position);

                Toast.makeText(MainActivity.this, object.toString(), Toast.LENGTH_LONG).show();

                return false;
            }
        });
    }

    private String loadText() {
        StringBuilder result = null;
        Context context = this;
        if (isExternalStorageWritable()) {

            File textFile = new File(context.getExternalFilesDir(null), FILE_NAME);
            result = new StringBuilder();

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                    result.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert result != null;
        return result.toString();
    }

    private ArrayList<String> stringList() {

        String str = loadText();
        str = str.trim();
        String[] arr = str.split(",");

        return new ArrayList<>(Arrays.asList(arr));
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


}

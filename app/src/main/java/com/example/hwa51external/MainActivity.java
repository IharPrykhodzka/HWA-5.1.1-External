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

                    initList();

                }
        }
    }

    private void initList() {
        final ListView listView = findViewById(R.id.list_view);
        final List<DataItems> dataItemsList = new ArrayList<>();

        ArrayList<String> listTextFile = new ArrayList<>(stringList());

        int indexTitle = 0;
        int indexSubTitle = 1;
        int indexImage = 2;
        int indexChecked = 3;

        while (listTextFile.get(indexTitle) != null) {

            dataItemsList.add(new DataItems(listTextFile.get(indexTitle), listTextFile.get(indexSubTitle),
                    Integer.parseInt(listTextFile.get(indexImage)), Boolean.parseBoolean(listTextFile.get(indexChecked))));

            indexTitle = indexTitle + 4;
            indexSubTitle = indexSubTitle + 4;
            indexImage = indexImage + 4;
            indexChecked = indexChecked + 4;

        }


//        dataItemsList.add(new DataItems("Записная книжка", "Из задания № 2.2.1",
//                R.drawable.note_background, false));
//        dataItemsList.add(new DataItems("Календарь", "Из задания № 2.1.3",
//                R.drawable.calendar_background, false));
//        dataItemsList.add(new DataItems("Адресс", "Из задания № 2.1.2",
//                R.drawable.address_background, false));
//        dataItemsList.add(new DataItems("Настройки", "Из задания № 2.2.2",
//                R.drawable.settings_background, false));


        final DataItemsAdapter dataItemsAdapter = new DataItemsAdapter(dataItemsList, this);
        listView.setAdapter(dataItemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object object = dataItemsList.get(position);
                String massage = object.toString();


                switch (massage) {
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
                String massage = object.toString();

                Toast.makeText(MainActivity.this, massage, Toast.LENGTH_LONG).show();

                return false;
            }
        });
    }

    private String loadText() {
        StringBuilder result = null;
        if (isExternalStorageWritable()) {

            Context context = this;

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

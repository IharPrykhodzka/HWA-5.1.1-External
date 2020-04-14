package com.example.hwa51external;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements DeleteListener {

    private String FILE_NAME = "text.txt";
    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createText();
        initList();
        newPoint();
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
        int image = R.mipmap.ic_launcher;

        ArrayList<String> strings = stringList();
        if (strings.size() > 1) {
            for (int i = 0; i < strings.size(); i += 2) {
                dataItemsList.add(new DataItems(strings.get(i), strings.get(i + 1), image, false));
            }
        }

        final DataItemsAdapter dataItemsAdapter = new DataItemsAdapter(dataItemsList, this, this);
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

    private void createText() {
        File textFile = new File(getExternalFilesDir(null), FILE_NAME);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(textFile))) {
            bufferedWriter.write("Записная книжка,Из задания № 2.2.1,Календарь,Из задания № 2.1.3," +
                    "Адресс,Из задания № 2.1.2,Настройки,Из задания № 2.2.2,");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadText() {
        StringBuilder result = null;
        if (isExternalStorageWritable()) {
            File textFile = new File(getExternalFilesDir(null), FILE_NAME);
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

    private void newPoint() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExternalStorageWritable()) {
                    File textFile = new File(getExternalFilesDir(null), FILE_NAME);
                    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(textFile, true))) {
                        bufferedWriter.append("Автор,Приходько Игорь,");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    initList();
                }
            }
        });
    }

    private ArrayList<String> stringList() {
        String str = loadText();
        str = str.trim();
        String[] arr = str.split(",");
        return new ArrayList<>(Arrays.asList(arr));
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override
    public void onDelete(int position) {
        ArrayList strings = stringList();
        strings.remove(position);
        strings.remove(position);
        if (isExternalStorageWritable()) {
            File textFile = new File(getExternalFilesDir(null), FILE_NAME);
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(textFile))) {
                for (int i = 0; i < strings.size(); i++) {
                    bufferedWriter.write(strings.get(i) + ",");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            initList();
        }
    }
}

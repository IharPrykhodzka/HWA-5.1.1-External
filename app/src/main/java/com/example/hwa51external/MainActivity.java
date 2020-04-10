package com.example.hwa51external;
import android.content.Context;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private String FILE_NAME = "text.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        createText();
        initList();
    }
    private void initList() {
        final ListView listView = findViewById(R.id.list_view);
        final List<DataItems> dataItemsList = new ArrayList<>();
        int image = R.mipmap.ic_launcher;

        ArrayList<String> strings = stringList();
        for (int i = 0; i < strings.size(); i+=2) {
            dataItemsList.add(new DataItems(strings.get(i), strings.get(i + 1), image, false));
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

    private void createText() {
        File textFile = new File(getExternalFilesDir(null), FILE_NAME);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(textFile))) {
            bufferedWriter.write("Записная книжка,Из задания № 2.2.1,Календарь,Из задания № 2.1.3," +
                                        "Адресс,Из задания № 2.1.2,Настройки,Из задания № 2.2.2");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
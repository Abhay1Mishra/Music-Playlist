package com.example.mysound;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listV;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listV = findViewById(R.id.listV);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //Toast.makeText(MainActivity.this, "RunTime permission is given", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mysong = fetchsong(Environment.getExternalStorageDirectory());
                        String[] items = new String[mysong.size()];
                        for (int i =0;i<mysong.size();i++){
                            items[i] = mysong.get(i).getName().replace(".mp3"," ");
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                            listV.setAdapter(arrayAdapter);
                            listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent =new Intent(MainActivity.this,PlaySong.class);
                                    String currentSong = listV.getItemAtPosition(i).toString();
                                    intent.putExtra("Song list",mysong);
                                    intent.putExtra("currentSong",currentSong);
                                    intent.putExtra("Possession",i);
                                    startActivity(intent);
                                }
                            });

                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.cancelPermissionRequest();
                    }
                })
                .check();
    }
    public ArrayList<File> fetchsong(File file){
       ArrayList arrayList= new ArrayList();
       File[] songs = file.listFiles();
       if (songs!=null){
           for (File myFile:songs){
               if (!myFile.isHidden() && myFile.isDirectory()){
                   arrayList.addAll(fetchsong(myFile));
               }
                else {
                    if (myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
               }
           }
       }
       return arrayList;
    }


}
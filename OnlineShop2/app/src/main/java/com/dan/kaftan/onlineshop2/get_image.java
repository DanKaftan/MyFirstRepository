package com.dan.kaftan.onlineshop2;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class get_image extends AppCompatActivity {


    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView [] imageViews = new ImageView[4];

    TextView tv;
    String imageNamesFile = "images_names.txt";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://online-shop-a32c0.appspot.com");


    List<String> images_names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);

        imageView1 = (ImageView) findViewById(R.id.iv1);
        imageView2 = (ImageView) findViewById(R.id.iv2);
        imageView3 = (ImageView) findViewById(R.id.iv3);
        imageView4 = (ImageView) findViewById(R.id.iv4);

        imageViews[0]= imageView1;
        imageViews[1]= imageView2;
        imageViews[2]= imageView3;
        imageViews[3]= imageView4;


        tv = (TextView) findViewById(R.id.tv);

        downloadImages();
    }

    private void downloadImages(){


        storageRef.child(imageNamesFile).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {

                    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

                    while (true) {
                        String line;
                        try {
                            line = bufferedReader.readLine();
                            if (line == null){
                                break;
                            }
                            System.out.println(line);
                        } catch (NullPointerException e) {
                            bufferedReader.close();
                            break;
                        }

                        images_names.add(line);
                    }

                    int i =0;
                    for (String line : images_names){
                        downloadImage("items_images", line, imageViews[i]);
                        i++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);


                }

            }
        });

    }


    private void downloadImage(String folder, String imageName, final ImageView imageView) {

        storageRef.child(folder + "/" +imageName).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                try {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


}


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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class get_image extends AppCompatActivity {


    ImageView imageView;
    Bitmap bitmap;
    int itemsNum = 4;
    int i;
    ProgressDialog pd;
    Uri file;

    String [] images_names = new String[itemsNum];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);


        imageView = (ImageView) findViewById(R.id.iv);

        setImagesFiles();

       for (i=0; i <itemsNum; i++) {

            downloadImage();

       }

   }



    private void downloadImage() {


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://online-shop-a32c0.appspot.com");


        storageRef.child("items_images/"+images_names[i]).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                try {


                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bmp);

                    wait(2000);



                } catch (Exception e) {
                    e.printStackTrace();

                    //     pd.dismiss();

                }

            }
        });
    }



    private void setImagesFiles (){

        images_names[0] = "board.jpg";
        images_names[1] = "hammer.jpg";
        images_names[2] = "nail.jpg";
        images_names[3] = "saw.jpg";



    }
}
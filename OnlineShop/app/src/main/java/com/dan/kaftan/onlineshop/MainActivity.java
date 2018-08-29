package com.dan.kaftan.onlineshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btn;

    int itemsNum = 2;
    int i = 1;

    String items [] = new String[i];
    String FILE_NAME [] = new String[i];
    ImageView itemsImage [] = new ImageView[itemsNum];

    Uri file;

    ImageView image;

    private StorageReference mStorageRef;

//    Intent intent = new Intent(MainActivity.this, MainGallery.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        btn = (Button)findViewById(R.id.btn);


        image = (ImageView)findViewById(R.id.imageView);


     //   for (i = 1; i <= itemsNum; i++) {

//            setLocalFiles();
  //          setLocalFiles();
 //           uploadFile();
//            itemsImage[i] = new ImageView();
 //           download2();
         //   upload();


  //      }
        try {
            download3();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void upload(){


        Uri file = Uri.fromFile(new File("/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
    public void download2(){

        File localFile = null;
        try {
            localFile = File.createTempFile("image", "jpg");

        } catch (IOException e){
            System.out.println(e);
        }

        StorageReference riversRef = mStorageRef.child("nail.jpg");

        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });

    }

    public void download (){

        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("example/3.png");

        // ImageView in your Activity

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(this /* context */)
                .load(storageReference)
                .into(image);

    }


    private void setFilesNames(){

        FILE_NAME[itemsNum] = "item_image_" + i;


    }


    private void onCLick (View v){

//        startActivity(intent);
//        intent.putExtra("itemsNum", itemsNum);


    }


    private void download3 () throws IOException {
        mStorageRef = FirebaseStorage.getInstance().getReference();

        final StorageReference riversRef = mStorageRef.child("nail.jpg");

        riversRef.getFile(new File("image1"))
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }



}

package com.dan.kaftan.onlineshop2;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
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


    Button buttonDownload;
    ImageView imageView;
    EditText editTextName;
    Bitmap bitmap;

    ProgressDialog pd;
    Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);


        buttonDownload = (Button)findViewById(R.id.btndownload);
        editTextName = (EditText)findViewById(R.id.editText);
        onClick();

    }

    public void onClick (){

    buttonDownload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            downloadImage();
        }
    });





    }


   private void downloadImage(){

       FirebaseStorage storage = FirebaseStorage.getInstance();
       StorageReference storageRef = storage.getReferenceFromUrl("gs://online-shop-a32c0.appspot.com");


       storageRef.child("images/nail.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
           @Override
           public void onSuccess(byte[] bytes) {

               String path =getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/"+editTextName.getText().toString();

               try {
                   FileOutputStream fos = new FileOutputStream(path);
                   fos.write(bytes);
                   fos.close();


               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
          //     pd.dismiss();

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               pd.dismiss();
           }
       });
   }
}

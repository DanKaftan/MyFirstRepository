package com.dan.kaftan.onlineshop;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class MainGallery extends AppCompatActivity {


    int itemsNum = 0;
    int i = 0;

    ListView listView;
    ImageView[] images = new ImageView[itemsNum];
    String[] names = new String[itemsNum];
    String[] price = new String[itemsNum];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gallery);

        listView = (ListView)findViewById(R.id.listView);
        CoustomAdapter coustomAdapter = new CoustomAdapter();

        listView.setAdapter(coustomAdapter);




    }

    private MainGallery (){

        Intent intent = getIntent();

        itemsNum = intent.getIntExtra("itemsNum", itemsNum);
    }




    private void getimagesFromLocalStorage(){

        for (i = 0; i <itemsNum; i++ )

        images[i].setImageBitmap(BitmapFactory.decodeFile("item_image_"+ i +1));

    }

    class CoustomAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.coustom_layout,null);

            ImageView imageView = (ImageView)view.findViewById(R.id.item_iv);
            TextView textView_name = (TextView)view.findViewById(R.id.tv_name);
            TextView textView_price = (TextView)view.findViewById(R.id.tv_price);


//            imageView.setImageResource(1);
            textView_name.setText(names[i]);
            textView_name.setText(price[i]);


            return view;
        }
    }
}

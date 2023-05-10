package com.example.mykhatabook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private LinearLayout submit_btn, camera, gallery;
    private CircleImageView profile_pic;
    private TextInputEditText get_name, get_business_name;
    private String name, business_name;
    private int width,height;
    private static final String KEY_CREDENTIALS = "LOGIN_CREDENTIALS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);
        get_name = findViewById(R.id.get_name);
        submit_btn = findViewById(R.id.submit_btn);
        get_business_name = findViewById(R.id.get_business_name);
        profile_pic = findViewById(R.id.profile_pic);

//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 201);
//
//            }
//        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 101);
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e=get_name.getText().toString();
                String p=get_business_name.getText().toString();
                if (e.equals("")){
                    get_name.setError("Name Required");
                }else if (p.equals("")){
                    get_business_name.setError("Business Name Required");
                }else {
                    Pref();
                    Intent intent = new Intent(MainActivity.this, DashBord.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {

            if (requestCode == 201) {
                width = profile_pic.getWidth();
                height = profile_pic.getHeight();
                Bitmap bitmap = (Bitmap) data.getExtras().getParcelable("data");

                Glide.with(MainActivity.this)
                        .asBitmap()
                        .load(bitmap)
                        .centerCrop()
                        .into(profile_pic);
            }
            if (requestCode == 101) {
                Uri uri = data.getData();

                profile_pic.setImageURI(uri);
            }
        }
    }

    void Pref() {
        SharedPreferences sharedPreferences = getSharedPreferences("My_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        name = get_name.getText().toString();
        business_name = get_business_name.getText().toString();
        editor.putString("key_name", name);
        editor.putString("key_business", business_name);
        editor.commit();
        Intent intent=new Intent(MainActivity.this,DashBord.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("My_pref", MODE_PRIVATE);
        String user_name = sharedPreferences.getString("key_name", "");
        String user_business_name = sharedPreferences.getString("key_business", "");
        if (user_name.equals("") && user_business_name.equals("")){

        }else{
            Intent intent=new Intent(MainActivity.this,DashBord.class);
            startActivity(intent);
            finish();
        }
    }
}
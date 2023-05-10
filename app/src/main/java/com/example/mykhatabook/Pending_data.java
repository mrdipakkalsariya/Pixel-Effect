package com.example.mykhatabook;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mykhatabook.Adapter.Full_Pending_List;
import com.example.mykhatabook.Adapter.ModelData;
import com.example.mykhatabook.DataBase.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Pending_data extends AppCompatActivity implements Animation.AnimationListener {

    private RecyclerView pending_recycle,pending_list;
    List<ModelData> list=new ArrayList<>();
    private DBHelper dbHelper;
    private List<ModelData> list1;
    private TextView set_duo_date,select_txt;
    String date;
    private FloatingActionButton calender;
    private int d_day,d_month,d_year;
    private List<ModelData> filtered_list;
    List<ModelData> pending_data_list=new ArrayList<>();
    private LinearLayout anime,linear;
    private Full_Pending_List full_pending_list;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_data);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        pending_recycle=findViewById(R.id.pending_recycle);
        set_duo_date=findViewById(R.id.set_duo_date);
        calender=findViewById(R.id.calender);
        select_txt=findViewById(R.id.select_txt);
        linear=findViewById(R.id.linear);
        anime=findViewById(R.id.anim);

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePic();
            }
        });
        Pending_list();
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation1);
        animation.setAnimationListener(Pending_data.this);
        linear.startAnimation(animation);

    }

    void DatePic()
    {
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(Pending_data.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                d_day=day;
                d_month=month;
                d_year=year;
                set_duo_date.setText(d_day+"-"+(d_month+1)+"-"+d_year);
                set_duo_date.setVisibility(View.VISIBLE);
                select_txt.setVisibility(View.GONE);
                date=set_duo_date.getText().toString();
                Filter();
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    void Pending_list(){
        dbHelper=new DBHelper(Pending_data.this);
        list=dbHelper.ReadData();
        for (int i = 0; i < list.size(); i++) {
            String check=list.get(i).getPayment();
            if (check.equals("Pending")){
                pending_data_list.add(list.get(i));
            }
        }
        full_pending_list=new Full_Pending_List(Pending_data.this,pending_data_list);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(Pending_data.this);
        pending_recycle.setLayoutManager(layoutManager);
        pending_recycle.setAdapter(full_pending_list);
    }

    private void Filter() {
        filtered_list = new ArrayList<>();
        for (ModelData item : DashBord.list) {
            if (item.getDuo_date().contains(date) && item.getPayment().contains("Pending")) {
                anime.setVisibility(View.GONE);
                filtered_list.add(item);
            }
        }
        if (filtered_list.isEmpty()) {
            Toast.makeText(Pending_data.this, "No Data Found...!", Toast.LENGTH_SHORT).show();
            anime.setVisibility(View.VISIBLE);
            pending_recycle.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            },2950);
        } else {
            pending_recycle.setVisibility(View.VISIBLE);
            full_pending_list.filterList_date(filtered_list);
        }
    }
    public void restart(Context context) {
        Intent intent = new Intent(Pending_data.this, DashBord.class);
        this.startActivity(intent);
        this.finishAffinity();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        restart(Pending_data.this);
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
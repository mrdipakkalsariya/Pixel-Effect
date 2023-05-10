package com.example.mykhatabook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mykhatabook.Adapter.Customer_List_Adapter;
import com.example.mykhatabook.Adapter.ModelData;
import com.example.mykhatabook.DataBase.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashBord extends AppCompatActivity implements Animation.AnimationListener {

    private TextView b_name;
    private TextView received_rate, pending_rate;
    public static int pending1 = 0, received1 = 0;
    private String user_name, business_name;
    private RecyclerView customer_list;
    public static List<ModelData> list = new ArrayList<>();
    private FloatingActionButton add;
    private DBHelper dbHelper;
    private Customer_List_Adapter customer_list_adapter;
    private ImageView filter_list,p_data;
    private LinearLayout pending_click,edit_name,linear;
    private SearchView search_view;
    private LottieAnimationView animationView;
    private SharedPreferences sharedPreferences;
    private RadioGroup filter;
    private RadioButton Pending_list_btn, all, Received_list_btn, get_check;
    private Dialog dialog;
    private List<ModelData> select_filter_list;
    Animation animation;
    private CardView data_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_bord);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        id();
        Shard_Preference();

        dbHelper = new DBHelper(DashBord.this);
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(DashBord.this);
                dialog.setContentView(R.layout.name_update_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                LinearLayout btn_yes = dialog.findViewById(R.id.btn_yes);
                LinearLayout btn_no = dialog.findViewById(R.id.btn_no);
                LinearLayout linear = dialog.findViewById(R.id.linear);
                LinearLayout ok_btn = dialog.findViewById(R.id.ok);
                FrameLayout frame = dialog.findViewById(R.id.frame);
                TextInputEditText get_new_name = dialog.findViewById(R.id.get_new_name);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String new_name = get_new_name.getText().toString();
                        editor.putString("key_business", new_name);
                        editor.commit();
                        linear.setVisibility(View.GONE);
                        frame.setVisibility(View.VISIBLE);
                        ok_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                restart(DashBord.this);
                                dialog.dismiss();
                            }
                        });
                    }
                });
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        p_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(DashBord.this,Pending_data.class);
                startActivity(intent1);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBord.this, Customer_Data_Insert.class);
                startActivity(intent);
            }
        });

            search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    search_view.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    Filter(s);
                    return false;
                }
            });
        rv_list();
        pending_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBord.this, Pending_data.class);
                startActivity(intent);
            }
        });
        if (list.isEmpty()) {
            animationView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {}
            }, 2950);
        } else {
            animationView.setVisibility(View.GONE);
        }
        filter_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(DashBord.this);
                dialog.setContentView(R.layout.filter_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                filter = dialog.findViewById(R.id.radio_filter);
                all = dialog.findViewById(R.id.all);
                Received_list_btn = dialog.findViewById(R.id.Received_list_btn);
                Pending_list_btn = dialog.findViewById(R.id.Pending_list_btn);
                filter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (all.isChecked()) {
                            rv_list();
                            dialog.dismiss();
                        } else if (Received_list_btn.isChecked()) {
                            String s_check = "Done";
                            SelectFilter(s_check);
                            dialog.dismiss();
                        } else if (Pending_list_btn.isChecked()) {
                            String s_check = "Pending";
                            SelectFilter(s_check);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation1);
        animation.setAnimationListener(DashBord.this);
        linear.startAnimation(animation);
        data_show.startAnimation(animation);
    }

    public void rv_list() {
        list = dbHelper.ReadData();
        customer_list_adapter = new Customer_List_Adapter(DashBord.this, list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DashBord.this);
        customer_list.setLayoutManager(layoutManager);
        customer_list.setAdapter(customer_list_adapter);
        if (list.isEmpty()){
            customer_list.setVisibility(View.GONE);
            animationView.setVisibility(View.VISIBLE);
        }else{
            customer_list.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.GONE);
        }
        customer_list_adapter.notifyDataSetChanged();
    }

    void id() {
        b_name = findViewById(R.id.b_name);
        customer_list = findViewById(R.id.customer_list);
        add = findViewById(R.id.add);
        pending_rate = findViewById(R.id.pending_rate);
        received_rate = findViewById(R.id.received_rate);
        filter_list = findViewById(R.id.filter_list);
        pending_click = findViewById(R.id.pending_click);
        search_view = findViewById(R.id.search_view);
        animationView = findViewById(R.id.animationView);
        p_data = findViewById(R.id.p_data);
        edit_name = findViewById(R.id.edit_name);
        linear = findViewById(R.id.linear);
        data_show = findViewById(R.id.data_show);
    }

    void Shard_Preference() {
        sharedPreferences = getSharedPreferences("My_pref", MODE_PRIVATE);
        user_name = sharedPreferences.getString("key_name", "");
        business_name = sharedPreferences.getString("key_business", "");
        b_name.setText(business_name);
    }

    void Filter(String text) {
        List<ModelData> list1 = new ArrayList<>();
        for (ModelData data : list) {
            if (data.getName().toLowerCase().contains(text.toLowerCase(Locale.ROOT))) {
                list1.add(data);
            }
        }
        if (list1.isEmpty()) {
            Toast.makeText(DashBord.this, "No Data Found", Toast.LENGTH_SHORT).show();
            customer_list.setVisibility(View.GONE);
            animationView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 2950);
        } else {
            animationView.setVisibility(View.GONE);
            customer_list.setVisibility(View.VISIBLE);
            customer_list_adapter.filter_data(list1);
        }
    }

    void SelectFilter(String chek) {

        select_filter_list = new ArrayList<>();

        for (ModelData item : list) {
            if (item.getPayment().contains(chek)) {

                select_filter_list.add(item);
            }
        }
        if (select_filter_list.isEmpty()) {
            customer_list.setVisibility(View.GONE);
            animationView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            customer_list.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.GONE);
            customer_list_adapter.filterListextra(select_filter_list);
        }
    }

    public void restart(Context context) {
        Intent intent = new Intent(DashBord.this, Splash.class);
        this.startActivity(intent);
        this.finishAffinity();
    }


    @Override
    protected void onResume() {
        super.onResume();
        pending1 = 0;
        received1 = 0;
        rv_list();
        for (int i = 0; i < list.size(); i++) {
            String chack1 = list.get(i).getPayment();
            if (chack1.equals("Pending")) {
                Log.e("TAG", "onCreate: " + list.get(i).getRate());
                pending1 = pending1 + Integer.parseInt(list.get(i).getRate());
            }
            pending_rate.setText("₹" + pending1);
            if (chack1.equals("Done")) {
                received1 = received1 + Integer.parseInt(list.get(i).getRate());
            }
            received_rate.setText("₹" + received1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        pending1 = 0;
        received1 = 0;
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(DashBord.this);
        dialog.setContentView(R.layout.quit_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        LinearLayout btn_yes = dialog.findViewById(R.id.btn_yes);
        LinearLayout btn_no = dialog.findViewById(R.id.btn_no);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
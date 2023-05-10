package com.example.mykhatabook.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mykhatabook.Customer_Data_Insert;
import com.example.mykhatabook.DashBord;
import com.example.mykhatabook.DataBase.DBHelper;
import com.example.mykhatabook.Delete_Activity;
import com.example.mykhatabook.R;

import java.util.ArrayList;
import java.util.List;

public class Customer_List_Adapter extends RecyclerView.Adapter<Customer_List_Adapter.ViewData> {
    DashBord activity;
    List<ModelData> list=new ArrayList<>();
    DBHelper dbHelper;
    public static int receive=0,pending=0,receive1=0,pending1=0,pending2=0;
    private Animation animation;

    public Customer_List_Adapter(DashBord dashBord, List<ModelData> list) {
        activity=dashBord;
        this.list=list;
        notifyDataSetChanged();
    }
    public void filterListextra(List<ModelData> filtered_list) {

        list = filtered_list;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.dashbord_item,parent,false);
        return new ViewData(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewData holder, @SuppressLint("RecyclerView") int position) {
        dbHelper=new DBHelper(activity);
        holder.c_name.setText(list.get(position).getName());
        holder.c_mobile.setText(list.get(position).getMobile());
        holder.payment_date.setText(list.get(position).getDate());
        holder.set_rate.setText(list.get(position).getRate());
        holder.set_item_duo_date.setText(list.get(position).getDuo_date());
        animation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.animation2);
        animation.setAnimationListener((Animation.AnimationListener) activity);
        holder.item.startAnimation(animation);
        String color=list.get(position).getPayment();
        if (color.equals("Done")){
            holder.d_date_layout.setVisibility(View.GONE);
            holder.set_rate.setTextColor(activity.getColor(R.color.green));
        }
        if (color.equals("Pending")){
            holder.d_date_layout.setVisibility(View.VISIBLE);
            holder.set_rate.setTextColor(activity.getColor(R.color.red));
        }
        if (holder.set_rate.getText().toString().equals("0")||holder.set_rate.getText().toString().equals("")){
            holder.set_rate.setTextColor(activity.getColor(R.color.black));
            holder.txt_name.setTextColor(activity.getColor(R.color.red));
            holder.txt_date.setTextColor(activity.getColor(R.color.red));
            holder.d_date_layout.setVisibility(View.GONE);
        }
        holder.menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(activity,holder.menu_btn);
                popupMenu.inflate(R.menu.recycler_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.menu1:

                                Dialog dialog=new Dialog(activity);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setContentView(R.layout.delete_dialog);
                                LinearLayout btn_yes=dialog.findViewById(R.id.btn_yes);
                                LinearLayout btn_no=dialog.findViewById(R.id.btn_no);
                                btn_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dbHelper.DeleteData(list.get(position).getId());
                                        ((DashBord)activity).rv_list();
                                        activity.finish();
                                        activity.overridePendingTransition( 0, 0);
                                        activity.startActivity(activity.getIntent());
                                        activity.overridePendingTransition( 0, 0);
                                        Intent intent=new Intent(activity, Delete_Activity.class);
                                        activity.startActivity(intent);
                                    }
                                });
                                btn_no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                break;
                            case R.id.menu2:
                                try {
                                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                                    waIntent.setType("text/plain");
                                    String text = "Type Message Hear";
                                    String w_num = list.get(position).getWhatsapp();
                                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("91"+w_num) + "@s.whatsapp.net");
                                    sendIntent.putExtra(Intent.EXTRA_TEXT,text);
                                    sendIntent.setType("text/plain");
                                    activity.startActivity(Intent.createChooser(sendIntent,"Share With"));

                                } catch (Exception e) {
                                    Toast.makeText(activity, "Whatsapp is not Installed in Your Device", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.menu3:
                                String number=list.get(position).getMobile();
                                String msg="Type Message Hear";
                                try {
                                    SmsManager smsManager=SmsManager.getDefault();
                                    smsManager.sendTextMessage(number,null,msg,null,null);
                                    Toast.makeText(activity,"Message Sending",Toast.LENGTH_LONG).show();
                                }catch (Exception e)
                                {
                                    Toast.makeText(activity,"Some fields is Empty",Toast.LENGTH_LONG).show();
                                }
                                break;
                            case R.id.menu4:
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + list.get(position).getMobile()));
                                activity.startActivity(intent);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, Customer_Data_Insert.class);
                intent.putExtra("c_id",list.get(position).getId());
                intent.putExtra("c_name",holder.c_name.getText());
                intent.putExtra("c_mobile",holder.c_mobile.getText());
                intent.putExtra("c_whatsapp",list.get(position).getWhatsapp());
                intent.putExtra("c_address",list.get(position).getAddress());
                intent.putExtra("c_rate",holder.set_rate.getText());
                String payment_check=list.get(position).getPayment();
                String pending_date=list.get(position).getDuo_date();
                intent.putExtra("payment_check",payment_check);
                intent.putExtra("pending_date",pending_date);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter_data(List<ModelData> list1) {
        list=list1;
        notifyDataSetChanged();
    }

    class ViewData extends RecyclerView.ViewHolder {
        private final TextView c_name,c_mobile,payment_date,set_rate,set_item_duo_date,txt_name,txt_date;
        private final ImageView menu_btn;
        private final CardView item;
        private final LinearLayout d_date_layout;

        public ViewData(@NonNull View itemView) {
            super(itemView);
            c_name=itemView.findViewById(R.id.c_name);
            c_mobile=itemView.findViewById(R.id.c_mobile);
            payment_date=itemView.findViewById(R.id.payment_date);
            set_rate=itemView.findViewById(R.id.set_rate);
            menu_btn=itemView.findViewById(R.id.menu_btn);
            d_date_layout=itemView.findViewById(R.id.d_date_layout);
            set_item_duo_date=itemView.findViewById(R.id.set_item_duo_date);
            item=itemView.findViewById(R.id.item);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_date=itemView.findViewById(R.id.txt_date);
        }
    }
}

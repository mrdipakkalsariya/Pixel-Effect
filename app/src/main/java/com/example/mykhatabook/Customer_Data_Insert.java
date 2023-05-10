package com.example.mykhatabook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mykhatabook.DataBase.DBHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Customer_Data_Insert extends AppCompatActivity {

    private RadioGroup payment_method, payment_check;
    private LinearLayout select_pay_method, insert_data, due_date, customer_data_insert_id, update_date, update_layout, add_customer_layout, whatsapp_btn, call_btn, message_btn;
    private ImageView down_arrow, up_arrow;
    private TextInputEditText get_name, get_mobile, get_whatsapp, get_address, get_rate;
    private RadioButton get_payment_check;
    private TextView current_date, set_due_date, payment_method_print, top_txt;
    private RadioButton payment_done, payment_pending;
    private RadioButton radio_cash, radio_online, radio_check, radio_card;
    private DBHelper dbHelper;
    private String c_payment_check, c_id;
    private String duo_date, c_pending_date;
    Calendar newCalendar = Calendar.getInstance();
    int year = newCalendar.get(Calendar.YEAR);
    int month = newCalendar.get(Calendar.MONTH);
    int day = newCalendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_data_insert);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        id();


        dbHelper = new DBHelper(Customer_Data_Insert.this);

        payment_check.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (payment_done.isChecked()) {
                    Snackbar snackbar = Snackbar.make(customer_data_insert_id, "Done", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    duo_date = "null";
                    set_due_date.setText("Duo Date");
                    select_pay_method.setVisibility(View.VISIBLE);
                    due_date.setClickable(false);
                } else if (payment_pending.isChecked()) {
                    Snackbar snackbar = Snackbar.make(customer_data_insert_id, "Pending", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    select_pay_method.setVisibility(View.GONE);
                    due_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DataPic();

                        }
                    });
                }
            }
        });
        select_pay_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payment_method.getVisibility() == View.GONE) {
                    payment_method.setVisibility(View.VISIBLE);
                    down_arrow.setVisibility(View.GONE);
                    up_arrow.setVisibility(View.VISIBLE);
                    payment_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            if (radio_cash.isChecked()) {
                                Snackbar snackbar = Snackbar.make(customer_data_insert_id, "Payment Received in Cash", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                payment_method_print.setText("Cash");
                            } else if (radio_online.isChecked()) {
                                Snackbar snackbar = Snackbar.make(customer_data_insert_id, "Payment Received Online", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                payment_method_print.setText("Online");
                            } else if (radio_check.isChecked()) {
                                Snackbar snackbar = Snackbar.make(customer_data_insert_id, "Payment Received By Check", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                payment_method_print.setText("Check");
                            } else if (radio_card.isChecked()) {
                                Snackbar snackbar = Snackbar.make(customer_data_insert_id, "Payment Received By Credit/Debit Card", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                payment_method_print.setText("Card");
                            }
                            payment_method.setVisibility(View.GONE);
                        }
                    });

                } else {
                    payment_method.setVisibility(View.GONE);
                    down_arrow.setVisibility(View.VISIBLE);
                    up_arrow.setVisibility(View.GONE);
                }
            }
        });

        c_id = getIntent().getStringExtra("c_id");
        String c_name = getIntent().getStringExtra("c_name");
        String c_mobile = getIntent().getStringExtra("c_mobile");
        String c_whatsapp = getIntent().getStringExtra("c_whatsapp");
        String c_address = getIntent().getStringExtra("c_address");
        String c_rate = getIntent().getStringExtra("c_rate");
        c_payment_check = getIntent().getStringExtra("payment_check");
        c_pending_date = getIntent().getStringExtra("pending_date");
        get_name.setText(c_name);
        get_mobile.setText(c_mobile);
        get_whatsapp.setText(c_whatsapp);
        get_address.setText(c_address);
        get_rate.setText(c_rate);

        String date = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(new Date());
        current_date.setText(date);

        insert_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = payment_check.getCheckedRadioButtonId();
                get_payment_check = findViewById(value);

                String name = get_name.getText().toString();
                String mobile = get_mobile.getText().toString();
                String whatsapp = get_whatsapp.getText().toString();
                String address = get_address.getText().toString();
                String rate = get_rate.getText().toString();
                String date = current_date.getText().toString();


                duo_date = set_due_date.getText().toString();
                String payment_method = payment_method_print.getText().toString();
                if (get_name.getText().toString().trim().equalsIgnoreCase("") && get_mobile.getText().toString().trim().equalsIgnoreCase("") && get_whatsapp.getText().toString().trim().equalsIgnoreCase("") && get_rate.getText().toString().trim().equalsIgnoreCase("") && get_address.getText().toString().trim().equalsIgnoreCase("")) {
                    get_name.setError("Name Required");
                    get_mobile.setError("Mobile Required");
                    get_rate.setError("Rate Required");
                    get_whatsapp.setError("Whatsapp Number Required");
                    get_address.setError("Address Required");
                } else {
                    if (get_name.getText().toString().trim().equalsIgnoreCase("")) {
                        get_name.setError("Name Required");
                    } else if(get_mobile.getText().toString().trim().equalsIgnoreCase("")){
                        get_mobile.setError("Mobile Required");
                    } else if(get_whatsapp.getText().toString().trim().equalsIgnoreCase("")){
                        get_whatsapp.setError("Whatsapp Number Required");
                    } else if(get_address.getText().toString().trim().equalsIgnoreCase("")){
                        get_address.setError("Address Required");
                    } else if(get_rate.getText().toString().trim().equalsIgnoreCase("")){
                        get_rate.setError("Rate Required");
                    } else {
                        String payment = get_payment_check.getText().toString();
                        if (get_payment_check.getText().toString().equals("Pending") && set_due_date.getText().toString().equals("Duo Date")) {
                            DataPic();
                            Toast.makeText(Customer_Data_Insert.this, "Select Duo Date", Toast.LENGTH_SHORT).show();
                        } else {
                            dbHelper.InsertData(name, mobile, whatsapp, address, rate, date, payment, payment_method, duo_date);
                            Intent intent = new Intent(Customer_Data_Insert.this, DashBord.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });

        whatsapp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWhatsapp();
            }
        });
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + get_mobile.getText().toString()));
                startActivity(intent);
            }
        });
        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });
        update_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = payment_check.getCheckedRadioButtonId();
                get_payment_check = findViewById(value);

                String name = get_name.getText().toString();
                String mobile = get_mobile.getText().toString();
                String whatsapp = get_whatsapp.getText().toString();
                String address = get_address.getText().toString();
                String rate = get_rate.getText().toString();
                String date = current_date.getText().toString();
                String duo_date = set_due_date.getText().toString();
                String payment_method = payment_method_print.getText().toString();
                isValidMobile(mobile);
                if (get_name.getText().toString().trim().equalsIgnoreCase("")) {
                    get_name.setError("Name Required");
                } else if(get_mobile.getText().toString().trim().equalsIgnoreCase("")){
                    get_mobile.setError("Mobile Required");
                } else if(get_rate.getText().toString().trim().equalsIgnoreCase("")){
                    get_rate.setError("Rate Required");
                } else if(get_whatsapp.getText().toString().trim().equalsIgnoreCase("")){
                    get_whatsapp.setError("Whatsapp Number Required");
                } else if(get_address.getText().toString().trim().equalsIgnoreCase("")){
                    get_address.setError("Address Required");
                } else {
                    String payment = get_payment_check.getText().toString();
                    if (get_payment_check.getText().toString().equals("Pending") && set_due_date.getText().toString().equals("Duo Date")) {
                        DataPic();
                        Toast.makeText(Customer_Data_Insert.this, "Select Duo Date", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.UpdateData(c_id, name, mobile, whatsapp, address, rate, date, payment, payment_method, duo_date);
                        Intent intent = new Intent(Customer_Data_Insert.this, DashBord.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }


    void DataPic() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(Customer_Data_Insert.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                set_due_date.setText(day + "-" + (month + 1) + "-" + year);
                payment_done.setClickable(false);
            }
        }, year, month, day + 1);
        datePickerDialog.show();
    }

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    void id() {
        customer_data_insert_id = findViewById(R.id.customer_data_insert_id);
        payment_done = findViewById(R.id.payment_done);
        payment_pending = findViewById(R.id.payment_pending);
        get_name = findViewById(R.id.get_name);
        get_mobile = findViewById(R.id.get_mobile);
        get_whatsapp = findViewById(R.id.get_whatsapp);
        get_address = findViewById(R.id.get_address);
        get_rate = findViewById(R.id.get_rate);
        current_date = findViewById(R.id.current_date);
        due_date = findViewById(R.id.due_date);
        set_due_date = findViewById(R.id.set_due_date);
        payment_check = findViewById(R.id.payment_check);
        payment_method = findViewById(R.id.payment_method);
        select_pay_method = findViewById(R.id.select_pay_method);
        down_arrow = findViewById(R.id.down_arrow);
        up_arrow = findViewById(R.id.up_arrow);
        insert_data = findViewById(R.id.insert_data);
        radio_cash = findViewById(R.id.radio_cash);
        radio_online = findViewById(R.id.radio_online);
        radio_check = findViewById(R.id.radio_check);
        radio_card = findViewById(R.id.radio_card);
        payment_method_print = findViewById(R.id.payment_method_print);
        update_date = findViewById(R.id.update_date);
        update_layout = findViewById(R.id.update_layout);
        add_customer_layout = findViewById(R.id.add_customer_layout);
        whatsapp_btn = findViewById(R.id.whatsapp_btn);
        call_btn = findViewById(R.id.call_btn);
        message_btn = findViewById(R.id.message_btn);
        top_txt = findViewById(R.id.top_txt);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (get_name.getText().toString().isEmpty()) {
            add_customer_layout.setVisibility(View.VISIBLE);
            update_layout.setVisibility(View.GONE);
        } else {
            update_layout.setVisibility(View.VISIBLE);
            add_customer_layout.setVisibility(View.GONE);
            top_txt.setText("Update Data !");
            if (c_payment_check.equals("Done")) {
                payment_done.setChecked(true);
            } else if (c_payment_check.equals("Pending")) {
                payment_pending.setChecked(true);
            }
            if (set_due_date.equals("null")) {
                set_due_date.setText("Duo Date");
            } else {
                set_due_date.setText(c_pending_date);
            }
        }
    }

    public void onWhatsapp() {
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "Type Message Hear";
            String wnum = get_whatsapp.getText().toString();
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("91" + wnum) + "@s.whatsapp.net");
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share With"));

        } catch (Exception e) {
            Toast.makeText(Customer_Data_Insert.this, "Whatsapp is not Installed in Your Device", Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendSMS() {
        String number = get_mobile.getText().toString();
        String msg = "Type Message Hear";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, msg, null, null);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Give Permission of Message" +
                    " ", Toast.LENGTH_LONG).show();
        }
    }
}
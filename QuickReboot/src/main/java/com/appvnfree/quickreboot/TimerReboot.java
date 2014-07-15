package com.appvnfree.quickreboot;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appvnfree.quickreboot.service.RebootService;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Calendar;

public class TimerReboot extends Activity {

    private ActionBar actionBar;
    private TimePicker timePicker;
    private Button btnSetReboot,btncancel;
    private CheckBox cboConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_reboot);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_color);
            tintManager.setNavigationBarTintResource(R.color.app_color);
        }
        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#607d8b")));
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        cboConfirm=(CheckBox)findViewById(R.id.checkBox);
        timePicker=(TimePicker)findViewById(R.id.timePicker);
        btnSetReboot=(Button)findViewById(R.id.btnSetReboot);

        final SharedPreferences sharedPreferences=getSharedPreferences("timer",MODE_PRIVATE);
        boolean checkConfirm=sharedPreferences.getBoolean("dialog",false);
        cboConfirm.setChecked(checkConfirm);
        cboConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences1=getSharedPreferences("timer",MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedPreferences1.edit();
                edit.putBoolean("dialog",isChecked);
                edit.commit();

            }
        });


        btncancel=(Button)findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarmNoti();
                Toast.makeText(TimerReboot.this,"Cancel auto reboot",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        btnSetReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("TIME:",timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute());
                SharedPreferences sharedPreferences=getSharedPreferences("timer",MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedPreferences.edit();
                edit.putInt("hour",timePicker.getCurrentHour());
                edit.putInt("minute",timePicker.getCurrentMinute());
                edit.commit();
                pushLocal(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                Toast.makeText(TimerReboot.this,"Set time reboot at: "+timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute(),Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    public void cancelAlarmNoti() {

        Intent intent = new Intent(TimerReboot.this, RebootService.class);
        PendingIntent sender = PendingIntent.getService(TimerReboot.this,
                0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

    }

    public void pushLocal(int hour,int minute) {
        Intent intent = new Intent(TimerReboot.this, RebootService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(
                TimerReboot.this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

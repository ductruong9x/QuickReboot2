package com.appvnfree.quickreboot.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.appvnfree.quickreboot.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class RebootService extends Service {
    public RebootService() {

    }
    static Process root;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences=getSharedPreferences("timer",MODE_PRIVATE);
        boolean checkdialog=sharedPreferences.getBoolean("dialog",false);
        if (checkTimeLimit()) {
            Log.e("SERVICE","FAlSE");
        } else {
            //dung gio reboot
            if (checkdialog) {
                //show dialog confirm
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setIcon(R.drawable.ic_launcher);
                dialog.setTitle("Quick Reboot");
                dialog.setMessage("You want to reboot now ?");
                dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(RebootService.this, "Reboot at time", Toast.LENGTH_SHORT).show();
                        Notification.Builder notification = new Notification.Builder(RebootService.this);
                        notification.setContentTitle(getString(R.string.app_name));
                        notification.setContentText("Rebooted at time");
                        notification.setSmallIcon(R.drawable.ic_launcher);
                        notification.setWhen(System.currentTimeMillis());
                        notification.setAutoCancel(true);
                        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                        Notification noti = notification.build();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(1, noti);

                        DataOutputStream os;
                        try {
                            root = Runtime.getRuntime().exec("su");
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Toast.makeText(getApplication(), "Device non root :(",
                                    Toast.LENGTH_LONG).show();
                        }
                        os = new DataOutputStream(root
                                .getOutputStream());
                        try {
                            os.writeBytes("reboot\n");
                            os.writeBytes("exit\n");
                            os.flush();
                            root.waitFor();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //Log.e("SERVICE","TRUE");
                AlertDialog alert = dialog.create();
                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alert.show();
            }else{

                //do not show dialog
                Toast.makeText(RebootService.this, "Reboot at time", Toast.LENGTH_SHORT).show();
                Notification.Builder notification = new Notification.Builder(RebootService.this);
                notification.setContentTitle(getString(R.string.app_name));
                notification.setContentText("Rebooted at time");
                notification.setSmallIcon(R.drawable.ic_launcher);
                notification.setWhen(System.currentTimeMillis());
                notification.setAutoCancel(true);
                notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                Notification noti = notification.build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1, noti);

                DataOutputStream os;
                try {
                    root = Runtime.getRuntime().exec("su");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "Device non root :(",
                            Toast.LENGTH_LONG).show();
                }
                os = new DataOutputStream(root
                        .getOutputStream());
                try {
                    os.writeBytes("reboot\n");
                    os.writeBytes("exit\n");
                    os.flush();
                    root.waitFor();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private boolean checkTimeLimit() {
        SharedPreferences sharedPreferences=getSharedPreferences("timer",MODE_PRIVATE);
        int hour=sharedPreferences.getInt("hour",0);
        int minute=sharedPreferences.getInt("minute",0);
        Log.e("SERVICE",hour+minute+"");
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) == hour
                && calendar.get(Calendar.MINUTE) == minute) {
            return false;
        }

        return true;
    }
}

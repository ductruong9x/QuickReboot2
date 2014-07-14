package com.appvnfree.quickreboot;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.appvnfree.quickreboot.adapter.RebootAdapter;
import com.appvnfree.quickreboot.model.Reboot;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MyActivity extends Activity {
    private ListView lvReboot;
    private ArrayList<Reboot> list=new ArrayList<Reboot>();
    private RebootAdapter adapter;
    static Process root;
    private ActionBar actionBar;
    private String dev_id="108403113";
    private String app_id="207588777";
    private StartAppAd startAppAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StartAppSDK.init(this,dev_id,app_id,true);
        StartAppAd.showSplash(this,savedInstanceState);
        setContentView(R.layout.activity_my);
        startAppAd=new StartAppAd(this);
        startAppAd.loadAd();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_color);
            tintManager.setNavigationBarTintResource(R.color.app_color);
        }
        actionBar=getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#607d8b")));
        actionBar.setIcon(android.R.color.transparent);
        try {
            root = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplication(), "Device non root :(",
                    Toast.LENGTH_LONG).show();
            lvReboot.setEnabled(false);
        }
        lvReboot=(ListView)findViewById(R.id.listview);
        list.add(new Reboot("Reboot",R.drawable.restart));
        list.add(new Reboot("Reboot Recovery",R.drawable.recovery));
        list.add(new Reboot("Reboot Bootloader",R.drawable.bootloader));
        list.add(new Reboot("Power off",R.drawable.turnoff));
        adapter=new RebootAdapter(this,R.layout.item_layout,list);
        lvReboot.setAdapter(adapter);
        danhGia();
        lvReboot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataOutputStream os;
                switch (position){
                    case 0:
                        //reboot

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
                        break;
                    case 1:
                        //reboot recovery

                         os = new DataOutputStream(root
                                .getOutputStream());
                        try {
                            os.writeBytes("reboot recovery\n");
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
                        break;
                    case 2:
                        //reboot bootloader
                         os = new DataOutputStream(root
                                .getOutputStream());
                        try {
                            os.writeBytes("reboot bootloader\n");
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
                        break;
                    case 3:
                        //power off
                         os = new DataOutputStream(root
                                .getOutputStream());
                        try {
                            os.writeBytes("reboot download\n");
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
                        break;

                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.more_app:

                Intent goMoreApp = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri
                                .parse("https://play.google.com/store/apps/developer?id=App+Entertainment"));
                startActivity(goMoreApp);

                break;
            case R.id.rate:

                Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri
                        .parse("market://details?id=" + getPackageName()));
                startActivity(goToMarket);
                break;
            case R.id.share:
                shareIt();

                break;
        }
        return true;
    }
    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(sharingIntent, getResources()
                .getString(R.string.share_via)));
    }

    public void danhGia() {
        SharedPreferences getPre = getSharedPreferences("setting", MODE_PRIVATE);
        int i = getPre.getInt("VOTE", 0);
        SharedPreferences pre;
        SharedPreferences.Editor edit;
        switch (i) {
            case 0:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", 1);
                edit.commit();
                break;
            case 1:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", i + 1);
                edit.commit();
                break;
            case 2:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", i + 1);
                edit.commit();
                break;
            case 3:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", i + 1);
                edit.commit();
                break;
            case 4:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", i + 1);
                edit.commit();
                break;
            case 5:
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle("Vote Application");
                dialog.setMessage("You can vote for Quick Reboot");
                dialog.setIcon(R.drawable.ic_launcher);
                dialog.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse("market://details?id="
                                        + getPackageName()));
                        startActivity(goToMarket);
                    }
                });
                dialog.setNeutralButton("Do not show",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pre = getSharedPreferences("setting",
                                MODE_PRIVATE);
                        SharedPreferences.Editor edit = pre.edit();
                        edit.putInt("VOTE", 6);
                        edit.commit();
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("Later",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.create().show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
    }
}

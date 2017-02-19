package com.example.somy.fragmenttap;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.somy.fragmenttap.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity  {

    //define pager, tab

    final int TAB_COUNT=5;
    private int page_position=0;
    OFragment one;
    TFragment two;
    ThFragment three;
    FFragment four;
    FiveFragment five;

    private LocationManager manager;
    public LocationManager getLocationManager(){
     return manager;
    }

    boolean backPress = false;
    // ArrayList<Integer> pageStack = new ArrayList<>();
    ViewPager viewPager;

    Stack <Integer> pageStack = new Stack<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null) {
            setContentView(R.layout.activity_main);

           // Debug.startMethodTracing("trace_result");

           }else{
            return;
        }

        //0. version check. if lower then mashimellow, don't check
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
            checkPermission();
        }else{
            init();
        }

       // Debug.stopMethodTracing();
    }


    private void init(){


        //tab init
        one = new OFragment();
        two = new TFragment();
        three = new ThFragment();
        four = new FFragment();
        five = FiveFragment.newInstance(3); // set column

        //tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);

        //title
        tabLayout.addTab(tabLayout.newTab().setText("Calculator"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.addTab(tabLayout.newTab().setText("Convertor"));
        tabLayout.addTab(tabLayout.newTab().setText("Location"));
        tabLayout.addTab(tabLayout.newTab().setText("Gallery"));


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //pager listener
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!backPress){

                    pageStack.push(page_position);
                    //pageStack.add(page_position);
                    // nothing at the first page
                    // get page_position = position;(below) just before one. not present one.
                } else {
                    backPress = false; // can stack again
                }

                page_position = position;
                //get where the page is for web-view
                // save the position in array stack
                // Don't stack when just going back
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //tab listener
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // check GPS on or off
        //off -> move to page
        if (! gpscheck()) {

            // manager.isProviderEnabled(LocationManager.GPS_PROVIDER
            //set pop-up
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            //title
            alertDialog.setTitle("On GPS");
            //message
            alertDialog.setMessage("GPS is OFF. Would you move to GPS setting?");
            //YES/NO Button
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog.show();
            // pop-up on screen
        }

    }



    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){

                case 0 : fragment = one ; break;
                case 1 : fragment = two ; break;
                case 2 : fragment = three ; break;
                case 3 : fragment = four ; break;
                case 4 : fragment = five ; break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

    }

    private  final int REQ_CODE = 100;

    //1. Check the permission
    @TargetApi(Build.VERSION_CODES.M) // Target

    private void checkPermission(){
        //1.1 check the Runtime permission
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                //support by OS
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                ){
            //not permitted
            String permArr[] = {Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.CAMERA

            };
            //define each situation (no permission) and listing
            // if writing permission is OK and also reading one is OK

            requestPermissions(permArr, REQ_CODE);
            //1,3ask system
        } else {
            init();
        }

    }

    //2. after checking and call back < system calls after user checked

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //                         recieve the result. start [0]. permitted
        if(requestCode ==REQ_CODE) {
            // 2.1 runtime send to array is permitted
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                //2.2run the program
                init();
            } else{

                Toast.makeText(this, "No permission, can't use this app", Toast.LENGTH_SHORT).show();
                //choice : 1. exit 2. ask again
                //checkPermission();
                finish();
                // init();
            }
        }
    }
    // version less then lolipop
    private boolean gpscheck(){
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP) {
         return  manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }else{
        String gps = android.provider.Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                if(gps.matches(",*gps.*")) {
                    return true;

                }else{
                    return false;
                }
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        switch (page_position){

            case 1 :
                if(two.goBack()){

                    // if going back is possible,
                } else{

                    goBackStack();
                }
                break;

            default:
                   goBackStack();

                break;

        }

    }

    public void goBackStack(){

        if(pageStack.size() < 1){
            super.onBackPressed();
            //=> close app
        } else {

            backPress = true;
            // viewPager.setCurrentItem(pageStack.get(pageStack.size() - 1));
            //pageStack.remove(pageStack.size() - 1);

            viewPager.setCurrentItem(pageStack.pop());

        }

    }
}

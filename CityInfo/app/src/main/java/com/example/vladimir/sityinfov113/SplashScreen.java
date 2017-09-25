package com.example.vladimir.sityinfov113;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Vladimir on 12.09.2017.
 */

public class SplashScreen extends Activity implements LocationListener {
    //При загрузке приложения, сразу определяем его местоположение, и по его координатама находим город и скачиваем город.
    LocationManager locationManager;
    String provider;
    double latitude;
    double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        //геопозиция
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        //Проверка на первый запуск
        SharedPreferences first_start_app = getPreferences(MODE_PRIVATE);
        if(first_start_app.getBoolean("isFirstRun", true)){
            Log.e("E","FIRST RUN");
        //    LocationAdress.getAddressFromLocation(latitude,longitude,getApplicationContext(),new GeocoderHandler());
        }

        else {
            Intent complete_download_start_activity_main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(complete_download_start_activity_main);
            Log.e("E","NOT FIRST RUN");
        }
        first_start_app.edit().putBoolean("isFirstRun",false).apply();
        //прочая штука

        //StartAnimations();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.e("Latitude", "Latitude = " + latitude);
        Log.e("Longitude", "Longitude = " + longitude);
        //LocationAdress.getAddressFromLocation(latitude,longitude,getApplicationContext(),new GeocoderHandler());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude", "disable");
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAdress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAdress = bundle.getString("ADRESS");
                    break;
                default:
                    locationAdress = null;
                    showSettingsAlert();
            }
            Log.e("E", "LOCATION ADRESS = " + locationAdress);
            if (locationAdress == "Томск") {
                Log.w("return false", "false return");
            }
            else {
                Log.w("Nadpis", "Kot'ya JOPA");
                Intent load_file_activity = new Intent(getApplicationContext(), Load_file_activity.class);
                startActivity(load_file_activity);
            }
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SplashScreen.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        SplashScreen.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}
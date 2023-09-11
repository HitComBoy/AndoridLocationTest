package com.example.androidlocationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AndroidLocationTest";
    private LocationManager lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取定位服务
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        startLocation(findViewById(R.id.gpsresult));
    }

    //代码5
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            //打印定位数据
            Log.d(TAG, "onLocationChanged: " + location.toString());
        }
    };

    public void startLocation(View view) {
        //动态授权
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请先授予定位权限", Toast.LENGTH_SHORT).show();
            goToLocationSetting();
        }
        //判断GPS是否打开
        boolean isOpen = checkGPSIsOpen();
        if (!isOpen) {
            Toast.makeText(this, "请先打开GPS", Toast.LENGTH_SHORT).show();
            goToGpsSetting();
        }
        //请求定位数据
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
    }

    private void goToGpsSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private void goToLocationSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package",getPackageName(),null));
        startActivity(intent);
    }

    private boolean checkGPSIsOpen() {
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
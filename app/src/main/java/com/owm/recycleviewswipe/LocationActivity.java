package com.owm.recycleviewswipe;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

/**
 * Demo描述:
 * 利用LocationManager实现定位功能
 * 1 实时更新经度,纬度
 * 2 根据经度和纬度获取地理信息(比如:国家,街道等)(略过)
 *
 *
 * 注意事项:
 * 0 在测试GPS定位时最好在较为宽广的空间,否则影响定位
 * 1 利用mLocationManager.getLastKnownLocation(GPSProvider)获取Location时常为null.
 *   因为设备定位是需要一定时间的,所以把定位逻辑放在LocationManager的requestLocationUpdates()方法
 *
 * 2 LocationManager.requestLocationUpdates
 *   (String provider, long minTime, float minDistance, LocationListener listener)
 *   第一个参数:位置信息的provider,比如GPS
 *   第二个参数:更新位置信息的时间间隔,单位毫秒
 *   第三个参数:更新位置信息的距离间隔,单位米
 *   第四个参数:位置信息变化时的回调
 *
 * 3 LocationListener中最重要的回调方法onLocationChanged()
 *   当minTime和minDistance同时满足时会调用该方法.文档说明:
 *   The minDistance parameter can also be used to control the
 *   frequency of location updates. If it is greater than 0 then the
 *   location provider will only send your application an update when
 *   the location has changed by at least minDistance meters, AND
 *   at least minTime milliseconds have passed.
 *   比如间隔时间(minTime)到了3秒并且移动的距离(minDistance)大于了5米
 *   那么就会调用该方法.
 *
 * 4 在Activity的onDestroy()时取消地理位置的更新.
 *
 *
 * 权限配置:
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 * <uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION"/>
 * <uses-permission android:name="android.permission.INTERNET"/>
 */
public class LocationActivity extends Activity {
    private Context mContext;
    private TextView mTextView;
    private LocationManager mLocationManager;
    private LocationListenerImpl mLocationListenerImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
        initLocationManager(mContext);
    }

    private void init() {
        mContext = this;
        mTextView = (TextView) findViewById(R.id.textView);
    }

    private void initLocationManager(Context context) {

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //获取可用的位置信息Provider.即passive,network,gps中的一个或几个
        List<String> providerList = mLocationManager.getProviders(true);
        for (Iterator<String> iterator = providerList.iterator(); iterator.hasNext(); ) {
            String provider = (String) iterator.next();
            System.out.println("provider=" + provider);
        }


        //在此采用GPS的方式获取位置信息
        String GPSProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(GPSProvider);
        if (location!=null) {
            double longitude=location.getLongitude();
            double altitude=location.getAltitude();
            System.out.println("longitude="+longitude+",altitude="+altitude);
        } else {
            System.out.println("location==null");
        }

        //注册位置监听
        mLocationListenerImpl=new LocationListenerImpl();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, mLocationListenerImpl);
    }


    private class LocationListenerImpl implements LocationListener {
        //当设备位置发生变化时调用该方法
        @Override
        public void onLocationChanged(Location location) {
            if (location!=null) {
                showLocation(location);
            }
        }

        //当provider的状态发生变化时调用该方法.比如GPS从可用变为不可用.
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged");

        }

        //当provider被打开的瞬间调用该方法.比如用户打开GPS
        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");

        }

        //当provider被关闭的瞬间调用该方法.比如关闭打开GPS
        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
        }

    }


    private void showLocation(Location location) {
        // 获取经度
        double longitude = location.getLongitude();
        // 获取纬度
        double altitude = location.getAltitude();
        String message="经度为:"+longitude+"\n"+"纬度为:"+altitude;
        mTextView.setText(message);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager!=null) {
            mLocationManager.removeUpdates(mLocationListenerImpl);
        }
    }




}
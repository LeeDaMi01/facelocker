package com.example.flocker;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Bluetooth {
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    //위치 권한 한 얻어오는데 사용되는 상수

    public Bluetooth(Context context) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        enableBluetooth();
    }

    private void enableBluetooth() {
        //블루투스 활성화 기능

        //위치 권한이 허용되어 있나 체크 (같은 퍼미션 사용하기에 위치 권한으로 받아옴)
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //이미 허용되어 있을 때
            if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                //bluetoothAdapter 가 null 값이라면 블루투스 기능이 지원하지 않는거
                // bluetoothAdapter.isEnabled() 는 블루투스가 활성화가 되어있는지 체크인데
                // 원래라면 비활성화라서 false 가 나와야하는데 ! 나와서 반대문 비활성화일때 true 줌
                bluetoothAdapter.enable();
                //블루투스 기능을 활성화 해라
            }
        }else {
            //위치권한이 허용되어 있지 않았을 때
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                //위치권한을 요청함 그리고 활성화 하라고 명령했던 곳으로 값을 돌려줌
            }
        }
    }

    public void disableBluetooth() {
        //블루투스 비활성화 기능

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            //bluetoothAdapter 가 null 값이라면 블루투스 기능이 지원하지 않는거
            // bluetoothAdapter.isEnabled() 는 블루투스가 활성화가 되어있는지 체크인데
            // 활성화라서 true 값을 줌
            bluetoothAdapter.disable(); //밑줄 쳐져있어도 신경쓰지 말것 정상작동함
        }
    }
}

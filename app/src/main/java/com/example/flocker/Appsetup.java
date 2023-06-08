package com.example.flocker;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Appsetup {

    private BluetoothManager lebluetoothManager;
    private BluetoothAdapter bluetoothAdapter, leBluetoothAdapter;
    private Context context;
    private Activity activity;


    //요청할 권한 배열 저장

    //공용 권한
    private String[] Public_Permissions = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.CHANGE_WIFI_STATE,
            android.Manifest.permission.CHANGE_NETWORK_STATE,
    };
    //안드로이드 10 이전 폰상태 권한
    private String[] Permissions_An10B = {
            android.Manifest.permission.READ_PRECISE_PHONE_STATE
    };
    //안드로이드 12이전 블루투스 권한
    private String[] Permissions_An12B = {
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.BLUETOOTH,
    };
    //안드로이드 12이후 블루투스 권한
    private String[] Permissions_An12A = {
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_ADVERTISE,
            android.Manifest.permission.BLUETOOTH_CONNECT
    };
    //안드로이드 13이전 미디어 권한
    private String[] Permissions_An13B = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    //안드로이드 13이후 권한 (미디어, 와이파이)
    private String[] Permissions_An13A = {
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.NEARBY_WIFI_DEVICES
    };

    private List permissionList;

    //권한 요청시 발생하는 창에 대한 결과값을 받기 위해 지정해주는 int 형
    //원하는 임의의 숫자 지정
    private final int MULTIPLE_PERMISSIONS = 1024; //요청에 대한 결과값 확인을 위해 RequestCode를 final로 정의


    public Appsetup(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public boolean PermissionCheck() {
        int result;
        permissionList = new ArrayList<>();

        //저전력 블루투스 는 확실하게 안드로이드 13 부터 지원하기에 12에는 지원하기도하고 아니기도함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            lebluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            leBluetoothAdapter = lebluetoothManager.getAdapter();
        } else {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        for (String PC : Public_Permissions) {
            //퍼미션 체크를 했을때 result의 값이 0이 아니라면( 권한이 없다면 )
            result = ContextCompat.checkSelfPermission(context, PC);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(PC);
            }
        }
        //안드로이드 버전 12 (api 31) 미만 일때
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            //블루투스가 될때 블루투스 퍼미션
            if (bluetoothAdapter != null) {
                for (String PC : Permissions_An12B) {
                    //퍼미션 체크를 했을때 result의 값이 0이 아니라면( 권한이 없다면 )
                    result = ContextCompat.checkSelfPermission(context, PC);
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(PC);
                    }
                }
            }
        }
        //안드로이드 버전 12 (api 31)이상 일때
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //블루투스가 될때 블루투스 퍼미션요청
            if ((leBluetoothAdapter != null) || (bluetoothAdapter != null)) {
                for (String PC : Permissions_An12A) {
                    //퍼미션 체크를 했을때 result의 값이 0이 아니라면( 권한이 없다면 )
                    result = ContextCompat.checkSelfPermission(context, PC);
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(PC);
                    }
                }
            }
        }
        //안드로이드 버전 13 (api 33) 미만 일때
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            for (String PC : Permissions_An13B) {
                //퍼미션 체크를 했을때 result의 값이 0이 아니라면( 권한이 없다면 )
                result = ContextCompat.checkSelfPermission(context, PC);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(PC);
                }
            }
        }
        //안드로이드 버전 13 (api 33) 이상 일때
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (String PC : Permissions_An13A) {
                result = ContextCompat.checkSelfPermission(context, PC);
                //권한이 없을때 체크해서 추가함
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(PC);
                }
            }
        }
        //안드로이드 버전 10 미만 일때
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            for (String PC : Permissions_An10B) {
                result = ContextCompat.checkSelfPermission(context, PC);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(PC);
                }
            }
        }

        //권한 요청이 하나라도 없을 때
        //권한 없는게 1개라도 있다면
        if (!permissionList.isEmpty()) {
            return false;
        }
        //권한이 모두 있을 때
        return true;
    }

    /*
    //나중에 테스트 할때 다시 활성화 하도록 하고 일단 기능에선 필요없음으로 주석처리
    public void printPermissionList() {
        //체크를 다시 한번 시도해서 없는 권한들을 저장함
        PermissionCheck();
        //위에서 권한 없다고 체크된 리스트들을 모두 리스트뷰로 표기하도록 리스트뷰넘겨줌
        Intent intent = new Intent(context, PermissionList.class);
        intent.putExtra("permissionList", new ArrayList<>(permissionList));
        context.startActivity(intent);
    }
     */

    public void BluetoothEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            //bluetoothadapter 가 null 이면 블루투스 기능이 지원하지않는거
            if (leBluetoothAdapter != null && !leBluetoothAdapter.isEnabled()) {
                //퍼미션 체크했을때 권한이없다면 기능 활성화 못함
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivity(enableBtIntent);
            }
        } else if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothAdapter.enable();
        }
    }

    //블루투스 비활성화
    public void BluetothDisable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            //안드로이드 버전 32 부터 되도록 안되면 쩔수없고 일단 임시
            //bluetoothadapter 가 null 이면 블루투스 기능이 지원하지않는거
            if (leBluetoothAdapter != null && leBluetoothAdapter.isEnabled()) {
                //퍼미션 체크했을때 퍼미션권한이없다면 기능 비활성화 못함
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                leBluetoothAdapter.disable();
            }
        } else if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothAdapter.disable();
        }
    }

    public String MACAddress(){
        String Address = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            if (leBluetoothAdapter != null && leBluetoothAdapter.isEnabled()) {
                Address = leBluetoothAdapter.getAddress();
            }
        } else if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Address = bluetoothAdapter.getAddress();
        }

        if ( (Address != null) && ( Address.equals("02:00:00:00:00:00") ) ) {
            //wifi 가 켜져있고 연결되어있을때 wifi mac 주소 들고옴
            //근데 다른값 가져올 수 도 있음, rnadom wifi mac 도 들고옴
            Address = getMACAddress("wlan0");

            //끝자리를 -1 로 변환하는 과정임
            char[] re = Address.substring(Address.length()-2).toCharArray();
            re[re.length-1] = (char) (re[re.length-1]-1);
            if ( re[re.length-1] == 'A'-1){
                re[re.length-2] = (char) (re[re.length-2]-1);
                re[re.length-2] = 'F';
            }
            Address.replace(Address.substring(Address.length()-2), String.valueOf(re));
        }
        return Address;
    }

    private String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++) {
                    buf.append(String.format("%02X:", mac[idx]));
                }
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }


    public void requestPermission() {
        ActivityCompat.requestPermissions(activity, (String[]) permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
    }

    //요청 권한
    public boolean permissionResult(int requestConde, @NonNull String[] permissions, @NonNull int[] grantResults){
        //우선 requestCode가 아까 위에 final로 선언하였던 숫자와 맞는지, 결과값의 길이가 0보다는 큰지 먼저 체크
        if (requestConde == MULTIPLE_PERMISSIONS && (grantResults.length >0)){
            for (int i = 0 ; i<grantResults.length; i++){
                if (grantResults[i] == -1){
                    //grantResults 가 0이면 사용자가 허용한 것 / -1이면 거부한 것
                    //-1이 있는지 체크하여 하나라도 -1이 나온다면 false를 리턴
                    return false;
                }
            }
        }
        return true;
    }


}

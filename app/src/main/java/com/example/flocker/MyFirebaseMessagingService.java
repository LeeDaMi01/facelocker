package com.example.flocker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private static final String CHANNEL_ID = "channel_id";

    // FCM 메시지를 수신했을 때 호출
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //FCM 메시지에 알림 데이터가 포함되어 있는지 확인
        if (remoteMessage.getNotification() != null) {
            //제목
            String title = remoteMessage.getNotification().getTitle();
            //본문
            String body = remoteMessage.getNotification().getBody();
            sendNotification(title, body);
        }
        if (remoteMessage.getData() != null && remoteMessage.getData().containsKey("openTime")) {
            String openTime = remoteMessage.getData().get("openTime");
            if (openTime != null) {
                sendNotification("Open Time", openTime); // 푸시 알림으로 openTime 값을 표시
            }
        }
    }
    //제목과 본문을 받아 푸시알림 생성
    private void sendNotification(String title, String messageBody) {
        //푸시 알림 클릭시 개패로그확인(log.class) 화면으로 이동
        Intent intent = new Intent(this, log.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //제목, 본문, 아이콘, 알림 사운드, 알림을 클릭시 실행 설정
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Channel Name";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

}

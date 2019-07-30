package com.example.black.go_tankuser.utils;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.black.go_tankuser.Detail_histori;
import com.example.black.go_tankuser.HistoriActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseInstanceService extends FirebaseMessagingService {
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("ada pesan",remoteMessage.getData().get("body")+"=="+remoteMessage.getData().get("pesan_id"));
        processChatRoomPush(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"),remoteMessage.getData().get("pesan_id"));
    }

    private void processChatRoomPush(String title, String body, String pesan_id) {
        Intent resultIntent = new Intent(getApplicationContext(), Detail_histori.class);
        resultIntent.putExtra("ID",Integer.valueOf(pesan_id));
        showNotificationMessage(getApplicationContext(), title,body, resultIntent);
    }

    private void showNotificationMessage(Context applicationContext, String title, String body, Intent resultIntent) {
        notificationUtils = new NotificationUtils(applicationContext);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title,body, resultIntent);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("catatan token", token);

        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.token_gcm(token);
    }
}

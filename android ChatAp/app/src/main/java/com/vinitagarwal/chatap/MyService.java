package com.vinitagarwal.chatap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyService extends FirebaseMessagingService {
    public MyService() {
        super();
    }
}

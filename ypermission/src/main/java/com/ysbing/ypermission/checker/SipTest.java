package com.ysbing.ypermission.checker;

import android.content.Context;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.support.annotation.NonNull;

import java.text.ParseException;

class SipTest {

    static boolean check(@NonNull Context context) {
        if (!SipManager.isApiSupported(context)) {
            return true;
        }
        SipManager manager = SipManager.newInstance(context);
        if (manager == null) {
            return true;
        }
        try {
            SipProfile.Builder builder = new SipProfile.Builder("Permission", "127.0.0.1");
            builder.setPassword("password");
            SipProfile profile = builder.build();
            manager.open(profile);
            manager.close(profile.getUriString());
        } catch (ParseException | SipException e) {
            return false;
        }
        return true;
    }
}
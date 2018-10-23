package com.ysbing.ypermission.checker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

class PhoneStateReadTest {

    @SuppressLint({"MissingPermission", "HardwareIds"})
    static boolean check(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) return true;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager != null && (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE
                || !TextUtils.isEmpty(telephonyManager.getDeviceId())
                || !TextUtils.isEmpty(telephonyManager.getSubscriberId()));
    }
}
package com.ysbing.ypermission.checker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.core.app.ActivityCompat;

import com.ysbing.ypermission.PermissionManager;
import com.ysbing.ypermission.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

public final class LowMobileChecker {

    private static final HandlerThread handlerThread = new HandlerThread("LowMobileChecker");

    static {
        handlerThread.start();
    }

    public static List<PermissionManager.NoPermission>
    hasPermission(@NonNull final Activity activity, @Size(min = 1) @NonNull final String[] permissions) {
        final List<PermissionManager.NoPermission> noPermissionList = new ArrayList<>();
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                || checkBlackList()) {
            for (String permission : permissions) {
                if (!hasPermission(activity, permission)) {
                    PermissionManager.NoPermission noPermission = new PermissionManager.NoPermission();
                    noPermission.permission = permission;
                    if (!PermissionUtil.isFirstAskingPermission(activity, permission)) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                            noPermission.isAlwaysDenied = true;
                        }
                    }
                    noPermissionList.add(noPermission);
                }
            }
        }
        return noPermissionList;
    }

    public static void hasPermission(@NonNull final Activity activity,
                                     @Size(min = 1) @NonNull final String[] permissions,
                                     @NonNull final PermissionManager.PermissionsListener listener) {
        final List<PermissionManager.NoPermission> noPermissionList = new ArrayList<>();
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                || checkBlackList()) {
            Handler handler = new Handler(handlerThread.getLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (String permission : permissions) {
                        if (!hasPermission(activity, permission)) {
                            PermissionManager.NoPermission noPermission = new PermissionManager.NoPermission();
                            noPermission.permission = permission;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                noPermission.isAlwaysDenied = true;
                            } else if (!PermissionUtil.isFirstAskingPermission(activity, permission)) {
                                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                                    noPermission.isAlwaysDenied = true;
                                }
                            }
                            noPermissionList.add(noPermission);
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (noPermissionList.isEmpty()) {
                                listener.onPermissionGranted();
                            } else {
                                listener.onPermissionDenied(noPermissionList);
                            }
                        }
                    });
                }
            });
        } else {
            listener.onPermissionGranted();
        }
    }

    private static boolean checkBlackList() {
        // 所有的手机将进行二次检测
        if (Blacklist.forceCheck && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        switch (Build.BRAND.toUpperCase()) {
            case Blacklist.OPPO.BRAND:
                return Blacklist.OPPO.check();
            case Blacklist.VIVO.BRAND:
                return Blacklist.VIVO.check();
            default:
                return Blacklist.Other.check();
        }
    }

    private static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        try {
            switch (permission) {
                case Manifest.permission.READ_CALENDAR:
                    return CalendarReadTest.check(context);
                case Manifest.permission.WRITE_CALENDAR:
                    return CalendarWriteTest.check(context);
                case Manifest.permission.CAMERA:
                    return CameraTest.check(context);
                case Manifest.permission.READ_CONTACTS:
                    return ContactsReadTest.check(context);
                case Manifest.permission.WRITE_CONTACTS:
                    return ContactsWriteTest.check(context);
                case Manifest.permission.GET_ACCOUNTS:
                    return true;
                case Manifest.permission.ACCESS_COARSE_LOCATION:
                    return LocationCoarseTest.check(context);
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    return LocationFineTest.check(context);
                case Manifest.permission.RECORD_AUDIO:
                    return RecordAudioTest.check();
                case Manifest.permission.READ_PHONE_STATE:
                    return PhoneStateReadTest.check(context);
                case Manifest.permission.CALL_PHONE:
                    return true;
                case Manifest.permission.READ_CALL_LOG:
                    return CallLogReadTest.check(context);
                case Manifest.permission.WRITE_CALL_LOG:
                    return CallLogWriteTest.check(context);
                case Manifest.permission.ADD_VOICEMAIL:
                    return AddVoiceMailTest.check(context);
                case Manifest.permission.USE_SIP:
                    return SipTest.check(context);
                case Manifest.permission.PROCESS_OUTGOING_CALLS:
                    return true;
                case Manifest.permission.BODY_SENSORS:
                    return SensorsTest.check(context);
                case Manifest.permission.SEND_SMS:
                case Manifest.permission.RECEIVE_MMS:
                    return true;
                case Manifest.permission.READ_SMS:
                    return SmsReadTest.check(context);
                case Manifest.permission.RECEIVE_WAP_PUSH:
                case Manifest.permission.RECEIVE_SMS:
                    return true;
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    return StorageReadTest.check();
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    return StorageWriteTest.check();
                default:
                    break;
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }
}
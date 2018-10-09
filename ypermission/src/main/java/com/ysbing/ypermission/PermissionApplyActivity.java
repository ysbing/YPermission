package com.ysbing.ypermission;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import static com.ysbing.ypermission.PermissionApplyDialogFragment.PERMISSION_KEY;

/**
 * 权限申请Activity
 * 仅用于非Activity的Context的进行权限申请
 *
 * @author ysbing
 */
public class PermissionApplyActivity extends Activity {

    private static final String ACTION_PERMISSION = "ACTION_PERMISSION";
    private static final String KEY_PERMISSION_GRANTED = "KEY_PERMISSION_GRANTED";
    private static final String KEY_PERMISSION_DENIED = "KEY_PERMISSION_DENIED";
    private static final String KEY_PERMISSION_DENIED_LIST = "KEY_PERMISSION_DENIED_LIST";

    static void startAction(@NonNull Context context, @NonNull String[] permissions, @NonNull final PermissionManager.PermissionsListener listener) {
        Intent intent = new Intent(context, PermissionApplyActivity.class);
        intent.putExtra(PERMISSION_KEY, permissions);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        IntentFilter intentFilter = new IntentFilter(ACTION_PERMISSION);
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ACTION_PERMISSION.equals(intent.getAction())) {
                    LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
                    if (intent.hasExtra(KEY_PERMISSION_GRANTED)) {
                        listener.onPermissionGranted();
                    } else if (intent.hasExtra(KEY_PERMISSION_DENIED)) {
                        List<PermissionManager.NoPermission> noPermissionsList = intent.getParcelableArrayListExtra(KEY_PERMISSION_DENIED_LIST);
                        listener.onPermissionDenied(noPermissionsList);
                    }
                }
            }
        }, intentFilter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = getIntent().getStringArrayExtra(PERMISSION_KEY);
        PermissionManager.requestPermission(this, permissions, new PermissionManager.PermissionsListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(ACTION_PERMISSION);
                intent.putExtra(KEY_PERMISSION_GRANTED, true);
                LocalBroadcastManager.getInstance(PermissionApplyActivity.this).sendBroadcast(intent);
                finish();
            }

            @Override
            public void onPermissionDenied(@NonNull List<PermissionManager.NoPermission> noPermissionsList) {
                super.onPermissionDenied(noPermissionsList);
                Intent intent = new Intent(ACTION_PERMISSION);
                intent.putExtra(KEY_PERMISSION_DENIED, true);
                intent.putParcelableArrayListExtra(KEY_PERMISSION_DENIED_LIST, (ArrayList<? extends Parcelable>) noPermissionsList);
                LocalBroadcastManager.getInstance(PermissionApplyActivity.this).sendBroadcast(intent);
                finish();
            }
        });
    }
}

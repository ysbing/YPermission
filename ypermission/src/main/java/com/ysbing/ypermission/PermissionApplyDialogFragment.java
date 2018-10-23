package com.ysbing.ypermission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限申请
 * 使用对话框的形式显示
 *
 * @author ysbing
 */
@TargetApi(Build.VERSION_CODES.M)
public final class PermissionApplyDialogFragment extends Fragment {
    public static final String TAG = PermissionApplyDialogFragment.class.getSimpleName();
    public static final String PERMISSION_KEY = "PERMISSION_KEY";
    public static final int REQUEST_CODE = 0x1;
    private PermissionManager.PermissionsListener mPermissionsListener;
    private String[] mPermissions;

    public static PermissionApplyDialogFragment newInstance(@NonNull String[] permissions) {
        Bundle args = new Bundle();
        args.putStringArray(PERMISSION_KEY, permissions);
        PermissionApplyDialogFragment fragment = new PermissionApplyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void requestPermissions(@NonNull PermissionManager.PermissionsListener permissionsListener) {
        this.mPermissionsListener = permissionsListener;
        if (mPermissions != null) {
            requestPermissions(mPermissions, REQUEST_CODE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPermissions = bundle.getStringArray(PERMISSION_KEY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Activity activity = getActivity();
        if (requestCode == REQUEST_CODE && mPermissionsListener != null) {
            List<PermissionManager.NoPermission> noPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.NoPermission noPermission = new PermissionManager.NoPermission();
                    noPermission.permission = permissions[i];
                    if (activity != null && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                        noPermission.isAlwaysDenied = true;
                    }
                    noPermissionList.add(noPermission);
                }
            }
            if (grantResults.length == 0) {
                for (String permission : mPermissions) {
                    PermissionManager.NoPermission noPermission = new PermissionManager.NoPermission();
                    noPermission.permission = permission;
                    noPermission.isAlwaysDenied = true;
                    noPermissionList.add(noPermission);
                }
                mPermissionsListener.onPermissionDenied(noPermissionList);
            } else {
                if (noPermissionList.isEmpty()) {
                    mPermissionsListener.onPermissionGranted();
                } else {
                    mPermissionsListener.onPermissionDenied(noPermissionList);
                }
            }
        }
        for (String permission : permissions) {
            if (activity != null) {
                PermissionUtil.firstAskingPermission(activity, permission);
            }
        }
    }
}

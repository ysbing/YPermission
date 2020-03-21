package com.ysbing.ypermission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import static com.ysbing.ypermission.PermissionApplyDialogFragment.PERMISSION_KEY;
import static com.ysbing.ypermission.PermissionApplyDialogFragment.REQUEST_CODE;

/**
 * 权限申请
 * 使用对话框的形式显示
 *
 * @author ysbing
 */
public final class SupportPermissionApplyDialogFragment extends Fragment {
    public static final String TAG = SupportPermissionApplyDialogFragment.class.getSimpleName();
    private PermissionManager.PermissionsListener mPermissionsListener;
    private String[] mPermissions;

    public static SupportPermissionApplyDialogFragment newInstance(@NonNull String[] permissions) {
        Bundle args = new Bundle();
        args.putStringArray(PERMISSION_KEY, permissions);
        SupportPermissionApplyDialogFragment fragment = new SupportPermissionApplyDialogFragment();
        fragment.setArguments(args);
        return fragment;
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

    public void requestPermissions(PermissionManager.PermissionsListener permissionsListener) {
        this.mPermissionsListener = permissionsListener;
        if (mPermissions != null) {
            requestPermissions(mPermissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Activity activity = getActivity();
        if (requestCode == REQUEST_CODE && mPermissionsListener != null) {
            List<PermissionManager.NoPermission> noPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.NoPermission noPermission = new PermissionManager.NoPermission();
                    noPermission.permission = permissions[i];
                    if (activity != null && !ActivityCompat
                            .shouldShowRequestPermissionRationale(activity, permissions[i])) {
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

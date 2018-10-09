package com.ysbing.ypermission;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

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
public final class PermissionApplyDialogFragment_v4 extends Fragment {
    public static final String TAG = PermissionApplyDialogFragment_v4.class.getSimpleName();
    private PermissionManager.PermissionsListener mPermissionsListener;
    private String[] mPermissions;

    public static PermissionApplyDialogFragment_v4 newInstance(@NonNull String[] permissions) {
        Bundle args = new Bundle();
        args.putStringArray(PERMISSION_KEY, permissions);
        PermissionApplyDialogFragment_v4 fragment = new PermissionApplyDialogFragment_v4();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        mPermissions = bundle.getStringArray(PERMISSION_KEY);
    }

    public void requestPermissions(PermissionManager.PermissionsListener permissionsListener) {
        this.mPermissionsListener = permissionsListener;
        if (mPermissions != null) {
            requestPermissions(mPermissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && mPermissionsListener != null) {
            List<PermissionManager.NoPermission> noPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.NoPermission noPermission = new PermissionManager.NoPermission();
                    noPermission.permission = permissions[i];
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
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
            PermissionUtil.firstAskingPermission(getActivity(), permission);
        }
    }
}

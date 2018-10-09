package com.ysbing.ypermission;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * 动态权限管理
 *
 * @author ysbing
 */
public class PermissionUtil {
    private static final String PREFS_FILE_NAME = "RUNTIME_PERMISSIONS_MANAGER";

    /**
     * 首次访问该权限，需要将它做一个标识，用于判断是否勾选了“不再询问”
     *
     * @param context    上下文对象性
     * @param permission 权限
     */
    public static void firstAskingPermission(@NonNull Context context, @NonNull String permission) {
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, false).apply();
    }

    /**
     * 判断首次访问该权限，如果是是首次的话，“不再询问”判断是不准确的
     *
     * @param context    上下文对象性
     * @param permission 权限
     * @return 是否首次访问
     */
    public static boolean isFirstAskingPermission(@NonNull Context context, @NonNull String permission) {
        return context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).getBoolean(permission, true);
    }

    /**
     * 保存权限到SharedPreferences，用于记录一些权限是否被拒绝过，下次就不用在申请，有助于用户体验的提高
     *
     * @param context    上下文对象性
     * @param permission 权限
     */
    public static void savePermission(@NonNull Context context, @NonNull String permission) {
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        sharedPreference.edit().putString(context.getClass().getName() + permission, permission).apply();
    }

    /**
     * 从SharedPreferences取出保存的权限，来判断是否被拒绝过之类的需求
     *
     * @param context    上下文对象性
     * @param permission 权限
     * @return 保存的权限对象
     */
    public static String getPermission(@NonNull Context context, @NonNull String permission) {
        return context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).getString(context.getClass().getName() + permission, "");
    }

    /**
     * 申请前检测强制权限是否被禁用
     *
     * @param activity         上下文对象
     * @param forcePermissions 需要申请的强制权限数组
     * @return 检查后的强制权限数组
     */
    public static List<PermissionManager.NoPermission> checkForcePermissions(@NonNull Activity activity, @NonNull String[] forcePermissions) {
        List<PermissionManager.NoPermission> noForcePermissionList = new ArrayList<>();
        for (String permission : forcePermissions) {
            if (!checkSelfPermission(activity, permission)) {
                if (!PermissionUtil.isFirstAskingPermission(activity, permission)) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                        PermissionManager.NoPermission noPermission = new PermissionManager.NoPermission();
                        noPermission.isAlwaysDenied = true;
                        noPermission.permission = permission;
                        noForcePermissionList.add(noPermission);
                    }
                }
            }
        }
        return noForcePermissionList;
    }


    /**
     * 检查是否有权限，有则可以不用申请
     *
     * @param activity    上下文对象
     * @param permissions 需要申请的权限数组
     * @return 检查后的权限列表
     */
    public static List<PermissionManager.NoPermission> check(@NonNull Activity activity, @NonNull String[] permissions) {
        List<PermissionManager.NoPermission> noPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkSelfPermission(activity, permission)) {
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
        return noPermissionList;
    }

    /**
     * 判断该权限是否可以访问
     *
     * @param context    上下文对象性
     * @param permission 权限
     * @return 该权限是否有效
     */
    public static boolean checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        return PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}

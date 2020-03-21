package com.ysbing.ypermission;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.CallSuper;
import androidx.fragment.app.FragmentActivity;

import androidx.annotation.NonNull;

import com.ysbing.ypermission.checker.LowMobileChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态权限管理
 *
 * @author ysbing
 */
public class PermissionManager {

    /**
     * 请求权限，使用原生Fragment
     *
     * @param context     上下文对象
     * @param permissions 需要申请的权限数组
     * @param listener    授权或拒绝后的回调
     */
    public static void requestPermission(@NonNull Context context,
                                         @NonNull String[] permissions,
                                         @NonNull PermissionsListener listener) {
        if (context instanceof FragmentActivity) {
            requestPermission((FragmentActivity) context, permissions, permissions, listener);
        } else if (context instanceof Activity) {
            requestPermission((Activity) context, permissions, permissions, listener);
        } else {
            PermissionApplyActivity.startAction(context, permissions, listener);
        }
    }

    /**
     * 请求权限，使用原生Fragment
     *
     * @param fragment    原生Fragment
     * @param permissions 需要申请的权限数组
     * @param listener    授权或拒绝后的回调
     */
    public static void requestPermission(@NonNull Fragment fragment,
                                         @NonNull String[] permissions,
                                         @NonNull PermissionsListener listener) {
        requestPermission(fragment, permissions, permissions, listener);
    }

    /**
     * 请求权限，使用原生Fragment
     *
     * @param fragment         原生Fragment
     * @param permissions      需要申请的权限数组
     * @param forcePermissions 需要申请的强制权限数组
     * @param listener         授权或拒绝后的回调
     */
    public static void requestPermission(@NonNull Fragment fragment,
                                         @NonNull String[] permissions,
                                         @NonNull String[] forcePermissions,
                                         @NonNull PermissionsListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            requestPermission(fragment.getActivity(), fragment.getChildFragmentManager(), permissions, forcePermissions,
                    listener);
        } else {
            requestPermission(fragment.getActivity(), fragment.getFragmentManager(), permissions, forcePermissions,
                    listener);
        }
    }

    /**
     * 请求权限，使用v4supper包的Fragment
     *
     * @param fragment    v4包的Fragment
     * @param permissions 需要申请的权限数组
     * @param listener    授权或拒绝后的回调
     */
    public static void requestPermission(@NonNull androidx.fragment.app.Fragment fragment,
                                         @NonNull String[] permissions,
                                         @NonNull PermissionsListener listener) {
        requestPermission(fragment, permissions, permissions, listener);
    }

    /**
     * 请求权限，使用v4supper包的Fragment
     *
     * @param fragment         v4包的Fragment
     * @param permissions      需要申请的权限数组
     * @param forcePermissions 需要申请的强制权限数组
     * @param listener         授权或拒绝后的回调
     */
    public static void requestPermission(@NonNull androidx.fragment.app.Fragment fragment,
                                         @NonNull String[] permissions,
                                         @NonNull String[] forcePermissions,
                                         @NonNull PermissionsListener listener) {
        Activity activity = fragment.getActivity();
        if (activity != null) {
            requestPermission(activity, fragment.getChildFragmentManager(), permissions, forcePermissions, listener);
        }
    }

    /**
     * 请求权限，使用Activity
     *
     * @param activity    Activity
     * @param permissions 需要申请的权限数组
     * @param listener    授权或拒绝后的回调
     */
    public static void requestPermission(@NonNull Activity activity,
                                         @NonNull String[] permissions,
                                         @NonNull PermissionsListener listener) {
        requestPermission(activity, permissions, permissions, listener);
    }

    /**
     * 请求权限，使用Activity
     *
     * @param activity         Activity
     * @param permissions      需要申请的权限数组
     * @param forcePermissions 需要申请的强制权限数组
     * @param listener         授权或拒绝后的回调
     */
    public static void requestPermission(@NonNull Activity activity,
                                         @NonNull String[] permissions,
                                         @NonNull String[] forcePermissions,
                                         @NonNull PermissionsListener listener) {
        requestPermission(activity, activity.getFragmentManager(), permissions, forcePermissions, listener);
    }

    /**
     * 请求权限，使用FragmentActivity
     *
     * @param activity    FragmentActivity
     * @param permissions 需要申请的权限数组
     * @param listener    授权或拒绝后的回调
     */
    public static void requestPermission(@NonNull FragmentActivity activity,
                                         @NonNull String[] permissions,
                                         @NonNull PermissionsListener listener) {
        requestPermission(activity, permissions, permissions, listener);
    }

    /**
     * 请求权限，使用FragmentActivity
     *
     * @param activity         FragmentActivity
     * @param permissions      需要申请的权限数组
     * @param forcePermissions 需要申请的强制权限数组
     * @param listener         授权或拒绝后的回调
     */
    public static void requestPermission(@NonNull FragmentActivity activity,
                                         @NonNull String[] permissions,
                                         @NonNull String[] forcePermissions,
                                         @NonNull PermissionsListener listener) {
        requestPermission(activity, activity.getSupportFragmentManager(), permissions, forcePermissions, listener);
    }

    /**
     * 私有方法，真正入口
     *
     * @param activity         上下文对象
     * @param fragmentManager  FragmentManager
     * @param permissions      需要申请的权限数组
     * @param forcePermissions 需要申请的强制权限数组
     * @param listener         授权或拒绝后的回调
     */
    private static void requestPermission(@NonNull Activity activity,
                                          @NonNull Object fragmentManager,
                                          @NonNull String[] permissions,
                                          @NonNull String[] forcePermissions,
                                          @NonNull PermissionsListener listener) {
        List<NoPermission> noForcePermissionList = PermissionUtil.checkForcePermissions(activity, forcePermissions);
        if (!noForcePermissionList.isEmpty()) {
            listener.onPermissionDenied(noForcePermissionList);
            return;
        }
        List<NoPermission> noPermissionList = PermissionUtil.systemCheck(activity, permissions);
        //如果检查到没有权限列表为空，第一层处理完毕，再处理第二层
        if (noPermissionList.isEmpty()) {
            // 第二层处理，根据各权限单独检测
            LowMobileChecker.hasPermission(activity, permissions, listener);
        } else {
            String[] noPermissions = new String[noPermissionList.size()];
            boolean isApply = false;
            for (int i = 0; i < noPermissions.length; i++) {
                NoPermission permission = noPermissionList.get(i);
                if (!permission.isAlwaysDenied) {
                    isApply = true;
                }
                noPermissions[i] = permission.permission;
            }
            // 创建Fragment进行申请权限
            if (isApply) {
                if (fragmentManager instanceof FragmentManager) {
                    showPermissionsDialog(noPermissions, (FragmentManager) fragmentManager, listener);
                } else if (fragmentManager instanceof androidx.fragment.app.FragmentManager) {
                    showPermissionsDialog_v4(noPermissions, (androidx.fragment.app.FragmentManager) fragmentManager,
                            listener);
                }
            } else {
                listener.onPermissionDenied(noPermissionList);
            }
        }
    }

    /**
     * 使用原生DialogFragment进行权限申请
     *
     * @param permissions     待申请的权限列表
     * @param fragmentManager 原生的FragmentManager
     * @param listener        授权或拒绝后的回调
     */
    private static void showPermissionsDialog(@NonNull final String[] permissions,
                                              @NonNull FragmentManager fragmentManager,
                                              @NonNull PermissionsListener listener) {
        PermissionApplyDialogFragment dialogFragment =
                (PermissionApplyDialogFragment) fragmentManager.findFragmentByTag(PermissionApplyDialogFragment.TAG);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            List<PermissionManager.NoPermission> noPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                PermissionManager.NoPermission noPermission = new PermissionManager.NoPermission();
                noPermission.permission = permission;
                noPermissionList.add(noPermission);
            }
            listener.onPermissionDenied(noPermissionList);
        } else {
            if (dialogFragment == null) {
                dialogFragment = PermissionApplyDialogFragment.newInstance(permissions);
                fragmentManager
                        .beginTransaction()
                        .add(dialogFragment, PermissionApplyDialogFragment.TAG)
                        .commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
            dialogFragment.requestPermissions(listener);
        }
    }

    /**
     * 使用v4包的DialogFragment进行权限申请
     *
     * @param permissions     待申请的权限列表
     * @param fragmentManager 原生的FragmentManager
     * @param listener        授权或拒绝后的回调
     */
    private static void showPermissionsDialog_v4(@NonNull final String[] permissions,
                                                 @NonNull androidx.fragment.app.FragmentManager fragmentManager,
                                                 @NonNull PermissionsListener listener) {
        SupportPermissionApplyDialogFragment dialogFragment = (SupportPermissionApplyDialogFragment) fragmentManager
                .findFragmentByTag(SupportPermissionApplyDialogFragment.TAG);
        if (dialogFragment == null) {
            dialogFragment = SupportPermissionApplyDialogFragment.newInstance(permissions);
            fragmentManager
                    .beginTransaction()
                    .add(dialogFragment, SupportPermissionApplyDialogFragment.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        dialogFragment.requestPermissions(listener);
    }

    /**
     * 权限监听器
     */
    public abstract static class PermissionsListener implements PermissionsInterface {

        /**
         * 被勾选了“不再提醒”的权限列表
         */
        @SuppressWarnings("WeakerAccess")
        protected final List<String> alwaysDeniedPermissions = new ArrayList<>();
        /**
         * 被拒绝的全部权限
         */
        @SuppressWarnings("WeakerAccess")
        protected final List<String> noPermissions = new ArrayList<>();

        @Override
        @CallSuper
        public void onPermissionDenied(@NonNull List<NoPermission> noPermissionsList) {
            for (PermissionManager.NoPermission noPermission : noPermissionsList) {
                noPermissions.add(noPermission.permission);
                if (noPermission.isAlwaysDenied) {
                    alwaysDeniedPermissions.add(noPermission.permission);
                }
            }
        }
    }

    /**
     * 权限的回调接口
     */
    interface PermissionsInterface {

        /**
         * 权限通过
         */
        void onPermissionGranted();

        /**
         * 权限拒绝，剩余没通过的权限
         */
        void onPermissionDenied(@NonNull List<NoPermission> noPermissionsList);

    }

    /**
     * 没有权限的实体类
     */
    public static class NoPermission implements Parcelable {
        /**
         * 权限名称
         */
        public String permission;
        /**
         * 是否不再提醒，这个为True时，将无法再申请权限，需要让用户去手动打开
         */
        public boolean isAlwaysDenied;

        public NoPermission() {
        }

        NoPermission(Parcel in) {
            permission = in.readString();
            isAlwaysDenied = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(permission);
            dest.writeByte((byte) (isAlwaysDenied ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<NoPermission> CREATOR = new Creator<NoPermission>() {
            @Override
            public NoPermission createFromParcel(Parcel in) {
                return new NoPermission(in);
            }

            @Override
            public NoPermission[] newArray(int size) {
                return new NoPermission[size];
            }
        };
    }
}

package com.ysbing.ypermission;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

/**
 * 各手机品牌跳转设置的方法
 *
 * @author ysbing
 */
public class MobileSettingUtil {

    private static final String MANUFACTURER_MEIZU = "MEIZU";//魅族
    private static final String MANUFACTURER_XIAOMI = "XIAOMI";//小米
    private static final String MANUFACTURER_SONY = "SONY";//索尼
    private static final String MANUFACTURER_LG = "LG";
    private static final String MANUFACTURER_LEMOBILE = "LEMOBILE";//乐视
    private static final String MANUFACTURER_QIKU = "QIKU";//奇酷
    private static final String MANUFACTURER_360 = "360";//奇酷

    public static void gotoPermissionSettings(@NonNull Fragment fragment, int requestId) {
        gotoPermissionSettings((Object) fragment, requestId);
    }

    public static void gotoPermissionSettings(@NonNull android.support.v4.app.Fragment fragment, int requestId) {
        gotoPermissionSettings((Object) fragment, requestId);
    }

    public static void gotoPermissionSettings(@NonNull Activity activity, int requestId) {
        gotoPermissionSettings((Object) activity, requestId);
    }

    private static void gotoPermissionSettings(@NonNull Object object, int requestId) {
        try {
            switch (Build.MANUFACTURER.toUpperCase()) {
                case MANUFACTURER_XIAOMI:
                    // 是否是小米，http://dev.xiaomi.com/doc/p=254/index.html
                    gotoMIUIPermissionSettings(object, requestId);
                    break;
                case MANUFACTURER_MEIZU:
                    // 是否是魅族
                    gotoMeizuPermissionSettings(object, requestId);
                    break;
                case MANUFACTURER_QIKU:
                case MANUFACTURER_360:
                    // 是否是360奇酷
                    gotoQikuPermissionSettings(object, requestId);
                    break;
                case MANUFACTURER_LG:
                    // 是否是LG
                    gotoLGPermissionSettings(object, requestId);
                    break;
                case MANUFACTURER_LEMOBILE:
                    // 是否是乐视
                    gotoLeMobilePermissionSettings(object, requestId);
                    break;
                case MANUFACTURER_SONY:
                    // 是否是索尼
                    gotoSonyPermissionSettings(object, requestId);
                    break;
                default:
                    gotoCommonPermissionSettings(object, requestId);
                    break;
            }
        } catch (Exception e) {
            try {
                gotoCommonPermissionSettings(object, requestId);
            } catch (Exception ignored) {
            }
        }
    }

    private static void gotoCommonPermissionSettings(@NonNull Object object, int requestId) {
        Intent localIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        localIntent.setData(Uri.fromParts("package", getPackageName(object), null));
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(localIntent, requestId);
        } else if (object instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) object).startActivityForResult(localIntent, requestId);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(localIntent, requestId);
        }
    }

    private static void gotoMIUIPermissionSettings(@NonNull Object object, int requestId) {
        try {
            // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", getPackageName(object));
            if (object instanceof Activity) {
                ((Activity) object).startActivityForResult(localIntent, requestId);
            } else if (object instanceof android.support.v4.app.Fragment) {
                ((android.support.v4.app.Fragment) object).startActivityForResult(localIntent, requestId);
            } else if (object instanceof Fragment) {
                ((Fragment) object).startActivityForResult(localIntent, requestId);
            }
        } catch (Exception e) {
            // MIUI 5/6/7
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", getPackageName(object));
            if (object instanceof Activity) {
                ((Activity) object).startActivityForResult(localIntent, requestId);
            } else if (object instanceof android.support.v4.app.Fragment) {
                ((android.support.v4.app.Fragment) object).startActivityForResult(localIntent, requestId);
            } else if (object instanceof Fragment) {
                ((Fragment) object).startActivityForResult(localIntent, requestId);
            }
        }
    }

    private static void gotoMeizuPermissionSettings(@NonNull Object object, int requestId) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", getPackageName(object));
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestId);
        } else if (object instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) object).startActivityForResult(intent, requestId);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestId);
        }
    }

    private static void gotoQikuPermissionSettings(@NonNull Object object, int requestId) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", getPackageName(object));
        ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        intent.setComponent(comp);
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestId);
        } else if (object instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) object).startActivityForResult(intent, requestId);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestId);
        }
    }

    private static void gotoLGPermissionSettings(@NonNull Object object, int requestId) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", getPackageName(object));
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestId);
        } else if (object instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) object).startActivityForResult(intent, requestId);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestId);
        }
    }

    private static void gotoLeMobilePermissionSettings(@NonNull Object object, int requestId) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", getPackageName(object));
        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
        intent.setComponent(comp);
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestId);
        } else if (object instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) object).startActivityForResult(intent, requestId);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestId);
        }
    }

    private static void gotoSonyPermissionSettings(@NonNull Object object, int requestId) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", getPackageName(object));
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestId);
        } else if (object instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) object).startActivityForResult(intent, requestId);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestId);
        }
    }

    private static String getPackageName(@NonNull Object object) {
        if (object instanceof Activity) {
            return ((Activity) object).getPackageName();
        } else if (object instanceof android.support.v4.app.Fragment) {
            Activity activity = ((android.support.v4.app.Fragment) object).getActivity();
            if (activity != null) {
                return activity.getPackageName();
            } else {
                return null;
            }
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity().getPackageName();
        } else {
            return null;
        }
    }
}
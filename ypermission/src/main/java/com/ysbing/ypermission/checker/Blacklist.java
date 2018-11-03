package com.ysbing.ypermission.checker;

import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 国产手机的一些黑名单，持续更新
 */
public class Blacklist {
    /**
     * 用于临时设置黑名单，外部调用
     */
    public static List<String[]> mobiles = new ArrayList<>();
    public static boolean forceCheck = false;

    static class Other {
        static boolean check() {
            if (mobiles == null) {
                mobiles = new ArrayList<>();
            } else {
                for (String[] mobile : mobiles) {
                    if (mobile.length != 2) {
                        continue;
                    }
                    if (TextUtils.equals(mobile[0], Build.BRAND.toUpperCase())
                            && TextUtils.equals(mobile[1], Build.MODEL.toUpperCase())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    static class OPPO {
        static final String M1 = "OPPO R9S";

        static boolean check() {
            switch (Build.MODEL.toUpperCase()) {
                case M1:
                    return true;
                default:
                    return false;
            }
        }
    }

}

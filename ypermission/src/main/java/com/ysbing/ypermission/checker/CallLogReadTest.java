package com.ysbing.ypermission.checker;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import androidx.annotation.NonNull;

class CallLogReadTest {

    static boolean check(@NonNull Context context) {
        String[] projection = new String[]{CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.TYPE};
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                PermissionTest.CursorTest.read(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }
}
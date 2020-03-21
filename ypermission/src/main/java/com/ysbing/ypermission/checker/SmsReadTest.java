package com.ysbing.ypermission.checker;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;

import androidx.annotation.NonNull;

class SmsReadTest {

    static boolean check(@NonNull Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String[] projection = new String[]{Telephony.Sms._ID, Telephony.Sms.ADDRESS,
                    Telephony.Sms.PERSON, Telephony.Sms.BODY};
            Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,
                    projection, null, null, null);
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
        return true;
    }
}
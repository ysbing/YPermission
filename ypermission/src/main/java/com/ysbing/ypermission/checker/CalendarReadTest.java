package com.ysbing.ypermission.checker;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;

class CalendarReadTest {
    static boolean check(@NonNull Context context) {
        String[] projection = new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.NAME};
        Cursor cursor = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, projection, null, null, null);
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
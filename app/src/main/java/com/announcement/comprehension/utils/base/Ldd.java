package com.announcement.comprehension.utils.base;

import com.announcement.comprehension.ColorApp;
import com.announcement.comprehension.t.Fnc;

import java.text.SimpleDateFormat;
import java.util.Locale;


public final class Ldd {


    public static void saveStr(String a, String b) {
        ColorApp.kvCenter.encode(a, b);
    }

    public static String readStr(String a) {
        String v = ColorApp.kvCenter.decodeString(a, "");
        return v != null ? v : "";
    }

    public static void saveLong(String a, long b) {
        ColorApp.kvCenter.encode(a, b);
    }

    public static long readLong(String a) {
        return ColorApp.kvCenter.decodeLong(a, 0L);
    }

    public static String getTag(int t) {
        String p = (t == 0) ? "MMddHH" : "MMdd";
        return new SimpleDateFormat(p, Locale.getDefault())
                .format(System.currentTimeMillis());
    }

    public static void addAdmin() {
        refreshTag();
        saveLong("adminTime", readLong("adminTime") + 1);
    }

    public static int adminLimit() {
        refreshTag();
        if (readLong("adminTime") >= Fnc.INSTANCE.getAdminMaxTime()) {
            Fnc.INSTANCE.vLog("adminLimited");
            return 1;
        }
        return 0;
    }


    private static void refreshTag() {
        String dayTag = getTag(1);
        if (!readStr("dayTag").equals(dayTag)) {
            saveStr("dayTag", dayTag);
            saveLong("adminTime", 0);
        }
    }

    // 为兼容 Kotlin 默认参数
    public static String getTag() {
        return getTag(0);
    }
}

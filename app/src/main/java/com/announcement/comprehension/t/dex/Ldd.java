package com.announcement.comprehension.t.dex;

import com.tencent.mmkv.MMKV;

import java.text.SimpleDateFormat;
import java.util.Locale;

import m.ki.Dva;

public final class Ldd {

    public static MMKV tx;

    public static void saveStr(String a, String b) {
        tx.encode(a, b);
    }

    public static String readStr(String a) {
        String v = tx.decodeString(a, "");
        return v != null ? v : "";
    }

    public static void saveLong(String a, long b) {
        tx.encode(a, b);
    }

    public static long readLong(String a) {
        return tx.decodeLong(a, 0L);
    }

    public static String getTag(int t) {
        String p = (t == 0) ? "MMddHH" : "MMdd";
        return new SimpleDateFormat(p, Locale.getDefault())
                .format(System.currentTimeMillis());
    }


    public static void plusShow() {
        refreshTag();
        saveLong("hourShow", readLong("hourShow") + 1);
        saveLong("dayShow", readLong("dayShow") + 1);
    }

    public static void addAdmin() {
        refreshTag();
        saveLong("adminTime", readLong("adminTime") + 1);
    }

    public static int adminLimit() {
        refreshTag();
        if (readLong("adminTime") >= Dva.INSTANCE.getAdminMaxTime()) {
            Dva.INSTANCE.dexLog("adminLimited");
            return 1;
        }
        return 0;
    }

    public static int limit() {
        refreshTag();
        if (readLong("hourShow") >= Dva.INSTANCE.getHourMaxShow()) {
            return 1;
        } else if (readLong("dayShow") >= Dva.INSTANCE.getDayMaxShow()) {
            saveStr("dayShowLimitTag", getTag(1));
            return 2;
        }
        return 0;
    }

    private static void refreshTag() {
        String hourTag = getTag();
        if (!readStr("hourTag").equals(hourTag)) {
            saveStr("hourTag", hourTag);
            saveLong("hourShow", 0);
        }
        String dayTag = getTag(1);
        if (!readStr("dayTag").equals(dayTag)) {
            Dva.INSTANCE.setNowPopFail(0); // Kotlin 属性需通过 getter/setter 调用
            saveStr("dayTag", dayTag);
            saveLong("adminTime", 0);
            saveLong("dayShow", 0);
        }
    }

    // 为兼容 Kotlin 默认参数
    public static String getTag() {
        return getTag(0);
    }
}

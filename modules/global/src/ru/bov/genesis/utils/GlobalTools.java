package ru.bov.genesis.utils;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import ru.bov.genesis.entity.mainentity.Employee;

import java.util.Date;

/**
 * Created by Administrator on 25.05.2017.
 */
public class GlobalTools {

    public static String alpha = new String("абвгдеёжзиыйклмнопрстуфхцчшщьэюя");
    public static String[] _alpha = {"a", "b", "v", "g", "d", "e", "yo", "g", "z", "i", "y", "i",
            "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "tz", "ch", "sh", "sh", "'", "e", "yu", "ya"};


    public static String statusStr(Employee entity, Date start, Date end) {
        LocalDate localStart = LocalDate.now();
        LocalDate localEnd = LocalDate.now();
        if (start != null) localStart = LocalDate.fromDateFields(start);
        if (end != null) localEnd = LocalDate.fromDateFields(end);
        if (entity.getBuilding() != null) {
            if (isWorkMan(localStart, localEnd)) {
                return "вахта";
            } else {
                return "межвахта";
            }
        } else {
            return "свободен";
        }
    }

    public static boolean isWorkMan(LocalDate start, LocalDate end) {
        return isJobMan(start, Period.months(2), Period.months(1));

    }

    public static boolean isJobMan(LocalDate start, Period periodV, Period periodM) {
        LocalDate now = LocalDate.now();

        if (start.isAfter(now))
            return false; // если тек.дата раньше начала работы то это межвахта однозначно
        if (start.isBefore(now) && start.plus(periodV).isAfter(now))
            return true; // если тек.дата в пределах периода работы - вахта
        if (start.isBefore(now) && start.plus(periodV).isBefore(now)) {
            for (LocalDate i = start; i.isBefore(now.plusMonths(10)); i = i.plus(periodV).plus(periodM)) {
                if (i.isBefore(now) && i.plus(periodV).isAfter(now)) {
                    return true;
                } else if (i.plus(periodV).isBefore(now) && i.plus(periodV).plus(periodM).isAfter(now)) {
                    return false;
                }
            }
        }
        return false;
    }

    public static String returnStatusColor(String property, Date start, Date end) {
        LocalDate localStart = LocalDate.now();
        LocalDate localEnd = LocalDate.now();
        if (start != null) localStart = LocalDate.fromDateFields(start);
        if (end != null) localEnd = LocalDate.fromDateFields(end);
        if (property == null) {
            return "default-status";
        } else if (property.equals("fieldStatus")) {
            // Вот тут-ещ мы и ....
            if (isWorkMan(localStart, localEnd)) {
                return "green-status";
            } else {
                return "red-status";
            }
        } else return null;
    }

    public static String fullFIO(String f, String i, String o) {
        f = f != null ? f : "";
        i = i != null ? i : "";
        o = o != null ? o : "";
        return f + " " + i + " " + o;
    }

    public static String shortFIO(String f, String i, String o) {
        f = f != null ? f : "";
        i = i != null ? i.toUpperCase().substring(0, 1) + "." : "";
        o = o != null ? o.toUpperCase().substring(0, 1) + "." : "";
        return f + " " + i + " " + o;
    }

    public static String newLoginEng(String fio) {
        String aStr[];
        aStr = fio.toLowerCase().split(" ");
        if (aStr.length != 3) return "error";
        fio = translitStr(aStr[0]);
        fio += translitStr(aStr[1].substring(0, 1));
        fio += translitStr(aStr[2].substring(0, 1));
        return fio;
    }

    public static String translitStr(String str) {
        StringBuffer nname = new StringBuffer("");
        char[] chs = str.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            int k = alpha.indexOf(chs[i]);
            if (k != -1)
                nname.append(_alpha[k]);
            else {
                nname.append(chs[i]);
            }
        }
        return nname.toString();
    }


}

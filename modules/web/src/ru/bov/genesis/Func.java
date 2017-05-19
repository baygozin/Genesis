package ru.bov.genesis;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.charts.gui.data.MapDataItem;
import org.joda.time.LocalDate;
import ru.bov.genesis.entity.mainentity.Employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 03.05.2017.
 */
public class Func {

    private static int minusCalc = 0; // Начало расчета сегментов в месяцах от сегодня
    private static int plusCalc = 4; // Конец расчета сегментов в месяцах от сегодня
    private static String colorWork = "#008800";
    private static String colorPause = "#880000";


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
            System.out.println("==== Свободен ====");
            return "свободен";
        }
    }

    public static boolean isWorkMan(LocalDate start, LocalDate end) {
        LocalDate current = LocalDate.now();
        if (start != null) {
            if (current.isBefore(start)) {
                return false;
            } else if (current.isAfter(start)) {
                return true;
            }
        }
        return false;
    }

    public static String returnStatusColor(boolean b, String property, Date start, Date end) {
        LocalDate localStart = LocalDate.now();
        LocalDate localEnd = LocalDate.now();
        if (start != null) localStart = LocalDate.fromDateFields(start);
        if (end != null) localEnd = LocalDate.fromDateFields(end);
        if (property == null) {
            return "default-status";
        } else if (property.equals("fieldStatus")) {
            // Вот тут-ещ мы и ....
            if (b) {
                if (Func.isWorkMan(localStart, localEnd)) {
                    return "green-status";
                } else {
                    return "red-status";
                }
            } else {
                return "yellow-status";
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

    public static List<MapDataItem> calcSegments(Date dateStart, Date dateEnd, int periodWork, int periodPause) {
        LocalDate startCalc = LocalDate.now().minusMonths(minusCalc); // начало расчета сегментов - текущая дата минус "minusCalc"
        LocalDate stopCalc = LocalDate.now().plusMonths(plusCalc);    // конец расчета сегментов - текущая дата плюс "plusCalc"
        List<MapDataItem> segments = new ArrayList<>();
        LocalDate start = null;
        LocalDate stop = null;
        if (dateStart != null) start = LocalDate.fromDateFields(dateStart);
        if (dateEnd != null) stop = LocalDate.fromDateFields(dateEnd);
        for (LocalDate dateI = startCalc; dateI.isBefore(stopCalc); ) {
            if (dateEnd != null) {
                if (LocalDate.fromDateFields(dateEnd).isAfter(start)
                        && LocalDate.fromDateFields(dateEnd).isBefore(start.plusMonths(3))) {
                    stop = LocalDate.fromDateFields(dateEnd);
                    segments.add(setItemWork(start, stop));
                    LocalDate start_old = start;
                    start = stop;
                    stop = start_old.plusMonths(periodPause + periodWork);
                    segments.add(setItemPause(start, stop));
                    start = stop;

                    dateEnd = null;
                }
            } else {
                stop = start.plusMonths(periodWork);
                segments.add(setItemWork(start, stop));
                start = stop;
                stop = start.plusMonths(periodPause);
                segments.add(setItemPause(start, stop));
                start = stop;
            }
            dateI = dateI.plusMonths(3);
        }

        if (dateEnd != null) {
            if (LocalDate.fromDateFields(dateEnd).isAfter(LocalDate.fromDateFields(dateStart))
                    && LocalDate.fromDateFields(dateEnd).isBefore(LocalDate.fromDateFields(dateStart).plusMonths(3))) {
                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        segments.get(i).remove("stop");
                        segments.get(i).add("stop", dateEnd);
                    } else if (i == 1) {
                        segments.get(i).remove("start");
                        segments.get(i).add("start", dateEnd);
                    }
                }
            }
        }
        return segments;
    }

    private static MapDataItem setItemWork(LocalDate start, LocalDate end) {
        return new MapDataItem(ParamsMap.of("start", start.toString(),
                "end", end.toString(), "task",
                "Вахта", "color", colorWork));
    }

    private static MapDataItem setItemPause(LocalDate start, LocalDate end) {
        return new MapDataItem(ParamsMap.of("start", start.toString(),
                "end", end.toString(), "task",
                "Межвахта", "color", colorPause));
    }

}

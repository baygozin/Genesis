package ru.bov.genesis.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import ru.bov.genesis.entity.mainentity.Employee;
import ru.bov.genesis.entity.mainentity.Event;
import ru.bov.genesis.entity.services.StatusEmployeeEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 25.05.2017.
 */
public class GlobalTools {

//    @Inject
//    private DataManager dataManager;

    private static int plusCalc = 6; // Конец расчета сегментов в месяцах от сегодня
    public static String colorWork = "work-status";
    public static String colorPause = "pause-status";


    public static String alpha = new String("абвгдеёжзиыйклмнопрстуфхцчшщьэюя");
    public static String[] _alpha = {"a", "b", "v", "g", "d", "e", "yo", "g", "z", "i", "y", "i",
            "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "tz", "ch", "sh", "sh", "'", "e", "yu", "ya"};

    public static Period getPeriodFromRuString(String periodRus) {
        if (periodRus == null) {
            periodRus = "1М";
        }
        String periodJoda = periodRus.toUpperCase()
                .replace('М','M')
                .replace('Н','W')
                .replace('Д','D');
        periodJoda = "P" + periodJoda;
        return Period.parse(periodJoda);
    }

    // Возвращает строку со статусом сотрудника
    public static String statusStr(Employee employee) {
        if (employee.getBuilding() == null) {
            return "Свободен";
        }
        DateTime now = DateTime.now();

        List<Event> events = employee.getEvent();
        if (events.size() == 0) {
            return "График не задан";
        }
        return getStrStatus(events);
    }

    public static String getStrStatus(List<Event> events) {
        DateTime now = DateTime.now();
        for (Event event : events) {
            DateTime start = new DateTime(event.getStartEvent());
            DateTime stop = new DateTime(event.getStopEvent());
            if (now.isAfter(start) && now.isBefore(stop)) {
                return returnStrStatus(event.getStatus());
            }
        }
        return "Статус не определен";
    }

    public static String returnStrStatus(StatusEmployeeEnum status){
        String stat = "Не определен";
        if (status == StatusEmployeeEnum.Work) stat = "Вахта";
        else if (status == StatusEmployeeEnum.Pause) stat = "Межвахта";
        else if (status == StatusEmployeeEnum.Free) stat = "Свободен";
        return stat;
    }

    public static String returnStatusColor(Employee employee) {
        if (employee.getBuilding() == null) return "default-status";
        DateTime now = DateTime.now();
        List<Event> events = employee.getEvent();
        if (events.size() == 0) return "default-status";
        for (Event event : events) {
            DateTime start = new DateTime(event.getStartEvent());
            DateTime stop = new DateTime(event.getStopEvent());
            if (now.isAfter(start) && now.isBefore(stop))
                if (event.getStatus() == StatusEmployeeEnum.Work) return "work-status";
                else if (event.getStatus() == StatusEmployeeEnum.Pause) return "pause-status";
        }
        return "default-status";
    }

    public static String returnStatusCollorTools(Employee entity, String property) {
        if (property == null) {
            return "";
        } else if (property.equals("fieldStatus")) {
            return returnStatusColor(entity);
        } else if (property.equals("expireDateAll")) {
            return getExpireDate(entity);
        } else {
            return "";
        }
    }

    // Проверка на истечение срока действия
    public static String getExpireDate(Employee employee) {
        int x = 0;
        List<Date> dateList = new ArrayList<>();
        dateList.add(employee.getVik_date_expire());
        dateList.add(employee.getNaks_date_expire());
        dateList.add(employee.getUdCkExpire());
        dateList.add(employee.getUdEbExpire());
        dateList.add(employee.getUdOtExpire());
        dateList.add(employee.getUdPbExpire());
        dateList.add(employee.getUdPtmExpire());
        dateList.add(employee.getUdDriveSafExpire());
        dateList.add(employee.getUdEcoSafExpire());
        dateList.add(employee.getUdFirstHelpExpire());
        dateList.add(employee.getUdWorkHiExpire());
        dateList.add(employee.getMedicalCheckBaseEnd());
        dateList.add(employee.getMedicalCheckPeriodicEnd());
        for (Date date: dateList) {
            if (date != null) {
                int expireDate = checkExpire(date, "P2M");
                if (x < expireDate) x = expireDate;
            }
        }
        if (x == 1) {
            return "pink-status";
        } else if (x == 2) {
            return "red-status";
        } else return "";
    }

    public static int checkExpire(Date date, String expire) {
        if (date != null) {
            Period period = Period.parse(expire);
            LocalDate dateExpire = LocalDate.fromDateFields(date);
            if (dateExpire.isAfter(LocalDate.now().plus(period))) {
                return 0;
            } else if (dateExpire.isBefore(LocalDate.now().plus(period))
                    && dateExpire.isAfter(LocalDate.now())) {
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
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

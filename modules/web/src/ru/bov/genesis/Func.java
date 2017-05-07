package ru.bov.genesis;

import java.util.Date;

/**
 * Created by Administrator on 03.05.2017.
 */
public class Func {

    public static String alpha = new String("абвгдеёжзиыйклмнопрстуфхцчшщьэюя");
    public static String[] _alpha = {"a","b","v","g","d","e","yo","g","z","i","y","i",
            "k","l","m","n","o","p","r","s","t","u","f","h","tz","ch","sh","sh","'","e","yu","ya"};

    public static boolean isWorkMan(Date start, Date end) {
        Date current = new Date();
        if (start != null && end != null) {
            if (current.after(start) && current.before(end)) {
                return true;
            } else if (current.after(end) && current.before(start)) {
                return false;
            }
        } else return false;
        return false;
    }

    public static String returnStatusColor(boolean b, String property, Date start, Date end) {
        if (property == null) {
            return "default-status";
        } else if (property.equals("fieldStatus")) {
            // Вот тут-ещ мы и ....
            if (b) {
                if (Func.isWorkMan(start, end)) {
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

    public static String newLoginEng(String fio) {
        String aStr[];
        aStr = fio.toLowerCase().split(" ");
        if (aStr.length != 3) return "error";
        fio = translitStr(aStr[0]);
        fio += translitStr(aStr[1].substring(0,1));
        fio += translitStr(aStr[2].substring(0,1));
        return fio;
    }

    public static String translitStr(String str) {
        StringBuffer nname = new StringBuffer("");
        char[] chs = str.toCharArray();
        for(int i=0; i<chs.length;i++){
            int k = alpha.indexOf(chs[i]);
            if(k != -1)
                nname.append(_alpha[k]);
            else{
                nname.append(chs[i]);
            }
        }
        return nname.toString();
    };
}

package ru.bov.genesis;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.charts.gui.data.MapDataItem;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.bov.genesis.utils.GlobalTools.isWorkMan;

/**
 * Created by Administrator on 03.05.2017.
 */
public class ToolsFunc {

    private static int plusCalc = 6; // Конец расчета сегментов в месяцах от сегодня
    private static String colorWork = "#00FF00";
    private static String colorPause = "#FF0000";

    public static List<MapDataItem> calcSegments(Date dateS, Date dateE, Period periodWork, Period periodPause) {
        List<MapDataItem> segments = new ArrayList<>(); // Сегменты графика
        LocalDate stopCalc = LocalDate.now().plusMonths(plusCalc); // дата окончания расчета сегментов
        LocalDate dateEnd;
        LocalDate dateStart;
        if (dateS == null) {
            dateStart = LocalDate.now(); // если дата заезда не задана - установим сегодняшнюю дату
        } else {
            dateStart = LocalDate.fromDateFields(dateS);
        }
        if (dateE == null || dateE.before(dateS) || dateE.equals(dateS)) {
            dateEnd = dateStart.plus(periodWork);   // если дата выезда не задана или раньше или равно заезду
        } else {
            dateEnd = LocalDate.fromDateFields(dateE);
        }
        for (LocalDate dateI = dateStart; dateI.isBefore(stopCalc);) {
            segments.add(setItemWork(dateStart, dateEnd));
            dateStart = dateStart.plus(periodWork).plus(periodPause);
            segments.add(setItemPause(dateEnd, dateStart));
            dateEnd = dateStart.plus(periodWork);
            dateI = dateStart;
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

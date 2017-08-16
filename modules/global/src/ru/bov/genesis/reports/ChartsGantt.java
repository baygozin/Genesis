package ru.bov.genesis.reports;

import com.haulmont.yarg.formatters.CustomReport;
import com.haulmont.yarg.structure.BandData;
import com.haulmont.yarg.structure.Report;

import java.util.Map;

/**
 * Created by baygozin on 18.07.17.
 */
public class ChartsGantt implements CustomReport {

    @Override
    public byte[] createReport(Report report, BandData rootBand, Map<String, Object> params) {

        return new byte[0];

    }

}

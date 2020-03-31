package az.siftoshka.habitube.utils;

import android.content.Context;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import toothpick.Toothpick;

import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;

public class DateChanger {

    @Inject
    Context context;

    public DateChanger() {
        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
    }

    public String changeDate(String date) {
        String monthInWords = null;
        try {
            String year = date.substring(0, 4);
            String month = date.substring(5, 7);
            String day = date.substring(8, 10);

            switch (month) {
                case "01": monthInWords = context.getString(R.string.m_01);break;
                case "02": monthInWords = context.getString(R.string.m_02);break;
                case "03": monthInWords = context.getString(R.string.m_03);break;
                case "04": monthInWords = context.getString(R.string.m_04);break;
                case "05": monthInWords = context.getString(R.string.m_05);break;
                case "06": monthInWords = context.getString(R.string.m_06);break;
                case "07": monthInWords = context.getString(R.string.m_07);break;
                case "08": monthInWords = context.getString(R.string.m_08);break;
                case "09": monthInWords = context.getString(R.string.m_09);break;
                case "10": monthInWords = context.getString(R.string.m_10);break;
                case "11": monthInWords = context.getString(R.string.m_11);break;
                case "12": monthInWords = context.getString(R.string.m_12);break;
            }
            return day + " " + monthInWords + " " + year;
        } catch (Exception e) {
            return date;
        }
    }
}

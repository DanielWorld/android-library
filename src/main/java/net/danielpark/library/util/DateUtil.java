package net.danielpark.library.util;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Daniel Park on 2017. 11. 15..
 */

public final class DateUtil {

    private DateUtil() {
        throw new UnsupportedOperationException();
    }

    enum Days {
        Sunday(1), Monday(2), Tuesday(3), Wednesday(4), Thursday(5), Friday(6), Saturday(7);

        public final int value;

        Days(int value) {
            this.value = value;
        }

        private int getNumber() {
            return this.value;
        }

        public static Days getDate(int value) {
            for (Days i : Days.values()) {
                if (i.getNumber() == value)
                    return i;
            }
            return Days.Sunday;
        }
    }

    public static String getDate() {
        return getDate(System.currentTimeMillis(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     * @param time long time (milliseconds)
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
     */
    public static String getDate(long time){
        return getDate(time, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     * @param time long time (milliseconds)
     * @param format String with date format
     * @return
     */
    public static String getDate(long time, String format) {
        Date date = new Date(time);
        return getDate(date, format);
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date {@link Date}
     * @param format String with date format
     * @return
     */
    public static String getDate(Date date, String format) {
        String dateStringFormat = format;
        if (StringUtil.isNullorEmpty(dateStringFormat))
            dateStringFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        DateFormat dateFormat = new SimpleDateFormat(dateStringFormat, Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date);
    }

    /**
     * Get today's day of week <br>
     *     @see Calendar#SUNDAY
     * @return  today's day of week
     */
    public static Days getDayOfWeek() {
        return getDayOfWeek(System.currentTimeMillis());
    }

    public static Days getDayOfWeek(long milliseconds) {
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTimeInMillis(milliseconds);
        return Days.getDate(calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * Get today's day of Month
     * @return      today's day of Month
     */
    public static int getDayOfMonth() {
        return getDayOfMonth(System.currentTimeMillis());
    }

    /**
     * Get specific time's day of Month
     * @param milliseconds  milliseconds
     * @return      day of Month
     */
    public static int getDayOfMonth(long milliseconds) {
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTimeInMillis(milliseconds);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}

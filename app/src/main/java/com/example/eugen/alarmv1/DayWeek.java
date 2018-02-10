package com.example.eugen.alarmv1;


import org.joda.time.DateTime;
import java.util.Calendar;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by Eugen on 05.02.2018.
 */

public class DayWeek {
    private Calendar calendar;

    public DayWeek(Calendar calendar) {
        this.calendar = calendar;
    }
    public int getNumberDay(){
        int year = calendar.get(YEAR);
        int month = calendar.get(MONTH);
        int dayOfmonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        month++;
        DateTime dateTime = new DateTime(year,month,dayOfmonth,hour,minute,second);
        int dayOfWeek = dateTime.getDayOfWeek();
        return dayOfWeek;
    }
    public int getNextAlarm(Alarm alarm) {
        String s = alarm.getDaysOfWeek();
        Calendar calendar1 = Calendar.getInstance();
        int nextday = 0;
        int day;
        day = getNumberDay() - 1;
        boolean ff = false;
            if ((calendar1.after(calendar))
                    || (Character.compare(s.charAt(day), '0') == 0)) {
                int ind;
                if(day == 6) {
                    ind = 0;
                    ff = true;
                }
                else
                    ind = day + 1;
                while (ind < s.length()) {
                    if (Character.compare(s.charAt(ind), '1') == 0) {
                        if (!ff) {
                            nextday = ind - day;
                            break;
                        } else {
                            nextday = 7 - day + ind;
                            break;
                        }
                    }
                    if (!ff && ind == 6) {
                        ind = 0;
                        ff = true;
                        continue;
                    }
                    ind++;
                }
                if (ff && nextday == 0)
                    nextday = -1;
            }

        return nextday;
    }
}

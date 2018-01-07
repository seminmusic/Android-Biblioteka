package ba.sema.listtest.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateHelper
{
    public static String nazivDana(Date datum)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datum);
        int dan = calendar.get(Calendar.DAY_OF_WEEK);
        String naziv = null;
        switch (dan)
        {
            case Calendar.MONDAY:
                naziv = "Ponedjeljak";
                break;
            case Calendar.TUESDAY:
                naziv = "Utorak";
                break;
            case Calendar.WEDNESDAY:
                naziv = "Srijeda";
                break;
            case Calendar.THURSDAY:
                naziv = "Četvrtak";
                break;
            case Calendar.FRIDAY:
                naziv = "Petak";
                break;
            case Calendar.SATURDAY:
                naziv = "Subota";
                break;
            case Calendar.SUNDAY:
                naziv = "Nedjelja";
                break;
        }
        return naziv;
    }

    public static String nazivDanaSaDatumom(Date datum, SimpleDateFormat dateFormat)
    {
        return nazivDana(datum) + " " + dateFormat.format(datum);
    }
}

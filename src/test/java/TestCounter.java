
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Assert;
import org.junit.Test;
import ru.osk.sd.Counter;
import ru.osk.sd.ForceWorkday;
import ru.osk.sd.Holiday;
import ru.osk.sd.Weekends;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author maddo
 */
public class TestCounter {
    public static final String DATE = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE_OUT = "dd-MM-yyyy HH:mm:ss z";
    
    @Test
    public void testCountWorkHurs() throws ParseException {
        String infoSM = "Оперативное РМ авуавуцфыауца ывсмцус";  
            
        if(infoSM.startsWith("Оперативное РМ"))
            System.err.println("!!!");

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE);
        SimpleDateFormat df = new SimpleDateFormat(DATE_OUT);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date regcreateSM = dateFormat.parse("25-04-2018 13:01:24");
        Date deadlineSM = dateFormat.parse("03-05-2018 12:00:00");
        String callerSM = "МСК";
        Calendar clDeadline = Calendar.getInstance();
        clDeadline.setTimeInMillis(deadlineSM.getTime());
            
        Calendar clCreate = Calendar.getInstance();
        clCreate.setTimeInMillis(regcreateSM.getTime());
        
        switch(callerSM){
            case "КЛГ":
                clCreate.setTimeZone(TimeZone.getTimeZone("Europe/Kaliningrad"));
                clDeadline.setTimeZone(TimeZone.getTimeZone("Europe/Kaliningrad"));
                break;
            case "ПРИВ":
                clCreate.setTimeZone(TimeZone.getTimeZone("Europe/Samara"));
                clDeadline.setTimeZone(TimeZone.getTimeZone("Europe/Samara"));
                break;
            case "ЮУР":case "СВРД":
                clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
                clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
                break;
            case "ЗСИБ":
                clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Omsk"));
                clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Omsk"));
                break;
            case "КРАСН":
                clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Krasnoyarsk"));
                clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Krasnoyarsk"));
                break;
            case "ВСИБ":
                clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Irkutsk"));
                clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Irkutsk"));
                break;
            case "ЗАБ":
                clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Yakutsk"));
                clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Yakutsk"));
                break;
            case "ДВС":
                clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Vladivostok"));
                clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Vladivostok"));
                break;    
            default:
                clCreate.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                clDeadline.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        }
        
        Counter counter = new Counter(8, 17, 12, 13, new Weekends[]{Weekends.SATURDAY, Weekends.SUNDAY}, 
                new Holiday[]{new Holiday(30,4,2018), new Holiday(1,5,2018), new Holiday(2,5,2018), new Holiday(9,5,2018)}, 
                new ForceWorkday[]{new ForceWorkday(28, 4, 2018), new ForceWorkday(9, 6, 2018)});
        //counter.setWeekends(new Weekends []{});
        long workHours = counter.countWorkHurs(clCreate, clDeadline);
        int ho = (int) (workHours / (60*60*1000));
        int mi = (int) ((workHours - (60*60*1000*ho)) / (60*1000));
        int se = (int) ((workHours - (60*60*1000*ho) - (60*1000*mi)) / 1000);
        System.out.println(String.format("ч: %d м: %d с: %d", ho, mi, se));
        org.junit.Assert.assertEquals(String.format("ч: %d м: %d с: %d", ho, mi, se), "ч: 31 м: 58 с: 36");
        long ndl = (long) (workHours * 0.3);
        System.out.println(df.format(new Date(counter.getNewDeadLine(clDeadline, ndl).getTimeInMillis())));
        org.junit.Assert.assertEquals(df.format(new Date(counter.getNewDeadLine(clDeadline, ndl).getTimeInMillis())), "28-04-2018 10:24:25 MSK");
    }
    
    @Test
    public void testAddWorkHours() throws ParseException {   
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE);
        SimpleDateFormat df = new SimpleDateFormat(DATE_OUT);
        
        Counter counter = new Counter(8, 17, 12, 13, new Weekends[]{Weekends.SATURDAY, Weekends.SUNDAY}, 
                new Holiday[]{new Holiday(1,5,2019), new Holiday(2,5,2019), new Holiday(3,5,2019), new Holiday(9,5,2019), new Holiday(10,5,2019)}, 
                new ForceWorkday[]{new ForceWorkday(28, 4, 2018), new ForceWorkday(9, 6, 2018)});
        
        Calendar deadline = Calendar.getInstance();
        long workHours = 0;
        
        Date beforeLunch = dateFormat.parse("28-03-2019 08:30:00");
        deadline.setTimeInMillis(beforeLunch.getTime());
        workHours = Counter.HOU * 4;
        System.out.println(longToStringDate(workHours));
        System.out.println(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())));
        Assert.assertEquals(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())), "28-03-2019 13:30:00 MSK");
        workHours = Counter.HOU * 3;
        System.out.println(longToStringDate(workHours));
        System.out.println(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())));
        Assert.assertEquals(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())), "28-03-2019 11:30:00 MSK");
        workHours = Counter.HOU * 8;
        System.out.println(longToStringDate(workHours));
        System.out.println(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())));
        Assert.assertEquals(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())), "29-03-2019 08:30:00 MSK");
        
        Date afterLunch = dateFormat.parse("28-03-2019 12:30:00");
        deadline.setTimeInMillis(afterLunch.getTime());
        workHours = Counter.HOU * 4;
        System.out.println(longToStringDate(workHours));
        System.out.println(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())));
        Assert.assertEquals(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())), "29-03-2019 08:00:00 MSK");
        
        Date beforeWeekend = dateFormat.parse("29-03-2019 12:30:00");
        workHours = Counter.HOU * 4 + Counter.MIN * 30;
        System.out.println(longToStringDate(workHours));
        System.out.println(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())));
        Assert.assertEquals(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())), "29-03-2019 08:30:00 MSK");
        
        deadline.setTimeInMillis(beforeWeekend.getTime());
        workHours = Counter.HOU * 4 + 30 * Counter.MIN;
        System.out.println(longToStringDate(workHours));
        System.out.println(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())));
        Assert.assertEquals(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())), "01-04-2019 08:30:00 MSK");
        
        Date beforeHolidays = dateFormat.parse("30-04-2019 12:30:00");
        deadline.setTimeInMillis(beforeHolidays.getTime());
        workHours = Counter.HOU * 4 + 30 * Counter.MIN;
        System.out.println(longToStringDate(workHours));
        System.out.println(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())));
        Assert.assertEquals(df.format(new Date(counter.addWokrHours(deadline, workHours).getTimeInMillis())), "06-05-2019 08:30:00 MSK");
    }
    
    /**
     * Formating long datetime value to string
     * @param time
     * @return 
     */
    private String longToStringDate(long time) {
        return longToStringDate(time, "ч: %d м: %d с: %d");
    }
    
    /**
     * Formating long datetime value to string
     * @param time
     * @param format
     * @return 
     */
    private String longToStringDate(long time, String format) {
        int ho = (int) (time / (60*60*1000));
        int mi = (int) ((time - (60*60*1000*ho)) / (60*1000));
        int se = (int) ((time - (60*60*1000*ho) - (60*1000*mi)) / 1000);
        return String.format(format, ho, mi, se);
    }
}

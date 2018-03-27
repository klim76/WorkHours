/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import ru.osk.sd.Counter;
import ru.osk.sd.Weekends;

/**
 *
 * @author dev1
 */
public class JavaApplication2 {

    public static final String DATE = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE_OUT = "dd-MM-yyyy HH:mm:ss z";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        String infoSM = "Оперативное РМ авуавуцфыауца ывсмцус";  
            
        if(infoSM.startsWith("Оперативное РМ"))
            System.err.println("!!!");

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE);
        SimpleDateFormat df = new SimpleDateFormat(DATE_OUT);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date regcreateSM = dateFormat.parse("27-03-2018 12:46:24");
        Date deadlineSM = dateFormat.parse("09-04-2018 12:00:00");
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
            
        Counter counter = new Counter(8, 17, 12, 13);
        //counter.setWeekends(new Weekends []{});
        long workHours = counter.countWorkHurs(clCreate, clDeadline);
        int ho = (int) (workHours / (60*60*1000));
        int mi = (int) ((workHours - (60*60*1000*ho)) / (60*1000));
        int se = (int) ((workHours - (60*60*1000*ho) - (60*1000*mi)) / 1000);
        System.err.println(String.format("ч: %d м: %d с: %d", ho, mi, se));
        long ndl = (long) (workHours * 0.3);
        System.err.println(df.format(new Date(counter.getNewDeadLine(clDeadline, ndl).getTimeInMillis())));
        
        /*try {
            // TODO code application logic here
            String infoSM = "Оперативное РМ авуавуцфыауца ывсмцус";  
            
            if(infoSM.startsWith("Оперативное РМ"))
                System.err.println("!!!");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE);
            SimpleDateFormat df = new SimpleDateFormat(DATE_OUT);
            //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date regcreateSM = dateFormat.parse("30-01-2018 08:47:13");
            Date deadlineSM = dateFormat.parse("30-01-2018 16:44:58");
            String callerSM = "ОРГ-ЮУР";
            
            //Calendar dlCalendar = Calendar.getInstance();
            Calendar clDeadline = Calendar.getInstance();
            clDeadline.setTimeInMillis(deadlineSM.getTime());
            if(true){
                Calendar clCreate = Calendar.getInstance();
                clCreate.setTimeInMillis(regcreateSM.getTime());
                
                switch(callerSM){
                    case "ОРГ-КЛГ":
                        clCreate.setTimeZone(TimeZone.getTimeZone("Europe/Kaliningrad"));
                        clDeadline.setTimeZone(TimeZone.getTimeZone("Europe/Kaliningrad"));
                        break;
                    case "ОРГ-ПРИВ":
                        clCreate.setTimeZone(TimeZone.getTimeZone("Europe/Samara"));
                        clDeadline.setTimeZone(TimeZone.getTimeZone("Europe/Samara"));
                        break;
                    case "ОРГ-ЮУР":case "ОРГ-СВРД":
                        clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
                        clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
                        break;
                    case "ОРГ-ЗСИБ":
                        clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Omsk"));
                        clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Omsk"));
                        break;
                    case "ОРГ-КРАСН":
                        clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Krasnoyarsk"));
                        clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Krasnoyarsk"));
                        break;
                    case "ОРГ-ВСИБ":
                        clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Irkutsk"));
                        clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Irkutsk"));
                        break;
                    case "ОРГ-ЗАБ":
                        clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Yakutsk"));
                        clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Yakutsk"));
                        break;
                    case "ОРГ-ДВС":
                        clCreate.setTimeZone(TimeZone.getTimeZone("Asia/Vladivostok"));
                        clDeadline.setTimeZone(TimeZone.getTimeZone("Asia/Vladivostok"));
                        break;    
                    default:
                        clCreate.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                        clDeadline.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                }
               
                //Date tmpregcreateSM = new Date(regcreateSM.getTime() + shift*60*60*1000);
                //Date tmpdeadlineSM = new Date(deadlineSM.getTime() + shift*60*60*1000);
                
                
                
                long workHours = getWorkHours(clCreate, clDeadline) * 20 / 100;
                System.err.println(String.format("%d",workHours));
                long timeToDayStart = 0;
                while(workHours > 0) {
                    //Date tmpDate = new Date(dlCalendar.getTimeInMillis());
                    switch(isWorkHours(clDeadline)){
                        case -1: // если до начала рабочего дня вычитаем дедлайн до 17-00 предыдущего дня
                            clDeadline.setTimeInMillis(clDeadline.getTimeInMillis() - mlsToEndDay(clDeadline, true));
                            timeToDayStart = 0;
                            break;
                        case 1:
                            clDeadline.setTimeInMillis(clDeadline.getTimeInMillis() - mlsToEndDay(clDeadline, false));
                            timeToDayStart = 0;
                            break;
                        case 0:
                            if(isWorkDay(clDeadline)){
                                if(!isBeforeLunch(clDeadline)){
                                    timeToDayStart = mlsToStartDay(clDeadline) - 3600000; //+1;
                                    if(workHours > timeToDayStart)
                                        clDeadline.setTimeInMillis(clDeadline.getTimeInMillis() - (timeToDayStart + 3600001));
                                    else
                                        if(workHours > 14400000){
                                            clDeadline.setTimeInMillis(clDeadline.getTimeInMillis() - (workHours + 3600000));
                                        }else{
                                            clDeadline.setTimeInMillis(clDeadline.getTimeInMillis() - workHours);
                                        }
                                }else{
                                    timeToDayStart = mlsToStartDay(clDeadline)+1;
                                    if(workHours > timeToDayStart)
                                        clDeadline.setTimeInMillis(clDeadline.getTimeInMillis() - timeToDayStart);
                                    else
                                        clDeadline.setTimeInMillis(clDeadline.getTimeInMillis() - workHours);
                                }
                            }else{
                                clDeadline.setTimeInMillis(clDeadline.getTimeInMillis() - mlsToEndDay(clDeadline, true));
                                timeToDayStart = 0;
                            }
                            break;
                    }
                    workHours -= timeToDayStart;
                }
                System.out.println(df.format(new Date(clDeadline.getTimeInMillis())));
                //dlCalendar.setTimeInMillis(create.getTime() + workHours);
                //если получившийся дедлайн - нерабочее время, крутим назад до ближайшего конца рабоего дня
                switch (isWorkHours(clDeadline)){
                    case -1:
                        //dlCalendar.add(Calendar.DATE, -1);
                        clDeadline.set(Calendar.HOUR_OF_DAY, 8);
                        clDeadline.set(Calendar.MINUTE, 00);
                        break;
                    case 1:
                        clDeadline.add(Calendar.DATE, 1);
                        clDeadline.set(Calendar.HOUR_OF_DAY, 8);
                        clDeadline.set(Calendar.MINUTE, 00);
                        break;
                }
                //проверяем получившуюся дату - если выходной крутим назад до пятницы
                switch(clDeadline.get(Calendar.DAY_OF_WEEK)){
                    case Calendar.SUNDAY:
                        clDeadline.add(Calendar.DATE, -2);
                        break;
                    case Calendar.SATURDAY:
                        clDeadline.add(Calendar.DATE, -1);
                        break;
                }
                
                System.out.println(df.format(new Date(clDeadline.getTimeInMillis())));
                
                clDeadline.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            } else{
                long dif = deadlineSM.getTime() - regcreateSM.getTime();
                long newWork = (long) (0.8 * dif);
                clDeadline.setTimeInMillis(regcreateSM.getTime() + newWork);
            }
        } catch (ParseException ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}

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
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.osk.sd.Counter;

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
        Date regcreateSM = dateFormat.parse("05-02-2018 11:47:13");
        Date deadlineSM = dateFormat.parse("07-02-2018 10:49:58");
        String callerSM = "ОРГ-ЮУР";
        Calendar clDeadline = Calendar.getInstance();
        clDeadline.setTimeInMillis(deadlineSM.getTime());
            
        Calendar clCreate = Calendar.getInstance();
        clCreate.setTimeInMillis(regcreateSM.getTime());
            
            
        Counter counter = new Counter(8, 17, 12, 13);
        long workHours = counter.countWorkHurs(clCreate, clDeadline);
        System.err.println(df.format(new Date(workHours)));
        long ndl = (long) (workHours * 0.2);
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
    
    private static long getWorkHours(Calendar regcreateSM, Calendar deadlineSM) {
        long workHours = 0;
        long unWork = 0;
        Calendar clCreate = (Calendar) regcreateSM.clone();
        //clCreate.setTimeInMillis(regcreateSM.getTime());
        Calendar clDaedline = (Calendar) deadlineSM.clone();
        //clDaedline.setTimeInMillis(deadlineSM.getTime());
        
        long tmp = 0;
        //Date tmpDate = new Date(clCreate.getTimeInMillis());
        
        switch(isWorkHours(clCreate)){
            case -1:
                unWork -= mlsToStartDay(clCreate); 
                clCreate.setTimeInMillis(clCreate.getTimeInMillis() + unWork);
                break;
            case 1:
                long l = mlsToDay(clCreate);
                //unWork += l - mlsToStartDay(new Date(tmpDate.getTime() + mlsToDay(tmpDate))) ;
                Calendar tm = (Calendar) clCreate.clone();
                tm.setTimeInMillis(tm.getTimeInMillis() + l);
                unWork += l - mlsToStartDay(tm) ;
                //tmpDate = new Date(tmpDate.getTime() + unWork);
                clCreate.setTimeInMillis(clCreate.getTimeInMillis() + unWork);
                break;
        
        }
        
        
        while(clDaedline.getTimeInMillis() - clCreate.getTimeInMillis() >= 86400000){
            //tmpDate = new Date(clCreate.getTimeInMillis());
            tmp = mlsToDay(clCreate);
            if(!isWorkDay(clCreate)){ //если выходной
                unWork += tmp;
            } else{
                switch(isWorkHours(clCreate)){
                    case 1: //рабочий день но после окончания рабочих часов
                        unWork += tmp;
                        break;
                    case -1: //рабочий день, но до начала рабочих часов
                        workHours += 28800000;
                        unWork += 57600000;
                        break;
                    case 0: //рабочий день, рабочие часы
                        if(isBeforeLunch(clCreate)){
                            workHours += (tmp - 28800000);
                            unWork += 28800000;
                        }else{
                            workHours += (tmp - 25200000);
                            unWork += 25200000;
                        }
                        break;
                }
            }
            clCreate.setTimeInMillis(clCreate.getTimeInMillis() + tmp);
        }
        
        //tmpDate = new Date(clCreate.getTimeInMillis());
        switch(isWorkHours(clCreate)){
            case -1:
                unWork -= mlsToStartDay(clCreate); 
                //tmpDate = new Date(tmpDate.getTime() - mlsToStartDay(tmpDate));
                clCreate.setTimeInMillis(clCreate.getTimeInMillis() - mlsToStartDay(clCreate));
                break;
            /*case 1:
                long l = mlsToDay(tmpDate);
                unWork += l - mlsToStartDay(new Date(tmpDate.getTime() + mlsToDay(tmpDate))) ;
                tmpDate = new Date(tmpDate.getTime() + unWork);
                break;
        */
        }
        
        if(mlsToStartDay(clCreate) > mlsToStartDay(deadlineSM)){
            if(isWorkDay(clCreate) && isWorkHours(clCreate) == 0){
                tmp = 0 - mlsToEndDay(clCreate, false);
                if(isBeforeLunch(clCreate)){
                    workHours += (tmp - 3600000); //- час на обед
                    unWork += 32400000; // 9 часов
                }else{
                    workHours += tmp;
                    unWork += 28800000; //8 часов
                }
            }

            if(isWorkDay(deadlineSM) && isWorkHours(deadlineSM) == 0){
                tmp = mlsToStartDay(deadlineSM);
                if(isBeforeLunch(deadlineSM)){
                    workHours += tmp;
                    unWork += 28800000; // 8 часов
                }else{
                    workHours += (tmp - 3600000);
                    unWork += 32400000; //9 часов
                }
            }
        
        }else{
            if(isWorkDay(deadlineSM) && isWorkHours(deadlineSM) == 0){
                tmp = deadlineSM.getTimeInMillis()- clCreate.getTimeInMillis();
            }else{
                if(isWorkDay(clCreate))
                    tmp = 0 - mlsToEndDay(clCreate, false);
                else
                    tmp = 0;
            }
            if (! ((isBeforeLunch(deadlineSM) && isBeforeLunch(clCreate)) || (!isBeforeLunch(deadlineSM) && !isBeforeLunch(clCreate)) )){
                tmp -= 3600000;
            }
            workHours += tmp;
        }
        
        //long newWork = (long) (0.8 * workHours);
        return workHours;
    }
    
    private static boolean isWorkDay(Calendar cl){
        return !(cl.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                cl.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }
    
    private static int isWorkHours(Calendar cl){
        if(cl.get(Calendar.HOUR_OF_DAY) > 17 || 
                (cl.get(Calendar.HOUR_OF_DAY) == 17 && cl.get(Calendar.MINUTE) > 0 )){
            return 1;
        }
        if(cl.get(Calendar.HOUR_OF_DAY) < 8){
            return -1;
        }
        
        return 0;
    }
    
    private static long mlsToDay(Calendar cl){
        //Calendar cl = Calendar.getInstance();
        //cl.setTimeInMillis(date.getTime());
        Calendar nextDay = (Calendar) cl.clone();
        nextDay.add(Calendar.DATE, 1);
        nextDay.set(Calendar.HOUR_OF_DAY, 0);
        nextDay.set(Calendar.MINUTE, 0);
        nextDay.set(Calendar.SECOND, 0);
        nextDay.set(Calendar.MILLISECOND, 0);
        
        return nextDay.getTimeInMillis() - cl.getTimeInMillis();
    }
    
    private static long mlsToStartDay(Calendar cl){
        Calendar clDayStart = (Calendar) cl.clone();
        clDayStart.set(Calendar.HOUR_OF_DAY, 8);
        clDayStart.set(Calendar.MINUTE, 0);
        clDayStart.set(Calendar.SECOND, 0);
        clDayStart.set(Calendar.MILLISECOND, 0);
        
        return cl.getTimeInMillis() - clDayStart.getTimeInMillis();
    }
    
    private static long mlsToEndDay(Calendar date, boolean isBefore){
        //Calendar cl = (Calendar) date.clone();
        //cl.setTimeInMillis(date.getTime());
        Calendar clDayEnd = (Calendar) date.clone();
        //clDayEnd.setTimeInMillis(date.getTime());
        if(isBefore)
            clDayEnd.add(Calendar.DATE, -1);
        clDayEnd.set(Calendar.HOUR_OF_DAY, 17);
        clDayEnd.set(Calendar.MINUTE, 0);
        clDayEnd.set(Calendar.SECOND, 0);
        clDayEnd.set(Calendar.MILLISECOND, 0);
        
        return date.getTimeInMillis() - clDayEnd.getTimeInMillis();
    }

    private static boolean isBeforeLunch(Calendar cl) {
        return cl.get(Calendar.HOUR_OF_DAY) < 13;
    }
    
}

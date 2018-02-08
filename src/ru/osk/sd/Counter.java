/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.osk.sd;

import java.util.Calendar;

/**
 *
 * @author dev1
 */
public class Counter {
    
    private final int dayStart;
    private final int dayFinish;
    private final int lunchStart;
    private final int lunchFinish;
    
    public Counter(int dayStart, int dayFinish, int lunchStart, int lunchFinish){
        if(dayStart < dayFinish && dayStart >= 0 && dayFinish < 24){
            this.dayStart = dayStart;        
            this.dayFinish = dayFinish;
            this.lunchStart = lunchStart;
            this.lunchFinish = lunchFinish;
        }else{
            throw new IllegalArgumentException("Day start and finish must be between 0 and 24 and start must be less then finish");
        }
    }
    
    public long countWorkHurs(Calendar start, Calendar finish){
        Calendar tmStarn = (Calendar) start.clone();
        Calendar tmFinish = (Calendar) finish.clone();
        
        if(isWorkHours(tmStarn) != 0){
            
        }
        
        
        return 0;
        
        
    }
    
    private boolean isWorkDay(Calendar cl){
        return !(cl.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                cl.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }
    
    private int isWorkHours(Calendar cl){
        if(cl.get(Calendar.HOUR_OF_DAY) > dayFinish || 
                (cl.get(Calendar.HOUR_OF_DAY) == dayFinish && cl.get(Calendar.MINUTE) > 0 )){
            return 1;
        }
        if(cl.get(Calendar.HOUR_OF_DAY) < dayStart){
            return -1;
        }
        
        return 0;
    }
    
    private long mlsToStartDay(Calendar cl, boolean isAfter){
        Calendar clDayStart = (Calendar) cl.clone();
        if(isAfter){
            clDayStart.add(Calendar.DATE, 1);
        }
        clDayStart.set(Calendar.HOUR_OF_DAY, dayStart);
        clDayStart.set(Calendar.MINUTE, 0);
        clDayStart.set(Calendar.SECOND, 0);
        clDayStart.set(Calendar.MILLISECOND, 0);
        
        if(isAfter)
            return clDayStart.getTimeInMillis() - cl.getTimeInMillis();
        else
            return cl.getTimeInMillis() - clDayStart.getTimeInMillis();
    }
    
    private long mlsToEndDay(Calendar date, boolean isBefore){
        Calendar clDayEnd = (Calendar) date.clone();
        if(isBefore)
            clDayEnd.add(Calendar.DATE, -1);
        clDayEnd.set(Calendar.HOUR_OF_DAY, dayFinish);
        clDayEnd.set(Calendar.MINUTE, 0);
        clDayEnd.set(Calendar.SECOND, 0);
        clDayEnd.set(Calendar.MILLISECOND, 0);
        
        if(isBefore)
            return date.getTimeInMillis() - clDayEnd.getTimeInMillis();
        else
            return date.getTimeInMillis() - clDayEnd.getTimeInMillis();
    }

    private boolean isBeforeLunch(Calendar cl) {
        return cl.get(Calendar.HOUR_OF_DAY) < lunchStart;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.osk.sd;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dev1
 */
public class Counter {
    
    private static final long MLS = 1;
    private static final long SEC = 1000 * MLS;
    private static final long MIN = 60 * SEC;
    private static final long HOU = 60 * MIN;
    private static final long DA  = 24 * HOU;
    
    private static final int BEFORE_WORK_HOURS = -1;
    private static final int AFTER_WORK_HOURS  =  1;
    private static final int IS_WORK_HOURS     =  0;
    
    private final int dayStart;
    private final int dayFinish;
    private final int lunchStart;
    private final int lunchFinish;
    private Set<Weekends> weekends = new HashSet<>(); 
    
    public Counter(int dayStart, int dayFinish, int lunchStart, int lunchFinish){
        if(dayStart < dayFinish && dayStart >= 0 && dayFinish < 24){
            this.dayStart = dayStart;        
            this.dayFinish = dayFinish;
            this.lunchStart = lunchStart;
            this.lunchFinish = lunchFinish;
            this.setWeekends(new Weekends[]{Weekends.SUNDAY, Weekends.SATURDAY});
        }else{
            throw new IllegalArgumentException("Day start and finish must be between 0 and 24 and start must be less then finish");
        }
    }
    
    public void setWeekends(Weekends[] ws){
        this.weekends = new HashSet<>(Arrays.asList(ws));
    }
    
    public long countWorkHurs(Calendar start, Calendar finish){
        long workHours = 0;
        long unWork = 0;
        Calendar tmStart = (Calendar) start.clone();
        Calendar tmFinish = (Calendar) finish.clone();
        
        switch(isWorkHours(tmStart)){
            case AFTER_WORK_HOURS:
                unWork += mlsToStartDay(tmStart, true);
                tmStart.setTimeInMillis(tmStart.getTimeInMillis() + mlsToStartDay(tmStart, true));
                break;
            case BEFORE_WORK_HOURS:
                unWork += mlsToStartDay(tmStart, false);
                tmStart.setTimeInMillis(tmStart.getTimeInMillis() + mlsToStartDay(tmStart, false));
                break;
        }
        
        switch(isWorkHours(tmFinish)){
            case AFTER_WORK_HOURS:
                unWork += mlsToEndDay(tmFinish, false);
                tmFinish.setTimeInMillis(tmFinish.getTimeInMillis() - mlsToEndDay(tmFinish, false));
                break;
            case BEFORE_WORK_HOURS:
                unWork += mlsToEndDay(tmFinish, true);
                tmFinish.setTimeInMillis(tmFinish.getTimeInMillis() - mlsToEndDay(tmFinish, true));
                break;
        }
        
        while(!isWorkDay(tmStart)){
            tmStart.add(Calendar.DATE, 1);
            unWork += DA;
            if(tmStart.after(tmFinish))
                return workHours;
        }
        
        while(!isWorkDay(tmFinish)){
            tmFinish.add(Calendar.DATE, -1);
            unWork += DA;
            if(tmFinish.before(tmStart))
                return workHours;
        }
        
        if(tmFinish.before(tmStart))
            return workHours;
        
        while(tmFinish.getTimeInMillis() - tmStart.getTimeInMillis() >= DA){
            if(!isWorkDay(tmStart)){
                unWork += mlsToStartDay(tmStart, true);
            } else{
                workHours += ((dayFinish - dayStart) - (lunchFinish - lunchStart)) * HOU;
                unWork += DA - (((dayFinish - dayStart) - (lunchFinish - lunchStart)) * HOU) ;
            }
            tmStart.add(Calendar.DATE, 1);
        }
        
        if(tmFinish.getTimeInMillis() - tmStart.getTimeInMillis() <=  mlsToEndDay(tmStart, false)){
            workHours += tmFinish.getTimeInMillis() - tmStart.getTimeInMillis();
            if((isBeforeLunch(tmStart) && !isBeforeLunch(tmFinish)) || (!isBeforeLunch(tmStart) && isBeforeLunch(tmFinish))){
                workHours -= (lunchFinish - lunchStart) * HOU;
                unWork += (lunchFinish - lunchStart) * HOU;
            }
        } else{
            workHours += mlsToEndDay(tmStart, false);
            unWork += mlsToStartDay(tmStart, true) - mlsToEndDay(tmStart, false);
            if(isBeforeLunch(tmStart)){
                workHours -= (lunchFinish - lunchStart) * HOU;
                unWork += (lunchFinish - lunchStart) * HOU;
            }
            tmStart.setTimeInMillis(tmStart.getTimeInMillis() + mlsToStartDay(tmStart, true));
            
            workHours += tmFinish.getTimeInMillis() - tmStart.getTimeInMillis();
            if(!(isBeforeLunch(tmStart) && isBeforeLunch(tmFinish)) || (!isBeforeLunch(tmStart) && !isBeforeLunch(tmFinish))){
                workHours -= (lunchFinish - lunchStart) * HOU;
                unWork += (lunchFinish - lunchStart) * HOU;
            }
        }
        
        return workHours;
    }
    
    public Calendar getNewDeadLine(Calendar deadLine, long workHours){
        long timeToDayStart = 0;
        Calendar newDeadLine = (Calendar) deadLine.clone();
        while(workHours > 0){
            switch(isWorkHours(newDeadLine)){
                case -1: // если до начала рабочего дня вычитаем дедлайн до 17-00 предыдущего дня
                    newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, true));
                    timeToDayStart = 0;
                    break;
                case 1:
                    newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, false));
                    timeToDayStart = 0;
                    break;
                case 0:
                    if(isWorkDay(newDeadLine)){
                        if(!isBeforeLunch(newDeadLine)){
                            timeToDayStart = mlsToStartDay(newDeadLine, false) - ((lunchFinish - lunchStart) * HOU); //+1;
                            if(workHours > timeToDayStart)
                                newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() + mlsToEndDay(newDeadLine, true));
                            else
                                if(workHours > (dayFinish - lunchFinish) * HOU){
                                    newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - (workHours + ((lunchFinish - lunchStart) * HOU)));
                                }else{
                                    newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - workHours);
                                }
                        }else{
                            timeToDayStart = mlsToStartDay(newDeadLine, false);
                            if(workHours > timeToDayStart)
                                newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, true));
                            else
                                newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - workHours);
                        }
                    }else{
                        newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, true));
                        timeToDayStart = 0;
                    }
                    break;
            }
            workHours -= timeToDayStart;
        }
        
        return newDeadLine;
    }
    
    private boolean isWorkDay(Calendar cl){
        for(Weekends w : weekends){
            if(cl.get(Calendar.DAY_OF_WEEK) == w.getValue()){
                return false;
            }
        }
        
        return true;
    }
    
    private int isWorkHours(Calendar cl){
        if(cl.get(Calendar.HOUR_OF_DAY) > dayFinish || 
                (cl.get(Calendar.HOUR_OF_DAY) == dayFinish && cl.get(Calendar.MINUTE) > 0 )){
            return AFTER_WORK_HOURS;
        }
        if(cl.get(Calendar.HOUR_OF_DAY) < dayStart){
            return BEFORE_WORK_HOURS;
        }
        
        return IS_WORK_HOURS;
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
            return abs(clDayStart.getTimeInMillis() - cl.getTimeInMillis());
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
            return abs(date.getTimeInMillis() - clDayEnd.getTimeInMillis());
    }

    private boolean isBeforeLunch(Calendar cl) {
        return cl.get(Calendar.HOUR_OF_DAY) < lunchStart;
    }
    
}

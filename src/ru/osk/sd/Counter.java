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
    
    private static final int BEFORE_WORK_HOURS  = -1;
    private static final int BEFORE_LUNCH_HOURS = -2;
    private static final int AFTER_WORK_HOURS   =  1;
    private static final int AFTER_LUNCH_HOURS  =  2;
    private static final int IS_WORK_HOURS      =  0;
    private static final int IS_LUNCH_HOURS     =  3;
    
    private final int dayStart;
    private final int dayFinish;
    private final int lunchStart;
    private final int lunchFinish;
    private Set<Weekends> weekends = new HashSet<>(); 
    private List<Holiday> holidays = new ArrayList<>();
    private List<ForceWorkday> forceWorkdays = new ArrayList<>();
           
    
    /**
     *
     * @param dayStart start of work day
     * @param dayFinish finish of work day
     * @param lunchStart start of lunch break(for the schedule without lunch break set 0)
     * @param lunchFinish finish of lunch break(for the schedule without lunch break set 0)
     */
    public Counter(int dayStart, int dayFinish, int lunchStart, int lunchFinish){
        if(dayStart < dayFinish && dayStart >= 0 && dayFinish <= 24){
            this.dayStart = dayStart;        
            this.dayFinish = dayFinish;
            this.lunchStart = lunchStart;
            this.lunchFinish = lunchFinish;
            this.setWeekends(new Weekends[]{Weekends.SUNDAY, Weekends.SATURDAY});
            this.setHolidays(new Holiday[]{new Holiday(30,4,2018), new Holiday(1,5,2018), new Holiday(2,5,2018), new Holiday(9,5,2018)});
            this.setWorkdays(new ForceWorkday[]{new ForceWorkday(28, 4, 2018), new ForceWorkday(9, 6, 2018)});
        }else{
            throw new IllegalArgumentException("Day start and finish must be between 0 and 24 and start must be less then finish");
        }
    }
    
    /**
     *
     * @param ws list of weekends to skip while calculate workhours
     */
    public void setWeekends(Weekends[] ws){
        this.weekends = new HashSet<>(Arrays.asList(ws));
    }
    
    /**
     *
     * @param hs list of holidays to skip while calculate workhours
     */
    public void setHolidays(Holiday[] hs){
        this.holidays = new ArrayList<>(Arrays.asList(hs));
    }
    
    /**
     *
     * @param fwd list of holidays/weekends wich was workdays
     */
    public void setWorkdays(ForceWorkday[] fwd){
        this.forceWorkdays = new ArrayList<>(Arrays.asList(fwd));
    }
    
    /**
     *
     * @param start 
     * @param finish
     * @return workhours in mls between start and finish
     */
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
            case IS_LUNCH_HOURS:
                unWork += skipLunch(tmStart, true);
                tmStart.setTimeInMillis(tmStart.getTimeInMillis() + skipLunch(tmStart, true));
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
            case IS_LUNCH_HOURS:
                unWork += skipLunch(tmFinish, false);
                tmFinish.setTimeInMillis(tmFinish.getTimeInMillis() - skipLunch(tmFinish, false));
                break;
        }
        
        while(!isWorkDay(tmStart)){
            unWork += mlsToStartDay(tmStart, true);
            tmStart.setTimeInMillis(tmStart.getTimeInMillis() + mlsToStartDay(tmStart, true));
            if(tmStart.after(tmFinish))
                return workHours;
        }
        
        while(!isWorkDay(tmFinish)){
            unWork += mlsToEndDay(tmFinish, true);
            tmFinish.setTimeInMillis(tmFinish.getTimeInMillis() - mlsToEndDay(tmFinish, true));
            if(tmFinish.before(tmStart))
                return workHours;
        }
        
        if(tmFinish.before(tmStart))
            return workHours;
        
        while(tmFinish.getTimeInMillis() - tmStart.getTimeInMillis() >= DA){
            if(!isWorkDay(tmStart)){
                unWork += mlsToStartDay(tmStart, true);
            } else{
                workHours += mlsToEndDay(tmStart, false);
                unWork += mlsToStartDay(tmStart, true) - mlsToEndDay(tmStart, false);
                if(isBeforeLunch(tmStart)){
                    workHours -= (lunchFinish - lunchStart) * HOU;
                    unWork += (lunchFinish - lunchStart) * HOU;
                }
            }
            tmStart.setTimeInMillis(tmStart.getTimeInMillis() + mlsToStartDay(tmStart, true));
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
    
    /**
     *
     * @param deadLine old deadline to be reduced
     * @param workHours Working hours(in mls) for which you need to shift the deadline
     * @return new deadline shifted by workhours
     */
    public Calendar getNewDeadLine(Calendar deadLine, long workHours){
        long timeToDayStart = 0;
        Calendar newDeadLine = (Calendar) deadLine.clone();
        while(!isWorkDay(newDeadLine)){
            newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, true));
            if(deadLine.getTimeInMillis() - newDeadLine.getTimeInMillis() > 7 * DA)
                return deadLine;
        }
        while(workHours > 0){
            switch(isWorkHours(newDeadLine)){
                case BEFORE_WORK_HOURS: // если до начала рабочего дня вычитаем дедлайн до 17-00 предыдущего дня
                    newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, true));
                    timeToDayStart = 0;
                    break;
                case AFTER_WORK_HOURS: // если после рабочего дня вычитаем дедлайн до 17-00 этого дня
                    newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, false));
                    timeToDayStart = 0;
                    break;
                case IS_LUNCH_HOURS: // если попадаем на обеденный перерыв - сдвигаем дедлайн на начало обеда и возвращаем обеденное в workHours
                    timeToDayStart = 0 - skipLunch(newDeadLine, true);
                    newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - skipLunch(newDeadLine, false));
                    break;
                case IS_WORK_HOURS:
                    if(isWorkDay(newDeadLine)){ // если рабочий день
                        if(!isBeforeLunch(newDeadLine)){  // если после обеда
                            timeToDayStart = mlsToStartDay(newDeadLine, false) - ((lunchFinish - lunchStart) * HOU); //+1;
                            if(workHours > timeToDayStart)
                                newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, true));
                            else
                                if(workHours > (dayFinish - lunchFinish) * HOU){
                                    newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - (workHours + ((lunchFinish - lunchStart) * HOU)));
                                }else{
                                    newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - workHours);
                                }
                        }else{  //если до обеда
                            timeToDayStart = mlsToStartDay(newDeadLine, false);
                            if(timeToDayStart == 0)
                                timeToDayStart = mlsToStartDay(newDeadLine, true);
                            if(workHours > timeToDayStart)  // если рабочих часов больше чем до начала дня вычитаем часы до начала дня, дедлайн переводин на конец предыдущего дня
                                newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, true));
                            else // если рабочих часов меньше чем часов до начала дня - дедлайн двигаем на кочичество рабочих 
                                newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - workHours);
                        }
                    }else{ // если выходной
                        timeToDayStart = 0;
                        newDeadLine.setTimeInMillis(newDeadLine.getTimeInMillis() - mlsToEndDay(newDeadLine, true)); 
                    }
                    break;
            }
            workHours -= timeToDayStart;
        }
        
        return newDeadLine;
    }
    
    private boolean isWorkDay(Calendar cl){
        
        for(ForceWorkday fwd : forceWorkdays){
            if(cl.get(Calendar.DAY_OF_MONTH) == fwd.getDay() &&
                    cl.get(Calendar.MONTH) == fwd.getMonth()&&
                    cl.get(Calendar.YEAR) == fwd.getYear()){
                return true;
            }
        }
        
        for(Weekends w : weekends){
            if(cl.get(Calendar.DAY_OF_WEEK) == w.getValue()){
                return false;
            }
        }
        
        for(Holiday h : holidays){
            if(cl.get(Calendar.DAY_OF_MONTH) == h.getDay() &&
                    cl.get(Calendar.MONTH) == h.getMonth()&&
                    cl.get(Calendar.YEAR) == h.getYear()){
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
        if(cl.get(Calendar.HOUR_OF_DAY) < lunchFinish && (
                cl.get(Calendar.HOUR_OF_DAY) > lunchStart) || (cl.get(Calendar.HOUR_OF_DAY) == lunchStart && cl.get(Calendar.MINUTE) != 0)){
            return IS_LUNCH_HOURS;
        }
        return IS_WORK_HOURS;
    }
    
    private boolean isBeforeLunch(Calendar cl) {
        return ((cl.get(Calendar.HOUR_OF_DAY) < lunchStart) ||
                (cl.get(Calendar.HOUR_OF_DAY) == lunchStart && cl.get(Calendar.MINUTE) == 0));
    }
    
    /**
     *
     * @param date 
     * @param forvard skip direction flag. true - to return time to end of break. false - to return time to start break
     * @return mls to the begining of the selected working day
     */
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
    
    /**
     *
     * @param date 
     * @param isBefore if false - means the calculation for the current day, otherwise before the previous day
     * @return mls to the end of the selected working day
     */
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
    
    /**
     *
     * @param date 
     * @param forvard skip direction flag. true - to return time to end of break. false - to return time to start break
     * @return mls to skip lunch break
     */
    private long skipLunch(Calendar date, boolean forvard){
        Calendar clLunch = (Calendar) date.clone();
        if(forvard){
            clLunch.set(Calendar.HOUR_OF_DAY, lunchFinish);
        }else{
            clLunch.set(Calendar.HOUR_OF_DAY, lunchStart);
        }
        clLunch.set(Calendar.MINUTE, 0);
        clLunch.set(Calendar.SECOND, 0);
        clLunch.set(Calendar.MILLISECOND, 0);
        
        return abs(date.getTimeInMillis() - clLunch.getTimeInMillis());
    }
}

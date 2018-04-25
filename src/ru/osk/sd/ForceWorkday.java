/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.osk.sd;

/**
 *
 * @author dev1
 */
public class ForceWorkday {
    
    private final int day;
    private final int month;
    private final int year;
    
    public ForceWorkday(int day, int month, int year){
        this.day = day;
        this.month = month - 1;
        this.year = year;
    }

    /**
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }
}

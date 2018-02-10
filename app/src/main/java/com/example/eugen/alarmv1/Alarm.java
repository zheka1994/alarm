package com.example.eugen.alarmv1;


import java.util.UUID;

/**
 * Created by Eugen on 30.01.2018.
 */

public class Alarm {
    private UUID Id;
    private String Name;
    private int Hours;
    private int Minutes;
    private int State;
    private String Melodie;
    private String daysOfWeek;

    public void setName(String name) {
        Name = name;
    }

    public void setHours(int hours) {
        Hours = hours;
    }

    public void setMinutes(int minutes) {
        Minutes = minutes;
    }

    public void setState(int state) {
        State = state;
    }

    public void setMelodie(String melodie) {
        Melodie = melodie;
    }

    public UUID getId() {

        return Id;
    }

    public String getName() {
        return Name;
    }

    public int getHours() {
        return Hours;
    }

    public int getMinutes() {
        return Minutes;
    }

    public int getState() {
        return State;
    }

    public String getMelodie() {
        return Melodie;
    }

    public Alarm(String name, int hours, int minutes, int state, String melodie) {
        Id = UUID.randomUUID();
        Name = name;
        Hours = hours;
        Minutes = minutes;
        State = state;
        Melodie = melodie;
        daysOfWeek = "1111111";
    }
    public Alarm(UUID id){
        Id = id;
        daysOfWeek = "1111111";
    }

    public Alarm(){
        Id = UUID.randomUUID();
        daysOfWeek = "1111111";
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}

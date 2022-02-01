import java.util.Scanner;

public class Time {
    int seconds_main;
    int minutes_main;
    int hours_main;

    public Time(int hours, int minutes, int seconds) {
        this.hours_main = hours;
        this.minutes_main = minutes;
        this.seconds_main = seconds;
    }

    public static Time difference(Time start, Time stop)
    {
        Time diff = new Time(0, 0, 0);

        if(start.seconds_main > stop.seconds_main){
            --stop.minutes_main;
            stop.seconds_main += 60;
        }
        diff.seconds_main = stop.seconds_main - start.seconds_main;

        if(start.minutes_main > stop.minutes_main){
            --stop.hours_main;
            stop.minutes_main += 60;
        }
        diff.minutes_main = stop.minutes_main - start.minutes_main;
        diff.hours_main = stop.hours_main - start.hours_main;

        return(diff);
    }
}

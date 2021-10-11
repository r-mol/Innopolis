public class Time {

    static final int MAX_HOUR = 23;
    static final int MAX_MINUTE = 60;
    static final int MAX_SECOND = 60;
    static final int MIN_TIME_VALUE = 0;

    private int hours;
    private int minutes;
    private int seconds;

    /**
     * This is a constructor of a Time class
     *
     * @param hours the hours
     * @param minutes the minutes
     * @param seconds the seconds
     */
    public Time(int hours, int minutes, int seconds) {
        if (hours >= MIN_TIME_VALUE && hours <= MAX_HOUR) {
            this.hours = hours;
        } else {
            System.out.println("Error: hour value is out of boundaries!");
            System.exit(1);
        }

        if (minutes >= MIN_TIME_VALUE && minutes <= MAX_MINUTE) {
            this.minutes = minutes;
        } else {
            System.out.println("Error: minute value is out of boundaries!");
            System.exit(2);
        }

        if (seconds >= MIN_TIME_VALUE && seconds <= MAX_SECOND) {
            this.seconds = seconds;
        } else {
            System.out.println("Error: second value is out of boundaries!");
            System.exit(3);
        }
    }

    /**
     * This is a getter for hours
     *
     * @return number of hours
     */
    public int getHours() {
        return hours;
    }

    /**
     * This is a getter for minutes
     *
     * @return number of minutes
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * This is a getter for seconds
     *
     * @return number of seconds
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * This method allows incrementation of a Time
     * by 1 second
     *
     */
    public Time increment() {
        if (seconds != MAX_SECOND) {
            seconds++;
        } else {
            seconds = MIN_TIME_VALUE;
            if (minutes != MAX_MINUTE) {
                minutes++;
            } else {
                minutes = MIN_TIME_VALUE;
                if (hours != MAX_HOUR) {
                    hours++;
                } else {
                    hours = MIN_TIME_VALUE;
                }
            }
        }
        return this;
    }

    /**
     * This a method displaying value of a Time instance
     *
     * @return the string with Time instance object
     */
    @Override
    public String toString() {
        return "Time{" +
                "hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }
}
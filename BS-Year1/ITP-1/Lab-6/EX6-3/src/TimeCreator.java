public class TimeCreator{

    /**
     * The entry point of the TimeCreator program.
     * It allows creation of times, calculating their
     * difference and doing incrementation of a time
     *
     * @param args Array with parameters of the program
     */
    public static void main(String[] args) {
        Time t1 = new Time(12,50, 20);
        Time t2 = new Time(13,0, 18);
        System.out.println("The difference in time is " + getDifference(t1, t2));
        System.out.println("Time before incrementation: " + t1);
        t1.increment().increment().increment();
        System.out.println("Time after incrementation: " + t1);
    }

    /**
     * This method allows to calculate the difference between
     * starting and ending time
     *
     * @param start starting time
     * @param stop ending time
     * @return the difference between start and stop
     */
    public static Time getDifference(Time start, Time stop) {
        int hourDifference;
        int minuteDifference;
        int secondDifference;
        int startInSeconds = start.getSeconds() + start.getMinutes() * Time.MAX_SECOND +
                start.getHours() * Time.MAX_MINUTE * Time.MAX_SECOND;
        int stopInSeconds = stop.getSeconds() + stop.getMinutes() * Time.MAX_SECOND +
                stop.getHours() * Time.MAX_MINUTE * Time.MAX_SECOND;

        secondDifference = (stopInSeconds - startInSeconds) % Time.MAX_SECOND;
        minuteDifference = ((stopInSeconds - startInSeconds - secondDifference)
                / Time.MAX_SECOND) % Time.MAX_MINUTE;
        hourDifference = ((stopInSeconds - startInSeconds - secondDifference -
                minuteDifference * Time.MAX_SECOND) / Time.MAX_SECOND / Time.MAX_MINUTE);

        return new Time(hourDifference, minuteDifference, secondDifference);
    }
}

/**
 * program for solving task A.Simple fraud detection.
 * program output number of alerts the Bank sent to the client.
 * @author Kdrina Denisova
 * Group №7
 * TG: @karinadenisova
 * Email: k.denisova@innopolis.university
 */

import javafx.util.Pair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    /**
     * just main function
     * @param args not used
     * @throws ParseException standard parse exception
     */
    public static void main(String[] args) throws ParseException {
        Scanner input = new Scanner(System.in);
        List<Pair<Date, Double>> pairArrayList = new ArrayList<>();
        String[] splitStrings = input.nextLine().split(" ");
        int count = Integer.parseInt(splitStrings[0]);
        int countOfTrailingDays = Integer.parseInt(splitStrings[1]);

        //fill array list with pairs
        while (count > 0) {
            String temp = input.nextLine();
            splitStrings = temp.split(" \\$");

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(splitStrings[0]);

            pairArrayList.add(new Pair<>(date, Double.parseDouble(splitStrings[1])));

            count--;
        }

        countingSort(pairArrayList);

        int answer = alerts(pairArrayList, countOfTrailingDays);

        System.out.println(answer);
    }

    /**
     * function that count the number of alerts the Bank sent to the client.
     * @param pairs array list that keep pair of variable - date and count of money
     * @param numberTrailingDays contain the number of trailing days that the Bank is tracking
     * @return contain number of notification the Bank sent to client (include "empty" days)
     */
    public static int alerts(List<Pair<Date, Double>> pairs, int numberTrailingDays) {
        Queue<Double> queue = new ArrayDeque<>(numberTrailingDays);
        int alerts;
        int index;

        fillDays(pairs);

        index = firstFillOfQueue(pairs,numberTrailingDays,queue);

        alerts = mainCountingOfAlerts(index,pairs,numberTrailingDays,queue);

        return alerts;
    }

    /**
     * fill the list with pairs by days with the spending 0 which where are not in list.
     * @param pairs array list that keep pair of variable - date and count of money
     */
    public static void fillDays(List<Pair<Date, Double>> pairs){
        Date previous = null;
        for (int i = 0; i < pairs.size(); i++) {
            Date day = pairs.get(i).getKey();
            if (!(previous == null || previous.compareTo(findPrevDate(day)) == 0 || previous.compareTo(day) == 0)) {
                while (previous.compareTo(findPrevDate(day)) != 0) {
                    previous = findNextDate(previous);
                    pairs.add(i, new Pair<>(previous, (double) 0));
                    i++;
                }
            }
            previous = day;
        }
    }

    /**
     * fill queue with the spending in a range of the number of the trailing days.
     * @param pairs array list that keep pair of variable - date and count of money
     * @param numberTrailingDays range of days
     * @param queue contain amount of spent by client money, sorted by days(that function also sorted it by money), for trailing days that Bank tracking
     * @return last index
     */
    public static int firstFillOfQueue(List<Pair<Date, Double>> pairs, int numberTrailingDays, Queue<Double> queue){
        double spending = 0;
        int count = 0;
        int index;

        for (index = 0; count < numberTrailingDays && index < pairs.size() - 1; index++) {
            if (pairs.get(index).getKey().equals(pairs.get(index + 1).getKey())) {
                spending += pairs.get(index).getValue();
            } else {
                spending += pairs.get(index).getValue();
                queue.offer(spending);
                spending = 0;
                count++;
            }
        }
        return index;
    }

    /**
     * the main function which count amount of alerts in a queue of days
     * @param index index from which program should begin work
     * @param pairs array list that keep pair of variable - date and count of money
     * @param numberTrailingDays range of days
     * @param queue contain amount of spent by client money, sorted by days(that function also sorted it by money), for trailing days that Bank tracking
     * @return the amount of alerts
     */
    public static int mainCountingOfAlerts(int index,List<Pair<Date, Double>> pairs, int numberTrailingDays, Queue<Double> queue){
        Date previous = pairs.get(numberTrailingDays - 1).getKey();
        Date initial ;
        double spending = 0;
        int alerts = 0;
        for (; index < pairs.size(); index++) {
            double limit = 2 * median(queue);
            initial = pairs.get(index).getKey();

            if (!previous.equals(initial) && pairs.get(index).getValue() >= limit) {
                previous = initial;
                spending = pairs.get(index).getValue();

                if (pairs.get(index).getValue() != 0) {
                    alerts += 1;
                }
            } else if (previous.equals(initial)) {
                spending += pairs.get(index).getValue();

                if (spending >= limit) {
                    alerts += 1;
                }
            } else {
                previous = initial;
                spending = pairs.get(index).getValue();
            }

            if (index == pairs.size() - 1 || !pairs.get(index + 1).getKey().equals(initial)) {
                queue.offer(spending);
                queue.poll();
                spending = 0;
            }
        }
        return alerts;
    }

    /**
     * function for sorting the vector by date
     * @param pairArrayList vector array list that keep pair of variable - date and count of money
     */
    public static void countingSort(List<Pair<Date, Double>> pairArrayList) {
        List<Integer> arrayListOfConvertedDays = new ArrayList<>();
        List<Pair<Date, Double>> sorted = new ArrayList<>(Collections.nCopies(pairArrayList.size(),null));

        //fill list with converted date
        for (int i = 0; i < pairArrayList.size(); i++) {
            arrayListOfConvertedDays.add(i, convertToDays(pairArrayList.get(i).getKey()));
        }

        int min = Collections.min(arrayListOfConvertedDays);
        int max = Collections.max(arrayListOfConvertedDays);
        int[] counting = new int[max - min + 1];

        //count how much repetitive days
        for (int i = 0; i < pairArrayList.size(); ++i) {
            ++counting[arrayListOfConvertedDays.get(i) - min];
        }

        for (int i = 1; i <= max - min; ++i) {
            counting[i] += counting[i - 1];
        }

        for (int i = pairArrayList.size() - 1; i >= 0; i--) {
            sorted.add(counting[arrayListOfConvertedDays.get(i) - min] - 1, new Pair<>(pairArrayList.get(i).getKey(), pairArrayList.get(i).getValue()));
            sorted.remove(counting[arrayListOfConvertedDays.get(i) - min]);
            --counting[arrayListOfConvertedDays.get(i) - min];
        }


        for (int i = 0; i < pairArrayList.size(); i++) {
            pairArrayList.set(i, sorted.get(i));
        }

    }

    /**
     * function that find median by given formula
     * @param queue contain amount of spent by client money, sorted by days(that function also sorted it by money), for trailing days that Bank tracking
     * @return value of median
     */
    public static double median(Queue<Double> queue) {
        Queue<Double> tempQueue = new ArrayDeque<>(queue);
        ArrayList<Double> spending = new ArrayList<>();

        while (!tempQueue.isEmpty()) {
            spending.add(tempQueue.poll());
        }

        heapSort(spending);

        //two cases for odd and even amount of spending
        if (spending.size() % 2 != 0) {
            return spending.get((spending.size() - 1) / 2);
        } else {
            return (spending.get(spending.size() / 2 - 1) + spending.get(spending.size() / 2)) / 2;
        }
    }

    /**
     * just heap sort
     * @param list list of elements
     */
    public static void heapSort(List<Double> list) {
        int size = list.size();

        for (int i = size / 2 - 1; i >= 0; i--) {
            hapi(list, size, i);
        }

        // extract an element from heap one by one
        for (int i = size - 1; i > 0; i--) {
            // move current root to end
            double temp = list.get(0);
            list.set(0,list.get(i));
            list.set(i, temp);

            hapi(list, i, 0);
        }
    }

    /**
     * to hapi a subtree rooted with node i which is an index in tempList, n is size of heap
     * @param list list with elements
     * @param size size
     * @param index index in lis
     */
    static void hapi(List<Double> list, int size, int index) {
        int largest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        if (left < size && list.get(left) > list.get(largest)) {
            largest = left;
        }

        if (right < size && list.get(right) > list.get(largest)) {
            largest = right;
        }

        if (largest != index) {
            double swap = list.get(index);
            list.set(index,list.get(largest));
            list.set(largest, swap);

            hapi(list, size, largest);
        }
    }

    /**
     * function for converting data into days
     * @param day date needed to convert
     * @return days
     */
    public static int convertToDays(Date day) {
        return (int) (day.getTime() / 1000 / 60 / 60 / 24);
    }

    /**
     * function that find next day
     * @param day date needed to convert
     * @return next day
     */
    public static Date findNextDate(Date day) {
        return new Date(day.getTime() + 1000 * 60 * 60 * 24);
    }

    /**
     * function that find previous day
     * @param day date needed to convert
     * @return previous day
     */
    public static Date findPrevDate(Date day) {
        return new Date(day.getTime() - 1000 * 60 * 60 * 24);
    }
}
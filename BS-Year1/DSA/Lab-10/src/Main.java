import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class Main {

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        List<Integer> finalArr = new ArrayList<>();
        for (int j : nums1) {
            finalArr.add(j);
        }
        for (int j : nums2) {
            finalArr.add(j);
        }

        Collections.sort(finalArr);

        if (finalArr.size() % 2 == 0) {
            return (double) (finalArr.get(finalArr.size() / 2 - 1) + finalArr.get(finalArr.size() / 2)) / 2;
        } else {
            return (double) finalArr.get((finalArr.size() - 1) / 2);
        }
    }

    public static void main(String[] args) throws ParseException {
//        int[] nums1 = {1, 3};
//        int[] nums2 = {2};
//        System.out.println(findMedianSortedArrays(nums1, nums2));
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-29");
        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-29");

        if (date2.compareTo(date1) > 0) {
            System.out.println("big");
        } else {
            System.out.println("less");
        }
    }
}

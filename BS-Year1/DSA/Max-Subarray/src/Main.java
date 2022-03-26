public class Main {
    public int[] FindMaxCrossingSubarray(int[] A, int low, int mid, int high) {
        int leftSum = -1000000;
        int rightSum = -1000000;
        int sum = 0;
        int maxLeft = 0;
        int maxRight = 0;

        for (int i = mid; i >= low; i--) {
            sum += A[i];
            if (sum > leftSum) {
                leftSum = sum;
                maxLeft = i;
            }
        }
        sum = 0;
        for (int j = mid+1; j<=high;j++){
            sum += A[j];
            if(sum > rightSum){
                rightSum = sum;
                maxRight = j;
            }
        }
        int []element = new int [3];
        element[0] = maxLeft;
        element[1] = maxRight;
        element[2] = leftSum+rightSum;
        return element;
    }



    public static void main(String[] args) {

    }
}

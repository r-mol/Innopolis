public class Main {
    public static void main(String[] args){
        int R = 3;
        int M = 5;
        int [][] E = {{0,0, 0, 0},{0,45,50,20},{0,70,70,45},{0,90,80,75},{0,105,100,110},{0,110,120,140}};
        int[][] A = new int[M+1][R+1];
        for (int j = 0; j <= M; ++j) {
            for (int i = R; i >= 1; --i) {
                if (i == R) {
                    A[j][i] = E[j][R];
                } else {
                    int max_performance = 0;
                    for (int k = 0; k <= j; k++) {
                        int performance = A[j-k][i+1] + E[k][i];
                        if (performance > max_performance) {
                            max_performance = performance;
                        }

                    }

                    A[j][i] = max_performance;
                }
            }
        }
        System.out.println(A[M][1]);
    }
}

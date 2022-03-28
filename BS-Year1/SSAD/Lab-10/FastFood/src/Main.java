public class Main {
    public static void main(String[] args) {
        FastFood fastfood = new FastFood
                .FastFoodBuilder("Gamburger")
                .tomato(true)
                .peperone(false)
                .cheese(true)
                .desire(FastFood.FastFoodBuilder.Desire.BIG)
                .letucci(false)
                .build();
        System.out.println(fastfood.toString());
    }
}

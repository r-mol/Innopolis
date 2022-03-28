public class FastFood {
    private String name;
    enum Desire {MINI,MEDIUM,BIG}
    private boolean cheese;
    private boolean peperone;
    private boolean letucci;
    private boolean tomato;
    private String desire;

    private FastFood(){}

    public String toString(){
        return name + ": " +
                "\nContains cheese: " + cheese +
                "\nContains peperone: " + peperone +
                "\nContains letucci: " + letucci +
                "\nContains tomato: " + tomato +
                "\nDegree of desire is: " + desire;
    }

    public static class FastFoodBuilder{

        private final String name;
        enum Desire {MINI,MEDIUM,BIG}
        private boolean cheese;
        private boolean peperone;
        private boolean letucci;
        private boolean tomato;
        private String desire;

        public FastFoodBuilder(String name) {
            this.name = name;
        }


        public FastFoodBuilder cheese(boolean cheese) {
            this.cheese = cheese;
            return this;
        }

        public FastFoodBuilder peperone(boolean peperone) {
            this.peperone = peperone;
            return this;
        }

        public FastFoodBuilder letucci(boolean letucci) {
            this.letucci = letucci;
            return this;
        }

        public FastFoodBuilder tomato(boolean tomato) {
            this.tomato = tomato;
            return this;
        }

        public FastFoodBuilder desire(Desire des){
            switch(des){
                case MINI -> this.desire = "low";
                case MEDIUM -> this.desire = "medium";
                case BIG -> this.desire = "rear";
            }
            return this;
        }

        public FastFood build() {
            FastFood fastfood = new FastFood();
            fastfood.name = this.name;
            fastfood.cheese = this.cheese;
            fastfood.peperone = this.peperone;
            fastfood.letucci = this.letucci;
            fastfood.tomato = this.tomato;
            fastfood.desire = this.desire;

            return fastfood;
        }
    }
}

public enum Sex {
    FEMALE, MALE;

    private Sex opposite;

    static {
        FEMALE.opposite = MALE;
        MALE.opposite = FEMALE;
    }

    public Sex OppositeSex() {
        return opposite;
    }
}

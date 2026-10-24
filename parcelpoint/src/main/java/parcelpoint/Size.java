package parcelpoint;

public enum Size {
    SMALL(10.0),
    MEDIUM(15.0),
    LARGE(20.0);

    private final double dailyRate;

    Size(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public boolean fitsIn(Size lockerSize) {
        return this.compareTo(lockerSize) <= 0;
    }

    public static Size fromNumber(int number) {
        return switch (number) {
            case 1 -> SMALL;
            case 2 -> MEDIUM;
            case 3 -> LARGE;
            default -> throw new IllegalArgumentException("Size must be 1, 2 or 3");
        };
    }
}

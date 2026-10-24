package bikeshare;

public enum MemberType {
    STUDENT(2),
    STAFF(3);

    private final int ratePerMinute;

    MemberType(int ratePerMinute) {
        this.ratePerMinute = ratePerMinute;
    }

    public long calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException(
                    "Minutes must be zero or greater.");
        }
        return (long) minutes * ratePerMinute;
    }
}

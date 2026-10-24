package bikeshare;

public record CompletedRide(
        String memberId,
        MemberType memberType,
        String bikeId,
        Station returnStation,
        int minutes,
        long fee) {
}

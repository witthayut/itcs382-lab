package bikeshare;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BikeShareService {
    private final BikeInventory inventory;
    private final RideRepository repository;
    private final Map<String, Rental> rentals = new HashMap<>();

    private record Rental(String bikeId, MemberType memberType) {
    }

    public BikeShareService(
            BikeInventory inventory, RideRepository repository) {
        this.inventory = Objects.requireNonNull(inventory);
        this.repository = Objects.requireNonNull(repository);
    }

    public String rent(
            String memberId, MemberType memberType, Station station) {
        validateMemberId(memberId);
        Objects.requireNonNull(memberType);
        Objects.requireNonNull(station);

        if (rentals.containsKey(memberId)) {
            throw new IllegalArgumentException(
                    "This member already has a bike.");
        }

        String bike = inventory.takeBike(station);
        rentals.put(memberId, new Rental(bike, memberType));
        return bike;
    }

    public CompletedRide returnBike(
            String memberId, Station station, int minutes)
            throws IOException {
        validateMemberId(memberId);
        Objects.requireNonNull(station);

        Rental rental = rentals.get(memberId);
        if (rental == null) {
            throw new IllegalArgumentException(
                    "No active rental for this member.");
        }

        long fee = rental.memberType().calculateFee(minutes);
        CompletedRide ride = new CompletedRide(
                memberId, rental.memberType(), rental.bikeId(),
                station, minutes, fee);

        repository.save(ride);
        inventory.returnBike(rental.bikeId(), station);
        rentals.remove(memberId);
        return ride;
    }

    private void validateMemberId(String memberId) {
        if (memberId == null || !memberId.matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException(
                    "Member ID must contain only letters, digits, _ or -.");
        }
    }
}

package bikeshare;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;

public final class BikeInventory {
    private final Map<Station, Deque<String>> available =
            new EnumMap<>(Station.class);

    public BikeInventory() {
        int number = 1;
        for (Station station : Station.values()) {
            Deque<String> bikes = new ArrayDeque<>();
            bikes.addLast("B" + number++);
            bikes.addLast("B" + number++);
            available.put(station, bikes);
        }
    }

    public String takeBike(Station station) {
        String bike = available.get(station).pollFirst();
        if (bike == null) {
            throw new IllegalArgumentException(
                    "No bikes available at " + station);
        }
        return bike;
    }

    public void returnBike(String bikeId, Station station) {
        available.get(station).addLast(bikeId);
    }
}

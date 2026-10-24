package bikeshare;

import java.nio.file.Path;
import java.util.Scanner;

public final class Main {
    public static void main(String[] args) {
        RideRepository repository =
                new CsvRideRepository(Path.of("rides.csv"));
        BikeShareService service =
                new BikeShareService(new BikeInventory(), repository);

        try (Scanner input = new Scanner(System.in)) {
            new ConsoleMenu(service, input, System.out).run();
        }
    }
}

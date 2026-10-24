package bikeshare;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

public final class ConsoleMenu {
    private final BikeShareService service;
    private final Scanner input;
    private final PrintStream output;

    public ConsoleMenu(
            BikeShareService service, Scanner input, PrintStream output) {
        this.service = service;
        this.input = input;
        this.output = output;
    }

    public void run() {
        output.println("""
                Commands:
                  rent <memberId> <student|staff> <station>
                  return <memberId> <station> <minutes>
                  quit
                Stations: Library, Canteen, Dorm
                One active rental per member.
                """);

        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.equalsIgnoreCase("quit")) {
                break;
            }

            try {
                execute(line.split("\\s+"));
            } catch (IllegalArgumentException e) {
                output.println("Invalid input: " + e.getMessage());
            } catch (IOException | SecurityException e) {
                output.println(
                        "Could not save the ride; rental remains active: "
                                + e.getMessage());
            }
        }

        if (input.ioException() != null) {
            output.println(
                    "Input error: " + input.ioException().getMessage());
        }
    }

    private void execute(String[] words) throws IOException {
        if (words.length != 4) {
            throw new IllegalArgumentException(
                    "Enter a command with exactly three arguments.");
        }

        switch (words[0].toLowerCase(Locale.ROOT)) {
            case "rent" -> rent(words[1], words[2], words[3]);
            case "return" -> returnBike(words[1], words[2], words[3]);
            default -> throw new IllegalArgumentException(
                    "Unknown command.");
        }
    }

    private void rent(
            String memberId, String memberType, String stationName) {
        MemberType type = MemberType.valueOf(
                memberType.toUpperCase(Locale.ROOT));
        Station station = Station.valueOf(
                stationName.toUpperCase(Locale.ROOT));

        String bike = service.rent(memberId, type, station);
        output.println("Rented " + bike + " from " + station);
    }

    private void returnBike(
            String memberId, String stationName, String minutesText)
            throws IOException {
        Station station = Station.valueOf(
                stationName.toUpperCase(Locale.ROOT));
        int minutes = Integer.parseInt(minutesText);

        CompletedRide ride = service.returnBike(
                memberId, station, minutes);
        printReceipt(ride.bikeId(), ride.minutes(), ride.fee());
    }

    private void printReceipt(String bikeId, int minutes, long fee) {
        output.printf(
                "Receipt: bike=%s, minutes=%d, fee=%d baht%n",
                bikeId, minutes, fee);
    }
}

package bikeshare;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class Main {
    public static String[] stations = {"Library", "Canteen", "Dorm"};
    public static String[] bikes = {"B1", "B2", "B3", "B4", "B5", "B6"};

    // Station index for each bike; -1 means rented.
    public static int[] locations = {0, 0, 1, 1, 2, 2};
    public static String[] members = new String[6];

    // 1 = student, 2 = staff.
    public static int[] memberTypes = new int[6];
    public static Path rideFile = Path.of("rides.csv");

    public static void main(String[] args) {
        System.out.println("""
                Commands:
                  rent <memberId> <student|staff> <station>
                  return <memberId> <station> <minutes>
                  quit
                Stations: Library, Canteen, Dorm
                One active rental per member.
                """);

        try (Scanner input = new Scanner(System.in)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] words = line.split("\\s+");
                if (words.length == 1 && words[0].equalsIgnoreCase("quit")) {
                    break;
                }

                try {
                    if (words.length != 4) {
                        throw new IllegalArgumentException(
                                "Enter a command with exactly three arguments.");
                    }

                    if (words[0].equalsIgnoreCase("rent")) {
                        process(words, true);
                    } else if (words[0].equalsIgnoreCase("return")) {
                        process(words, false);
                    } else {
                        throw new IllegalArgumentException("Unknown command.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                } catch (IOException | SecurityException e) {
                    System.out.println(
                            "Could not save the ride; rental remains active: "
                                    + e.getMessage());
                }
            }

            if (input.ioException() != null) {
                System.err.println(
                        "Input error: " + input.ioException().getMessage());
            }
        }
    }

    public static void process(String[] words, boolean renting)
            throws IOException {
        String member = words[1];
        if (!member.matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException(
                    "Member ID must contain only letters, digits, _ or -.");
        }

        String stationName = renting ? words[3] : words[2];
        int station = -1;
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].equalsIgnoreCase(stationName)) {
                station = i;
                break;
            }
        }
        if (station == -1) {
            throw new IllegalArgumentException("Unknown station.");
        }

        int activeBike = -1;
        for (int i = 0; i < bikes.length; i++) {
            if (member.equals(members[i])) {
                activeBike = i;
                break;
            }
        }

        if (renting) {
            int type;
            if (words[2].equalsIgnoreCase("student")) {
                type = 1;
            } else if (words[2].equalsIgnoreCase("staff")) {
                type = 2;
            } else {
                throw new IllegalArgumentException(
                        "Member type must be student or staff.");
            }

            if (activeBike != -1) {
                throw new IllegalArgumentException(
                        "This member already has a bike.");
            }

            for (int i = 0; i < bikes.length; i++) {
                if (locations[i] == station) {
                    locations[i] = -1;
                    members[i] = member;
                    memberTypes[i] = type;
                    System.out.println(
                            "Rented " + bikes[i] + " from " + stations[station]);
                    return;
                }
            }
            System.out.println("No bikes available at " + stations[station]);
        } else {
            if (activeBike == -1) {
                throw new IllegalArgumentException(
                        "No active rental for this member.");
            }

            int minutes = Integer.parseInt(words[3]);
            if (minutes < 0) {
                throw new IllegalArgumentException(
                        "Minutes must be zero or greater.");
            }

            long fee = minutes * (memberTypes[activeBike] == 1 ? 2L : 3L);

            // Append before releasing the bike so a save failure keeps it rented.
            try (BufferedWriter writer = Files.newBufferedWriter(
                    rideFile, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(member + "," + memberTypes[activeBike] + ","
                        + bikes[activeBike] + "," + stations[station] + ","
                        + minutes + "," + fee);
                writer.newLine();
            }

            locations[activeBike] = station;
            members[activeBike] = null;
            memberTypes[activeBike] = 0;

            System.out.printf(
                    "Receipt: bike=%s, minutes=%d, fee=%d baht%n",
                    bikes[activeBike], minutes, fee);
        }
    }
}

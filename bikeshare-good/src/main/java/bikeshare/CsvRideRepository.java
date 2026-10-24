package bikeshare;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public final class CsvRideRepository implements RideRepository {
    private final Path file;

    public CsvRideRepository(Path file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public void save(CompletedRide ride) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(String.join(",",
                    escape(ride.memberId()),
                    ride.memberType().name(),
                    escape(ride.bikeId()),
                    ride.returnStation().name(),
                    Integer.toString(ride.minutes()),
                    Long.toString(ride.fee())));
            writer.newLine();
        }
    }

    private String escape(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}

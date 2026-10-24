package bikeshare;

import java.io.IOException;

public interface RideRepository {
    void save(CompletedRide ride) throws IOException;
}

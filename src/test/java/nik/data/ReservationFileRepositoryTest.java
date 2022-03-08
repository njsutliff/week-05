package nik.data;
import nik.models.Reservation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ReservationFileRepositoryTest {

    @Test
    void findByHostIdReturnsNotNull() {
        ReservationFileRepository repository = new ReservationFileRepository("./data/test");

        List<Reservation> r = repository.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");

        assertNotNull(r);

    }

    @Test
    void findByHostId() {
        ReservationFileRepository repository = new ReservationFileRepository("./data/test");

        List<Reservation> r = repository.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");

        assertFalse(r.size() == 0);
    }
}
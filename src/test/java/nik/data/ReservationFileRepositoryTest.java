package nik.data;

import nik.models.Host;
import nik.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ReservationFileRepositoryTest {
    static final String SEED_DIR_PATH = "./data/reservations";
    static final String TEST_DIR_PATH = "./data/test";
    HostFileRepository hostRepo = new HostFileRepository("./data/hosts.csv");
    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    @Test
    void findByHostIdFailsIfWrongId(){
        List<Reservation> r = repository.findByHostId("2e72f86c-badId-4265-b4f1-304dea8762db");
        assertEquals(0, r.size());
    }
    @Test
    void findByHostIdReturnsNotNull() {
        List<Reservation> r = repository.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        System.out.println(r.size());
        assertNotNull(r);
    }
    @Test
    void findByHostIdSuccess() {
        List<Reservation> r = repository.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        System.out.println(r.size());
        assertFalse(r.size() == 0);
    }
    @Test
    void getFutureReservationsSuccess(){
        Host h = hostRepo.getHostFromEmail("eyearnes0@sfgate.com");
        List<Reservation> r = repository.getFutureReservations(h);
        System.out.println(r.size());
        assertNotEquals(0,r.size());//should be true if h has future res which it does
    }
    @Test
    void getFutureReservationsFail(){
        Host h = hostRepo.getHostFromEmail("njsutliff@gmail.com");
        List<Reservation> r = repository.getFutureReservations(h);
        System.out.println(r.size());
        assertEquals(0,r.size());//should be true because wrong email
    }
}
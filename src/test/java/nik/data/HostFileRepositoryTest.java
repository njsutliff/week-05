package nik.data;

import nik.models.Host;
import nik.models.Reservation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {
    static final String TEST_PATH = "./data/test/host-test.csv";
    static HostFileRepository repository = new HostFileRepository(TEST_PATH);


    @Test
    void findReservationGivenEmail() {
        String email = "eyearnes0@sfgate.com";
        Reservation r = repository.findReservationByEmail(email);

        String iD = repository.getIdFromEmail(email);
       System.out.println(iD);
        System.out.println(r.getGuestId());
    }

    @Test
    void findByEmail() {
        String email = "eyearnes0@sfgate.com";
        String iD = repository.getIdFromEmail(email);
        assertEquals("3edda6bc-ab95-49a8-8962-d50b53f84b15", iD);
    }

    @Test
    void getAllHosts() {
        List<Host> h = repository.getAllHosts();
        assertTrue(h.size()==1000);
        System.out.println(h.size());
    }
}
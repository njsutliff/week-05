package nik.data;

import nik.models.Host;
import nik.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ReservationFileRepositoryTest {
    static final String SEED_DIR_PATH = "./data/reservations";
    static final String TEST_DIR_PATH = "./data/test";
    HostFileRepository hostRepo = new HostFileRepository("./data/hosts.csv");
    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);
    GuestFileRepository guestRepo = new GuestFileRepository("data/guests-test.csv");
    @Test
    void testCancel() throws DataException {
      /*  // 1,2021-07-31,2021-08-07,640,2550
        Host h = hostRepo.getHostFromEmail("eyearnes0@sfgate.com");
        List<Reservation> r=  repository.findByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        Reservation res = r.get(0);
       Reservation test = repository.cancelReservation(h, res);
       assertEquals(res, test);*/
    }
    @Test
    void testActuallyDeletes() throws DataException {
      /*  Host h = hostRepo.getHostFromEmail("eyearnes0@sfgate.com");
        List<Reservation> r=  repository.findByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        Reservation res = r.get(0);
        Reservation test = repository.cancelReservation(h, res);
        List<Reservation> result = repository.findByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        Reservation shouldNotExist = result.get(0);//ideally is not res
        System.out.println(test.getGuestId());        System.out.println(test.getId());

        System.out.println(shouldNotExist.getGuestId());        System.out.println(shouldNotExist.getId());

        assertNotEquals(res, shouldNotExist);*/
    }
    @Test
    void findByHostIdFailsIfWrongId() {
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
    void findByHostId() {
       List<Reservation> r=  repository.findByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
       assertEquals(1,Integer.parseInt(r.get(0).getId()));
        System.out.println(r.get(0).getGuestId());
    }

    @Test
    void findByHostIdSuccess() {
        List<Reservation> r = repository.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        System.out.println(r.size());
        assertNotEquals(0, r.size());
    }

    @Test
    void getFutureReservationsSuccess() {
        Host h = hostRepo.getHostFromEmail("eyearnes0@sfgate.com");
        List<Reservation> r = repository.getFutureReservations(h);
        System.out.println(r.size());
        assertNotEquals(0, r.size());//should be true if h has future res which it does
    }

    @Test
    void getFutureReservationsFail() {
        Host h = hostRepo.getHostFromEmail("njsutliff@gmail.com");
        List<Reservation> r = repository.getFutureReservations(h);
        System.out.println(r.size());
        assertEquals(0, r.size());//should be true because wrong email
    }

    @Test
    void createReservationSuccess() throws DataException {
        Host h = hostRepo.getHostFromEmail("eyearnes0@sfgate.com");
        String iD = h.getiD();
        Reservation test = new Reservation();
        test.setId("14");
        test.setStartDate(LocalDate.of(2022, 10, 10));
        test.setEndDate(LocalDate.of(2022, 11, 11));
        test.setGuestId(1);
        test.setTotal(BigDecimal.TEN);
        Reservation r = repository.createReservation(iD, test);
        assertNotNull(r);
    }

    @Test
    void doNotCreateReservationWhen() throws DataException {
        Host h = hostRepo.getHostFromEmail("eyearnes0@sfgate.com");

    }
}
package nik.data;

import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {
    static final String GUEST_PATH = "./data/guests-test.csv";
    HostFileRepository hostRepo = new HostFileRepository("./data/hosts.csv");
    GuestFileRepository guestRepo = new GuestFileRepository(GUEST_PATH);
    ReservationFileRepository reservationRepo = new ReservationFileRepository("./data/test");
    @Test
    void findAllGuests() {
        ArrayList<Guest> guests = guestRepo.findAll();
        assertEquals(1000,guests.size());
    }

    @Test
    void getGuestByLastName() {
        Guest kike = guestRepo.getGuestByLastName("Kike");
        assertTrue(kike.getLastName().equalsIgnoreCase("Kike"));
    }
    @Test
    void FailsToGetGuestNotExisting() {
        Guest kike = guestRepo.getGuestByLastName("Sutliff");
        assertNull(kike);
    }
    @Test
        void TestGettingGuestsForFirstResAndHost() throws DataException {
        Host h = hostRepo.getHostFromEmail("kdeclerkdc@sitemeter.com");
        Reservation test = new Reservation();
        test.setId("14");
        test.setStartDate(LocalDate.of(2022, 10, 10));
        test.setEndDate(LocalDate.of(2022, 11, 11));
        test.setGuestId(1);
        test.setTotal(BigDecimal.TEN);
        Reservation r = reservationRepo.createReservation(h.getiD(), test);
        List<Guest> expected = guestRepo.getGuestsForHostFromReservation(h, r);
        System.out.println(expected.get(0).firstName);
        System.out.println(expected.get(0).lastName);

        assertTrue(expected.size()!=0);
    }
}

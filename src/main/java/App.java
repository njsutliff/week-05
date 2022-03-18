import nik.data.GuestFileRepository;
import nik.data.HostFileRepository;
import nik.data.ReservationFileRepository;
import nik.domain.GuestService;
import nik.domain.HostService;
import nik.domain.ReservationService;
import nik.models.Reservation;
import nik.ui.ConsoleIO;
import nik.ui.Controller;
import nik.ui.View;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
public class App {
    public static void main(String[] args) {
        ConsoleIO io = new ConsoleIO();
        View view = new View(io);
        ReservationFileRepository repository = new ReservationFileRepository("./data/reservations");
        GuestFileRepository guestRepository = new GuestFileRepository("./data/guests.csv");
        HostFileRepository hostRepository = new HostFileRepository("./data/hosts.csv");

        ReservationService reservationService = new ReservationService(repository, guestRepository);
        HostService hostService = new HostService(hostRepository);
        GuestService guestService = new GuestService(guestRepository);
        Controller controller = new Controller(view, reservationService, hostService, guestService);


        controller.run();
    }
}

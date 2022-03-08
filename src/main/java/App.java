import nik.data.ReservationFileRepository;
import nik.domain.ReservationService;
import nik.models.Reservation;
import nik.ui.ConsoleIO;
import nik.ui.Controller;
import nik.ui.View;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;

@ComponentScan
public class App {
    public static void main(String[] args) {


    ConsoleIO io = new ConsoleIO();
    View view = new View(io);
    ReservationFileRepository repository = new ReservationFileRepository("./data/reservations");
        ReservationService reservationService = new ReservationService(repository);
        Controller controller = new Controller(view, reservationService);

    //ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
   // Controller controller = context.getBean(Controller.class);

        controller.run();
}
}

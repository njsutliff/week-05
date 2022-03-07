import nik.data.ReservationRepository;
import nik.ui.ConsoleIO;
import nik.ui.Controller;
import nik.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class App {
    public static void main(String[] args) {


    ConsoleIO io = new ConsoleIO();
    View view = new View(io);
    Controller controller = new Controller(view);
        ReservationRepository repository = new ReservationRepository("./data/reservations");
    //ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
   // Controller controller = context.getBean(Controller.class);

        controller.run();
}
}

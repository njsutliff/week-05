package nik.ui;

import nik.data.DataException;
import nik.data.ReservationFileRepository;
import nik.domain.ReservationService;
import nik.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Controller {

        private final View view;
    private final ReservationService reservationService;
        public Controller(View view, ReservationService reservationService) {
            this.reservationService = reservationService;
            this.view = view;
        }

        public void run()  {
            view.displayHeader("Welcome to Sustainable Foraging");
            try {
                runAppLoop();
            }
            catch (DataException ex) {
                view.displayException(ex);
            }
            view.displayHeader("Goodbye.");
        }

        private void runAppLoop() throws DataException {
            MainMenuOption option;
            do {
                option = view.selectMainMenuOption();
                switch (option) {
                    case VIEW_RESERVATIONS:
                        viewReservations();
                        break;
                    case MAKE_RESERVATION:
                        makeReservation();
                        break;
                    case EDIT_RESERVATION:
                        editReservation();
                        break;
                    case CANCEL_RESERVATION:
                        cancelReservation();
                        break;

                }
            } while (option != MainMenuOption.EXIT);
        }

    private void viewReservations() {
            view.displayHeader("view Reservations by Host");

            String iD = "2e72f86c-b8fe-4265-b4f1-304dea8762db"; //TEMPORARY
         List<Reservation> r = reservationService.findByHostId(iD);

          view.printReservations(r);
    }

    private void makeReservation() {
        view.displayHeader("make a Reservation");

    }

    private void editReservation() {
            view.displayHeader("edit a Reservation");
    }

    private void cancelReservation() {
            view.displayHeader("Cancel a Reservation");
    }
}

package nik.ui;

import nik.data.DataException;
import nik.data.ReservationFileRepository;
import nik.domain.HostService;
import nik.domain.ReservationService;
import nik.models.Reservation;
import org.springframework.stereotype.Component;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;

@Component
public class Controller {

        private final View view;
    private final ReservationService reservationService;
    private final HostService hostService;

    public Controller(View view, ReservationService reservationService, HostService hostService) {
            this.reservationService = reservationService;
            this.hostService = hostService;
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

    private void viewReservations() {// PROMPT FOR EMAIL
            //TODO host three layer
        //TODO host has <Host> findByEmail

            view.displayHeader("view Reservations by Host");
        String email =  view.getEmail();
        String id = hostService.getIdFromEmail(email);

        List<Reservation> result =  reservationService.findByHostId(id);
          view.printReservations(result);
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

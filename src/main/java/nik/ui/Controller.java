package nik.ui;

import nik.data.DataException;
import org.springframework.stereotype.Component;

@Component
public class Controller {

        private final View view;

        public Controller(View view) {
            /**
            this.foragerService = foragerService;
            this.forageService = forageService;
            this.itemService = itemService;
             **/
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

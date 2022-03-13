package nik.ui;

import nik.data.DataException;
import nik.data.ReservationFileRepository;
import nik.domain.GuestService;
import nik.domain.HostService;
import nik.domain.ReservationService;
import nik.domain.Result;
import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Controller {

    private final View view;
    private final ReservationService reservationService;
    private final HostService hostService;
    private final GuestService guestService;

    public Controller(View view, ReservationService reservationService, HostService hostService, GuestService guestService) {
        this.reservationService = reservationService;
        this.hostService = hostService;
        this.guestService = guestService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House! ");
        try {
            runAppLoop();
        } catch (DataException ex) {
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
        view.displayHeader("[Host Search]");

        String statePrefix = view.viewHostsByState();
        List<Host> hosts = hostService.getHostsFromState(statePrefix);
        view.displayHeader("Hosts for state " + statePrefix);
        view.printHosts(hosts);
        System.out.printf("%n");

        String email = view.getEmail();
        String id = hostService.getIdFromEmail(email);
        Host h = hostService.getHostFromEmail(email);
        List<Reservation> result = reservationService.findByHostId(id);
        view.printReservations(h, result);
    }
    //Books accommodations for a guest at a host.
    private void makeReservation() throws DataException {
        view.displayHeader("make a Reservation");
        Guest guestToFind = getGuest();
        if(guestService.findAll().contains(guestToFind)){
            System.out.println("Found guest! ");
            String email = view.getEmail();

            String iD = hostService.getIdFromEmail(email);
            Host h = hostService.getHostFromEmail(email);
            System.out.println("Upcoming reservations for host ");
            view.printReservations(h, reservationService.getFutureReservations(h));
            Reservation r = view.createReservation(h, guestToFind);
            System.out.printf("Reservation #: %s Start Date: %s - End Date: %s Guest ID: %s - Total: $%.2f%n",
                    r.getId(),
                    r.getStartDate(),
                    r.getEndDate(),
                    r.getGuestId(),
                    r.getTotal()
            );
            Result<Reservation> res = reservationService.createReservation(iD, r);
            if(res.isSuccess()){
                view.displayHeader("Created reservation" + guestService.getGuestsForHostFromReservation(h, r));//TODO make better
            }else {
                view.displayHeader(res.getErrorMessages().toString());
            }
        }else{
            System.out.println("Did Not Find Guest! ");
        }

    }

    private void editReservation() {
        view.displayHeader("Search for a guest to edit: ");
        Guest guest = guestService.getGuestByLastName(view.getLastName());
        view.displayHeader("Search for a host: ");
        Host host = hostService.getHostFromEmail(view.getEmail());


    }

    private void cancelReservation() {
        view.displayHeader("Cancel a Reservation");
    }


    private Guest getGuest() {
        String lastName = view.getLastName();
        return guestService.getGuestByLastName(lastName);
    }
}

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
        Host h = hostService.getHostFromEmail(email).getPayload();
        Result<List<Reservation>> result = reservationService.findByHostId(id);
        view.printReservations(h, result.getPayload());
    }
    private void makeReservation() throws DataException {
        view.displayHeader("make a Reservation");
        Guest guestToFind = getGuest();
        if (guestService.findAll().contains(guestToFind)) {
            System.out.println("Found guest! ");
            String email = view.getEmail();
            String iD = hostService.getIdFromEmail(email);
            Host h = hostService.getHostFromEmail(email).getPayload();
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
            if (res.isSuccess()) {
                view.displayHeader("Created reservation" + r);//TODO make better
            } else {
                view.displayHeader(res.getErrorMessages().toString());
            }
        } else {
            System.out.println("Did Not Find Guest! ");
        }

    }

    private void editReservation() throws DataException {
        view.displayHeader("Edit a reservation");
        //enter guest last name
        Guest guest = getGuest();
        if (guestService.findAll().contains(guest)) {
            view.displayHeader("Found guest!");
        } else {
            view.displayStatus(false,"Failed to find guest");
        }
        // enter email of host
        String email = view.getEmail();
        if (!hostService.getHostFromEmail(email).isSuccess()) {
            view.displayStatus(false, "Failed to find host");
        }else {
            Host h = hostService.getHostFromEmail(email).getPayload();
            Result<List<Reservation>>  result = reservationService.findByHostId(h.getiD());
            if (!result.isSuccess()){
                view.displayStatus(false, "Host does not have any current reservations to edit. ");
            }
            if(result.isSuccess()) {// Host has a reservation, now call view.editReservation to get reservation to
                //pass to service
                List<Reservation> hostReservation = reservationService
                        .findByHostId(hostService.getIdFromEmail(email)).getPayload();
                Reservation r = view.editReservation(hostReservation, guest, h);
                Result<Reservation> reservationResult = reservationService.editReservation(h, r);
                if (reservationResult.isSuccess()) {
                    view.displayHeader("Reservation" + r.getId() + "edited successfully");
                } else {
                    System.out.println(reservationResult.getErrorMessages());
                }
            }
        }
    }

    private void cancelReservation() throws DataException {
        view.displayHeader("Cancel a Reservation");
        Guest guest = getGuest();
        if (guestService.findAll().contains(guest)) {
            view.displayHeader("Found guest!");
        } else {
            view.displayStatus(false,"Failed to find guest");
        }
        String email = view.getEmail();
        if (!hostService.getHostFromEmail(email).isSuccess()) {
            view.displayStatus(false, "Failed to find host");
        }else {
            Host h = hostService.getHostFromEmail(email).getPayload();
            Result<List<Reservation>>  result = reservationService.findByHostId(h.getiD());
        if (result.isSuccess()) {
            List<Reservation> hostReservation = reservationService
                    .findByHostId(hostService.getIdFromEmail(email)).getPayload();

            Reservation r = view.cancel(hostReservation, guest, h);
            Result<List<Reservation>> res = reservationService.cancelReservation(h,r);
            if (res.isSuccess()){
                System.out.println("Reservation deleted successfully");
                view.printReservations(h, res.getPayload());
            }
            else {
                res.getErrorMessages().stream().forEach(System.out::println);
            }
        }

        }
        //TODO barebones service and data layer should be working
    }


    private Guest getGuest() {
        String lastName = view.getLastName();
        return guestService.getGuestByLastName(lastName);
    }

    private Host getHost() {
        String email = view.getEmail();
        return hostService.getHostFromEmail(email).getPayload();
    }
}

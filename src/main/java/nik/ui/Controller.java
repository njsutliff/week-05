package nik.ui;

import nik.data.DataException;
import nik.domain.GuestService;
import nik.domain.HostService;
import nik.domain.ReservationService;
import nik.domain.Result;
import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        Host h = getHost();
        if (h != null) {
            List<Reservation> result = reservationService.findByHostId(h.getiD());
            view.printReservations(h, result);
        } else {
            view.displayStatus(false, "Host not found. ");
        }

    }

    private void makeReservation() throws DataException {
        view.displayHeader("Add a Reservation");
        Guest guestToFind = getGuest();
        if (guestService.findAll().contains(guestToFind)) {
            System.out.println("Found guest! ");
            Host h = getHost();
            if (h != null) {
                String iD = h.getiD();
                System.out.println("Upcoming reservations for host ");
                List<Reservation> reservationList = reservationService.findByHostId(h.getiD());
                List<Reservation> future =
                        reservationList.stream().filter(reservation -> reservation.getStartDate()
                                .isAfter(LocalDate.now())).toList();
                view.viewReservations(h, future);
                Reservation r = view.createReservation(h, guestToFind);
                Result<Reservation> res = reservationService.createReservation(iD, r);
                if (res.isSuccess()) {
                    view.displayHeader("Created reservation");
                } else {
                    view.displayHeader(res.getErrorMessages().toString());
                }
            } else {
                view.displayStatus(false, "Did not find Host! ");
            }
        } else {
            view.displayStatus(false, "Did Not Find Guest! ");
        }
    }

    private void editReservation() throws DataException {
        view.displayHeader("Edit a reservation");
        Guest guest = getGuest();
        if (guest == null) {
            view.displayStatus(false, "Guest not found");
        } else {
            System.out.println("Found guest! ");
            Host h = getHost();
            if (h != null) {
                System.out.println("Found host! ");

                Reservation r = new Reservation();
                List<Reservation> reservationList = reservationService.findByHostId(h.getiD());
                List<Reservation> future =
                        reservationList.stream().filter(reservation -> reservation.getStartDate()
                                .isAfter(LocalDate.now())).toList();
                view.viewReservations(h, future);
                if (future.size() != 0) {
                    int rId = view.getReservationId();
                    System.out.println("Editing reservation " + rId);
                    r = view.editReservation(future, guest, h, rId);
                    if(r==null) {
                        view.displayStatus(false, "Invalid reservation.");
                    return;}
                    Result<Reservation> reservationResult = reservationService.editReservation(h, r);
                    if (reservationResult.isSuccess()) {
                        view.displayHeader("Reservation " + rId + " edited successfully");
                    } else {
                        view.displayStatus(false, reservationResult.getErrorMessages());
                    }
                }
            } else {
                view.displayStatus(false, "Host not found.");
            }
        }
    }

    private void cancelReservation() throws DataException {
        view.displayHeader("Cancel a Reservation");
        Guest guest = getGuest();//TODO working except for validation of guest using guestservice
        String guestLastName = "";
        if (guest == null) {
            view.displayStatus(false, "Guest not found");
            cancelReservation();
        }
        Host h = getHost();//TODO working except for validation of host using hostservice
        if (h == null) {
            view.displayStatus(false, "Host not found");
        } else {
            Reservation r = new Reservation();
            List<Reservation> reservations = reservationService.getReservationsForGuestAndHost(h, guest);
            if (reservations.size() == 0) {
                view.displayStatus(false, "host has no reservations!");
                return;
            }
            r.setHost(h);
            view.viewReservations(h, reservations);
            int rId = view.getReservationId();
            r = view.cancel(reservations, guest, h, rId);
            if(r==null) {
                view.displayStatus(false, "Invalid reservation.");
                return;}
            Result<Reservation> reservationResult = reservationService.cancelReservation(h, r, reservations);
            if (reservationResult.isSuccess()) {
                System.out.println("Deleted reservation");
            } else {
                System.out.println("Host has no reservations to cancel. ");
            }
        }
    }

    private Guest getGuest() {
        String lastName = view.getLastName();
        return guestService.getGuestByLastName(lastName).getPayload();
    }

    private Host getHost() {
        String email = view.getEmail();
        return hostService.getHostFromEmail(email).getPayload();
    }
}

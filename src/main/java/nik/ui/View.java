package nik.ui;

import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;
import nik.data.ReservationFileRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            if (!option.isHidden()) {
                io.printf("%s. %s%n", option.getValue(), option.getMessage());
            }
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }
    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    // display only
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    /**
     * Get a host email
     * @param
     */
    public String getEmail(){
        return io.readString("Enter a host email to view their reservations: ");
    }





    public void printReservations(Host h, List<Reservation> r) {
        if (r.size() == 0) {
            System.out.println("Host has no reservations!");
        }

        else {
            //r.sort(Comparator.comparing(reservation -> reservation.getEndDate()));
            io.printf("Host:  %s Email: %s %n", h.getLastName(), h.getEmail());
            io.printf("%s %s %n",h.getCity(), h.getState());

            for (Reservation reservation : r) {
                io.printf("Reservation #: %s Start Date: %s - End Date: %s Guest ID: %s - Total: $%.2f%n",
                        reservation.getId(),
                        reservation.getStartDate(),
                        reservation.getEndDate(),
                        reservation.getGuestId(),
                        reservation.getTotal()
                );

            }
        }
    }

    public String getLastName() {
       return io.readRequiredString("Enter a last name:");
    }

    public void printFutureReservations(Host h, List<Reservation> result) {

    }

    /**
     * Given a host and a guest, return a valid Reservation
     * @param h host
     * @param guestToFind guest to add reservation for
     * @return Reservation to pass to Controller -> service -> ReservationRepository.add
     */
    public Reservation createReservation(Host h, Guest guestToFind) {

    }
/**
    public Reservation makeReservation(Guest guestToFind) {
        io.readRequiredString("Enter");
    }
**/
/**
    public void displayReservations(List<Forage> forages) {
        if (forages == null || forages.isEmpty()) {
            io.println("No forages found.");
            return;
        }
        for (Forage forage : forages) {
            io.printf("%s %s - %s:%s - Value: $%.2f%n",
                    forage.getForager().getFirstName(),
                    forage.getForager().getLastName(),
                    forage.getItem().getName(),
                    forage.getItem().getCategory(),
                    forage.getValue()
            );
        }
    }
**/

}

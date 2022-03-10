package nik.ui;

import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;
import nik.data.ReservationFileRepository;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
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
        Reservation result = new Reservation();
        result.setHost(h);
        result.setId(String.valueOf(0));
        LocalDate start = io.readLocalDate("Enter a start date. ");
        result.setStartDate(start);
        LocalDate end = io.readLocalDate("Enter a end date. ");
        result.setEndDate(end);
        result.setGuestId(0); // will need to update this in data layer
        result.setTotal(calculateTotal(h, result));
        return  result;
    }

    /**
     * Calculates the total for the guest, given the guest's start and end date.
     * Also keep track of weekends to apply weekend rates.
     * @param host
     * @return
     */
    private BigDecimal calculateTotal(Host host, Reservation reservation) {
        Predicate<LocalDate> isWeekend = localDate -> localDate.getDayOfWeek()==DayOfWeek.SATURDAY ||
                localDate.getDayOfWeek()==DayOfWeek.SUNDAY;
        Predicate<LocalDate> isWeekday = localDate -> localDate.getDayOfWeek()==DayOfWeek.MONDAY ||
                localDate.getDayOfWeek()==DayOfWeek.TUESDAY||
                localDate.getDayOfWeek()==DayOfWeek.WEDNESDAY||
                localDate.getDayOfWeek()==DayOfWeek.THURSDAY||
                localDate.getDayOfWeek()==DayOfWeek.FRIDAY;
        //Size of these lists is the number of days stayed
        List<LocalDate> weekdays = reservation.startDate.datesUntil(reservation.endDate)
                .filter(isWeekday).toList();
        List<LocalDate> weekends = reservation.startDate.datesUntil(reservation.endDate).filter(isWeekend).toList();

        return  calculateWeekday(host, weekdays).add(calculateWeekend(host,weekends));
    }

    private BigDecimal calculateWeekday(Host host, List<LocalDate> weekdays) {
        return host.standardRate.multiply(BigDecimal.valueOf(weekdays.size()));
    }


    private BigDecimal calculateWeekend(Host host, List<LocalDate> weekend) {
        return host.weekendRate.multiply(BigDecimal.valueOf(weekend.size()));
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

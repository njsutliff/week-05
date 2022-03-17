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
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

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
     *
     * @param
     */
    public String getEmail() {
        return io.readString("Enter a host email to view their reservations: ");
    }

    public String viewHostsByState() {
        return io.readRequiredString("enter a host state: ");
    }

    //id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate
    public void printHosts(List<Host> h) {
        h.sort(Comparator.comparing(Host::getStandardRate).reversed());

        for (Host host : h) {
            io.printf(
                    "%nID #: %s Last Name: %s %nEmail: %s Phone: %s%nAddress: %s-%s, " +
                            "%s Postal Code: %s%nstandard rate: $%.2f weekend rate: $%.2f%n",
                    host.getiD(),
                    host.getLastName(),
                    host.getEmail(),
                    host.getPhone(),
                    host.getAddress(),
                    host.getCity(),
                    host.getState(),
                    host.getPostalCode(),
                    host.getStandardRate(),
                    host.getWeekendRate());
        }
        //h.stream().forEach(System.out::println);
    }

    /**
     * Prints reservations, sorted by most recent end date
     * (as that would be useful to the administrator)
     *
     * @param h host passed in
     * @param r list of reservations to sort and display
     */
    public void printReservations(Host h, List<Reservation> r) {
        if (r.size() == 0) {
            System.out.println("Host has no reservations!");
        } else {
            //r.sort(Comparator.comparing(Reservation::getEndDate).reversed());//TODO sorting error
            io.printf("Host:  %s Email: %s %n", h.getLastName(), h.getEmail());
            io.printf("%s %s %n", h.getCity(), h.getState());

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

    /**
     * Given a host and a guest, return a valid Reservation
     *
     * @param h           host
     * @param guestToFind guest to add reservation for
     * @return Reservation to pass to Controller -> service -> ReservationRepository.add
     */
    public Reservation createReservation(Host h, Guest guestToFind) {
        Reservation result = new Reservation();
        result.setHost(h);
        result.setId(String.valueOf(0));
        LocalDate start = io.readLocalDate("Enter a start date. ", LocalDate.EPOCH);
        result.setStartDate(start);
        LocalDate end = io.readLocalDate("Enter a end date. ", LocalDate.EPOCH);
        result.setEndDate(end);
        result.setGuestId(Integer.parseInt(guestToFind.getGuestId())); // will need to update this in data layer
        if (!result.getEndDate().isBefore(result.getStartDate())) {
            BigDecimal total = calculateTotal(h, result);
            result.setTotal(total);
            return displaySummary(result, total);
        } else {
            displayStatus(false, "Start date after end date");
            return result;
        }
    }

    private Reservation displaySummary(Reservation reservation, BigDecimal total) {
        boolean done = false;
        do {
            displayHeader("Summary of details");
            io.printf("Reservation #: %s Start Date: %s - End Date: %s Guest ID: %s - Total: $%.2f%n",
                    reservation.getId(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getGuestId(),
                    total
            );
            if (io.readRequiredString("Enter 'yes' to confirm").equalsIgnoreCase("yes")) {
                done = true;
            }
        } while (!done);
        return reservation;
    }

    /**
     * Calculates the total for the guest, given the guest's start and end date.
     * Also keep track of weekends to apply weekend rates.
     *
     * @param host
     * @return
     */
    private BigDecimal calculateTotal(Host host, Reservation reservation) {
        Predicate<LocalDate> isWeekend = localDate -> localDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                localDate.getDayOfWeek() == DayOfWeek.SUNDAY;
        Predicate<LocalDate> isWeekday = localDate -> localDate.getDayOfWeek() == DayOfWeek.MONDAY ||
                localDate.getDayOfWeek() == DayOfWeek.TUESDAY ||
                localDate.getDayOfWeek() == DayOfWeek.WEDNESDAY ||
                localDate.getDayOfWeek() == DayOfWeek.THURSDAY ||
                localDate.getDayOfWeek() == DayOfWeek.FRIDAY;
        //Size of these lists is the number of days stayed
        List<LocalDate> weekdays = reservation.startDate.datesUntil(reservation.endDate)
                .filter(isWeekday).toList();
        List<LocalDate> weekends = reservation.startDate.datesUntil(reservation.endDate).filter(isWeekend).toList();

        return calculateWeekday(host, weekdays).add(calculateWeekend(host, weekends));
    }

    private BigDecimal calculateWeekday(Host host, List<LocalDate> weekdays) {
        return host.standardRate.multiply(BigDecimal.valueOf(weekdays.size()));
    }


    private BigDecimal calculateWeekend(Host host, List<LocalDate> weekend) {
        return host.weekendRate.multiply(BigDecimal.valueOf(weekend.size()));
    }
    public Reservation cancel(List<Reservation> hostReservation, Guest guest, Host host) {
        List<Reservation> editList = hostReservation.stream()
                .filter(reservation -> reservation.getGuestId() == Integer.parseInt(guest.getGuestId())).toList();
        printReservations(host, editList);
        Reservation r = editList.get(0);
        io.readRequiredString("Do you want to delete Reservation " + r.getId());
        List<Reservation> listToPrint = new ArrayList<>();
        listToPrint.add(r);
        printReservations(host, listToPrint);
        r.setGuestId(Integer.parseInt(guest.getGuestId()));
        displaySummary(r, r.getTotal());
        return r;
    }
    /**
     * Find a reservation.
     * Start and end date can be edited. No other data can be edited.
     * Recalculate the total, display a summary, and ask the user to confirm.
     *
     * @param hostReservationsAlreadyExisting list of all reservations to search for
     * @param guest                           guest to find.
     */
    public Reservation editReservation(List<Reservation> hostReservationsAlreadyExisting, Guest guest, Host host) {
        List<Reservation> editList = hostReservationsAlreadyExisting.stream()
                .filter(reservation -> reservation.getGuestId() == Integer.parseInt(guest.getGuestId())).toList();
        printReservations(host, editList);
        Reservation r = editList.get(0);

        LocalDate newStart = io.readLocalDate("Enter a new start date: ", r.startDate);
        if (newStart.equals(r.getStartDate())){
            io.readEnter("Enter to keep the previous value");
        }
        LocalDate newEnd = io.readLocalDate("Enter a new end date: ", r.endDate);
        if (newEnd.equals(r.getEndDate())){
            io.readEnter("Enter to keep the previous value");
        }
        r.setStartDate(newStart);
        r.setEndDate(newEnd);
       return displayEdit(r);
    }
    private  Reservation displayEdit( Reservation reservation){
        boolean done = false;
        do {
            displayHeader("Summary of details");
            io.printf("Reservation #: %s Start Date: %s - End Date: %s Guest ID: %s%n",
                    reservation.getId(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getGuestId()
            );
            if (io.readRequiredString("Enter 'yes' to confirm").equalsIgnoreCase("yes")) {
                done = true;
            }
        } while (!done);
        return reservation;
    }
/**
    public Reservation cancelReservation(Host h, Guest guest, Reservation r) {
        io.readRequiredString("Do you want to delete Reservation " + r.getGuestId());
        List<Reservation> listToPrint = new ArrayList<>();
        listToPrint.add(r);
        printReservations(h, listToPrint);
        r.setGuestId(Integer.parseInt(guest.getGuestId()));
        displaySummary(r, r.getTotal());
        System.out.println(r.getStartDate());
        System.out.println(r.getStartDate());

        return r;
    }
**/

}



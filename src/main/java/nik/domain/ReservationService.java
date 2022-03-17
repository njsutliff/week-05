package nik.domain;

import nik.data.DataException;
import nik.data.ReservationFileRepository;
import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {
    private final ReservationFileRepository reservationRepository;

    public ReservationService(ReservationFileRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Result<List<Reservation>> findByHostId(String Id) {
        List<Reservation> resultList = reservationRepository.findByHostId(Id);
        Result<List<Reservation>> result = new Result<>();
        if (resultList.size() == 0) {
            result.addErrorMessage("Host has no current reservations");
            return result;
        }
        if (!result.isSuccess()) {
            result.addErrorMessage("Host not found");
            return result;
        }
        result.setPayload(resultList);
        return result;
    }

    public List<Reservation> getFutureReservations(Host h) {
        return reservationRepository.getFutureReservations(h);
    }

    public Result<Reservation> createReservation(String iD, Reservation r) throws DataException {
        Result<Reservation> result = validate(iD, r);
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(reservationRepository.createReservation(iD, r));

        return result;
    }

    public Result<Reservation> editReservation(Host h, Reservation r) throws DataException {
        Result<Reservation> result = new Result<>();
        if (reservationRepository.findByHostId(h.getiD()).contains(r)) {
            result.addErrorMessage("Cannot edit reservation");
        }
        result.setPayload(reservationRepository.editReservation(h, r));
        return result;
    }

    public Result<List<Reservation>> cancelReservation(Host h, Reservation r) throws DataException {
        Result<List<Reservation>> result = new Result<>();
        if (reservationRepository.findByHostId(h.getiD()).contains(r)) {
            result.setPayload(reservationRepository.cancelReservation(h, r));
        }
        return result;

    }

    public Result<Reservation> validate(String iD, Reservation r) {
        Result<Reservation> result = new Result<>();
        validateNulls(r);

        if (r == null) {
            result.addErrorMessage("Reservation is null. ");
            return result;
        }
        validateFields(iD, result, r);
        return result;
    }

    private void validateNulls(Reservation r) {
        Result<Reservation> result = new Result<>();
        if (r == null) {
            result.addErrorMessage("Nothing to save. ");
        }
        if (r.getId() == null) {
            result.addErrorMessage("Id is required. ");
        }
        if (r.getStartDate() == null) {
            result.addErrorMessage("Start date is required. ");
        }
        if (r.getEndDate() == null) {
            result.addErrorMessage("End date is required. ");
        }
        if ((r.getGuestId() < 0) || r.getGuestId() >= 1001) {
            result.addErrorMessage("Not a current guest. ");
        }
        if (r.total == null) {
            result.addErrorMessage("Total amount required. ");
        }
    }

    private void validateFields(String iD, Result<Reservation> result, Reservation r) {
        List<Reservation> reservationsWithId = reservationRepository.findByHostId(iD);
        LocalDate startDate = r.getStartDate();
        LocalDate endDate = r.getEndDate();
        if (startDate.isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservations must be made for the future. ");
        }
        if (startDate.isAfter(endDate)) {
            result.addErrorMessage("Reservation start date cannot be after the end date. ");
            return;
        }

        for (Reservation res : reservationsWithId) {
            LocalDate existingStartDate = res.getStartDate();
            LocalDate existingEndDate = res.getEndDate();
            if (startDate.isEqual(existingStartDate)
                    || endDate.isEqual(existingEndDate)) {
                result.addErrorMessage
                        ("Reservation cannot begin or end on same day,host needs time to checkout");
                return;
            }
            if (startDate.isBefore(existingStartDate) && endDate.isAfter(existingStartDate)) {
                result.addErrorMessage("Reservation is during an existing reservation. ");
                return;
            }

            if (startDate.isAfter(existingStartDate) && startDate.isBefore(existingEndDate)) {
                result.addErrorMessage("Reservation is during an existing reservation. ");
                return;
            }

        }
    }


}

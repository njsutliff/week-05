package nik.models;

import java.util.Objects;

public class Guest {
    public String guestId;
    public String firstName;



    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String lastName;
    public String email;

    public String getGuestId() {
        return guestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getState() {
        return state;
    }

    public String phone;
    public String state;
    public Guest(){

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(guestId, guest.guestId) && Objects.equals(firstName, guest.firstName)
                &&Objects.equals(lastName, guest.lastName) && Objects.equals(email, guest.email)
                && Objects.equals(phone, guest.phone) && Objects.equals(state, guest.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId, firstName, lastName, email, phone, state);
    }
}

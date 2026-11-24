package org.example.Tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;
import org.example.Models.Booking;
import org.example.Utils.Constants;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PutBooking implements Task {

    private final Booking booking;

    public PutBooking(Booking booking) {
        this.booking = booking;
    }

    public static Performable withData(Booking booking) {
        return instrumented(PutBooking.class, booking);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        String bookingId = actor.recall("bookingId");
        String token = actor.recall("token");
        actor.attemptsTo(
                Put.to(Constants.BOOKING_ENDPOINT + "/" + bookingId)
                        .with(requestSpecification -> requestSpecification
                                .contentType(ContentType.JSON)
                                .header("Accept", "application/json")
                                .cookie("token", token).body(booking))
        );
    }
}
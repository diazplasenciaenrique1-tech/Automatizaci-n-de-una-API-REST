package org.example.Tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.example.Models.Booking;
import org.example.Utils.Constants;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PostBooking implements Task {

    private final Booking booking;

    public PostBooking(Booking booking) {
        this.booking = booking;
    }

    public static Performable fromData(Booking booking) {
        return instrumented(PostBooking.class, booking);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to(Constants.BOOKING_ENDPOINT)
                        .with(requestSpecification -> requestSpecification
                                .contentType(ContentType.JSON).header("Accept", "application/json")
                                .body(booking).log().all())
        );

        SerenityRest.lastResponse().body().prettyPrint();

        if (SerenityRest.lastResponse().statusCode() == 200) {
            actor.remember("bookingId", SerenityRest.lastResponse().path("bookingid").toString());
            String bookingId = actor.recall("bookingId");
            System.out.println("Booking ID: " + bookingId);
        }
    }
}
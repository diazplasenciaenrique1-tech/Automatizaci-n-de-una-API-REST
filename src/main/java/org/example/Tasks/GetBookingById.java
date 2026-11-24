package org.example.Tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import org.example.Utils.Constants;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetBookingById implements Task {

    private final String bookingId;

    public GetBookingById(String bookingId) {
        this.bookingId = bookingId;
    }

    public static Performable fromId(String bookingId) {
        return instrumented(GetBookingById.class, bookingId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource(Constants.BOOKING_ENDPOINT + "/" + bookingId)
                        .with(requestSpecification -> requestSpecification
                                .contentType(ContentType.JSON)
                                .header("Accept", "application/json")
                                .log().all())
        );
    }
}
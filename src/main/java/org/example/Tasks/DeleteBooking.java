package org.example.Tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import org.example.Utils.Constants;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteBooking implements Task {

    public static Performable fromBooking() {
        return instrumented(DeleteBooking.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String bookingId = actor.recall("bookingId");
        String token = actor.recall("token");
        actor.attemptsTo(Delete.from(Constants.BOOKING_ENDPOINT + "/" + bookingId).with(requestSpecification -> requestSpecification.cookie("token", token))
        );
    }
}
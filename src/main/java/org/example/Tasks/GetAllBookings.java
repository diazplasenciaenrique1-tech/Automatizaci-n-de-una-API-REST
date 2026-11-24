package org.example.Tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import org.example.Utils.Constants;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetAllBookings implements Task {

    public static Performable fromEndpoint() {
        return instrumented(GetAllBookings.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource(Constants.BOOKING_ENDPOINT)
                        .with(requestSpecification -> requestSpecification
                                .contentType(ContentType.JSON)
                                .header("Accept", "application/json").log().all())
        );
    }
}
package org.example.StepDefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.example.Models.Booking;
import org.example.Questions.BookingDetails;
import org.example.Questions.ResponseCode;
import org.example.Tasks.GetAllBookings;
import org.example.Tasks.GetBookingById;
import org.example.Tasks.PostBooking;
import org.example.Tasks.PostToken;
import org.example.Tasks.PutBooking;
import org.example.Tasks.DeleteBooking;
import org.example.Utils.Constants;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BookingStepDefinitions {

    private Actor currentActor;

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("el {actor} establece el endpoint de booking")
    public void establecerEndpoint(Actor actor) {
        currentActor = actor;
        actor.whoCan(CallAnApi.at(Constants.BASE_URL));
    }

    @When("el {actor} consulta todas las reservas")
    public void consultarTodasLasReservas(Actor actor) {
        actor.attemptsTo(
                GetAllBookings.fromEndpoint()
        );
    }

    @When("el {actor} crea una reserva con datos validos")
    public void crearReserva(Actor actor) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        Booking booking;

        try (InputStream input = getClass()
                .getResourceAsStream(Constants.BOOKING_DATA)) {

            if (input == null) {
                throw new IOException("No se encontro booking.json");
            }

            booking = mapper.readValue(input, Booking.class);
        }

        actor.remember("bookingData", booking);

        actor.attemptsTo(
                PostBooking.fromData(booking)
        );

        String bookingId = SerenityRest.lastResponse()
                .jsonPath()
                .getString("bookingid");

        if (bookingId == null) {
            throw new IllegalStateException(
                    "No se recibio el ID de la reserva creada"
            );
        }

        actor.remember("bookingId", bookingId);

        actor.should(
                seeThat("Reserva creada correctamente",
                        ResponseCode.getStatus(),
                        equalTo(200)),
                seeThat("Identificador generado",
                        BookingDetails.getField("bookingid"),
                        notNullValue())
        );
    }

    @Given("el {actor} obtiene un token de autenticacion")
    public void obtenerToken(Actor actor) {
        actor.attemptsTo(
                PostToken.generateToken()
        );
    }

    @When("el {actor} actualiza los datos de la reserva")
    public void actualizarReserva(Actor actor) {

        Booking booking = actor.recall("bookingData");

        booking.setFirstname("Carlos");
        booking.setLastname("Ramirez");
        booking.setTotalprice(1500);
        booking.setAdditionalneeds("Desayuno y almuerzo");

        actor.remember("updatedBooking", booking);

        actor.attemptsTo(
                PutBooking.withData(booking)
        );
    }

    @When("el {actor} elimina la reserva")
    public void eliminarReserva(Actor actor) {
        actor.attemptsTo(
                DeleteBooking.fromBooking()
        );
    }

    @When("el {actor} consulta una reserva inexistente")
    public void consultarReservaInexistente(Actor actor) {
        actor.attemptsTo(
                GetBookingById.fromId("-1")
        );
    }

    @Then("la respuesta debe ser {int}")
    public void validarCodigoRespuesta(int responseCode) {

        theActorInTheSpotlight().should(
                seeThat("El codigo de respuesta",
                        ResponseCode.getStatus(),
                        equalTo(responseCode))
        );
    }

    @Then("la respuesta debe mostrar una lista de reservas")
    public void validarListaReservas() {

        SerenityRest.lastResponse().then()
                .contentType("application/json")
                .body("$", instanceOf(List.class))
                .body("bookingid", everyItem(instanceOf(Integer.class)));
    }

    @Then("debe mostrar el ID de la reserva creada")
    public void validarIdentificadorReserva() {

        Actor actor = theActorInTheSpotlight();

        Booking booking = actor.recall("bookingData");

        actor.should(
                seeThat("ID de la reserva",
                        BookingDetails.getField("bookingid"),
                        instanceOf(Integer.class)),

                seeThat("Nombre registrado",
                        BookingDetails.getField("booking.firstname"),
                        equalTo(booking.getFirstname())),

                seeThat("Apellido registrado",
                        BookingDetails.getField("booking.lastname"),
                        equalTo(booking.getLastname())),

                seeThat("Precio registrado",
                        BookingDetails.getField("booking.totalprice"),
                        equalTo(booking.getTotalprice()))
        );

        SerenityRest.lastResponse().then()
                .contentType("application/json");
    }

    @Then("debe mostrar los datos actualizados")
    public void validarDatosActualizados() {

        Actor actor = theActorInTheSpotlight();

        Booking booking = actor.recall("updatedBooking");

        actor.should(
                seeThat("Nombre actualizado",
                        BookingDetails.getField("firstname"),
                        equalTo(booking.getFirstname())),

                seeThat("Apellido actualizado",
                        BookingDetails.getField("lastname"),
                        equalTo(booking.getLastname())),

                seeThat("Precio actualizado",
                        BookingDetails.getField("totalprice"),
                        equalTo(booking.getTotalprice())),

                seeThat("Servicios adicionales actualizados",
                        BookingDetails.getField("additionalneeds"),
                        equalTo(booking.getAdditionalneeds()))
        );
    }

    @Then("la reserva creada debe existir")
    public void validarPersistencia() {

        Actor actor = theActorInTheSpotlight();

        String bookingId = actor.recall("bookingId");
        Booking booking = actor.recall("bookingData");

        actor.attemptsTo(
                GetBookingById.fromId(bookingId)
        );

        actor.should(
                seeThat("Reserva encontrada",
                        ResponseCode.getStatus(),
                        equalTo(200)),

                seeThat("Nombre almacenado",
                        BookingDetails.getField("firstname"),
                        equalTo(booking.getFirstname())),

                seeThat("Precio almacenado",
                        BookingDetails.getField("totalprice"),
                        equalTo(booking.getTotalprice()))
        );
    }

    @Then("los cambios deben guardarse correctamente")
    public void validarPersistenciaActualizacion() {

        Actor actor = theActorInTheSpotlight();

        String bookingId = actor.recall("bookingId");

        actor.attemptsTo(
                GetBookingById.fromId(bookingId)
        );

        validarCodigoRespuesta(200);
        validarDatosActualizados();
    }

    @Then("la reserva eliminada ya no debe existir")
    public void validarEliminacion() {

        Actor actor = theActorInTheSpotlight();

        String bookingId = actor.recall("bookingId");

        actor.attemptsTo(
                GetBookingById.fromId(bookingId)
        );

        actor.should(
                seeThat("Reserva eliminada correctamente",
                        ResponseCode.getStatus(),
                        equalTo(404))
        );

        actor.forget("bookingId");
    }

    @Then("los datos deben tener el formato correcto")
    public void validarContratoJSON() {

        SerenityRest.lastResponse().then()
                .contentType("application/json")
                .body(matchesJsonSchemaInClasspath(
                        "schemas/booking-schema.json"
                ));
    }

    @After
    public void limpiarDatos() {

        try {
            if (currentActor != null) {

                String bookingId = currentActor.recall("bookingId");

                if (bookingId != null) {

                    currentActor.attemptsTo(
                            PostToken.generateToken(),
                            DeleteBooking.fromBooking()
                    );

                    SerenityRest.lastResponse().then()
                            .statusCode(anyOf(equalTo(201), equalTo(404)));
                }
            }
        } finally {
            OnStage.drawTheCurtain();
        }
    }
}

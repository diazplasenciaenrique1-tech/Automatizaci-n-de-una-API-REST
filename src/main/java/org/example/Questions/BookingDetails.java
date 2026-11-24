package org.example.Questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class BookingDetails implements Question<Object> {

    private final String field;

    public BookingDetails(String field) {
        this.field = field;
    }

    public static BookingDetails getField(String field) {
        return new BookingDetails(field);
    }

    @Override
    public Object answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().get(field);
    }
}

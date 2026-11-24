package org.example.Tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.example.Utils.Constants;
import java.util.HashMap;
import java.util.Map;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PostToken implements Task {

    public static Performable generateToken() {
        return instrumented(PostToken.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", Constants.USERNAME);
        credentials.put("password", Constants.PASSWORD);

        actor.attemptsTo(Post.to(Constants.AUTH_ENDPOINT)
                        .with(requestSpecification -> requestSpecification
                                .contentType(ContentType.JSON).header("Accept", "application/json").body(credentials)));

        SerenityRest.lastResponse().then().statusCode(200);
        String token = SerenityRest.lastResponse().jsonPath().getString("token");

        if (token == null || token.isEmpty()) {
            throw new AssertionError("No se genero el token de autenticacion");
        }
        actor.remember("token", token);
        System.out.println("Token generado correctamente");
    }
}
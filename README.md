# Proyecto: Automatización de una API REST

## 1. Descripción del proyecto

Este proyecto consiste en automatizar algunas operaciones de una API REST de reservas. Lo desarrollé como parte del proyecto integrador de la Escuela de Testing, aplicando lo aprendido durante el curso sobre pruebas de servicios, automatización y validación de respuestas.

La API utilizada es **Restful Booker**, que permite realizar operaciones relacionadas con la gestión de reservas.

El objetivo es comprobar que los servicios respondan correctamente, que los datos se puedan crear y actualizar, y que las reservas se eliminen cuando corresponda.

## 2. Objetivos

- Automatizar pruebas de servicios REST.
- Consultar, crear, actualizar y eliminar reservas.
- Validar los códigos de respuesta HTTP.
- Comprobar que los datos enviados se registren correctamente.
- Validar la estructura de las respuestas mediante un esquema JSON.
- Generar un reporte de ejecución con Serenity.

## 3. Herramientas y tecnologías utilizadas

| Herramienta o tecnología | Uso en el proyecto |
|---|---|
| Java 17 | Lenguaje utilizado para desarrollar la automatización. |
| Maven | Administración de dependencias y ejecución del proyecto. |
| IntelliJ IDEA | Entorno de desarrollo. |
| Serenity BDD | Registro de las pruebas y generación del reporte. |
| Cucumber | Creación de escenarios en lenguaje Gherkin. |
| Screenplay | Organización de las acciones y responsabilidades de las pruebas. |
| REST Assured | Trabajo con los servicios REST y sus respuestas. |
| JUnit | Ejecución de las pruebas mediante RunnerTest. |
| OBS Studio | Grabación de la evidencia de ejecución. |

## 4. API utilizada

**Nombre:** Restful Booker

**URL base:** https://restful-booker.herokuapp.com

La API permite trabajar con reservas mediante diferentes operaciones HTTP. Para las pruebas se utilizan los servicios de consulta, creación, actualización y eliminación, además de la autenticación para las operaciones que la requieren.

## 5. Escenarios automatizados

El proyecto contiene cinco escenarios: cuatro positivos y uno negativo.

| Código | Escenario | Método | Resultado esperado |
|---|---|---|---|
| CP01 | Consultar todas las reservas | GET | 200 |
| CP02 | Crear una reserva correctamente | POST | 200 |
| CP03 | Actualizar una reserva | PUT | 200 |
| CP04 | Eliminar una reserva | DELETE | 201 |
| CN01 | Consultar una reserva que no existe | GET | 404 |

### 5.1. Pruebas positivas

- **CP01 – Consultar reservas:** comprueba que el servicio permita consultar la lista de reservas y que la respuesta contenga una lista de identificadores.
- **CP02 – Crear reserva:** envía los datos de una reserva válida, comprueba el código de respuesta, obtiene el identificador generado y valida la estructura JSON. También verifica que la reserva creada se pueda consultar.
- **CP03 – Actualizar reserva:** crea una reserva, obtiene el token de autenticación y actualiza sus datos. Después comprueba que los cambios se hayan guardado.
- **CP04 – Eliminar reserva:** crea una reserva, obtiene el token y realiza la eliminación. Finalmente verifica que la reserva ya no se encuentre disponible.

### 5.2. Prueba negativa

- **CN01 – Consultar una reserva inexistente:** intenta consultar un identificador que no existe y comprueba que la API responda con el código HTTP 404.

## 6. Estructura del proyecto

La organización del proyecto se realizó separando los modelos, las consultas, las tareas y los pasos de Cucumber para mantener el código ordenado y facilitar su mantenimiento.

```text
PracticaAtutomatizacionApi/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/
│   │   │       ├── Models/
│   │   │       │   └── Booking.java
│   │   │       ├── Questions/
│   │   │       │   ├── BookingDetails.java
│   │   │       │   └── ResponseCode.java
│   │   │       ├── Tasks/
│   │   │       │   ├── DeleteBooking.java
│   │   │       │   ├── GetAllBookings.java
│   │   │       │   ├── GetBookingById.java
│   │   │       │   ├── PostBooking.java
│   │   │       │   ├── PostToken.java
│   │   │       │   └── PutBooking.java
│   │   │       ├── Utils/
│   │   │       │   └── Constants.java
│   │   │       └── Main.java
│   │   └── resources/
│   │
│   └── test/
│       ├── java/
│       │   └── org/example/
│       │       ├── StepDefinitions/
│       │       │   ├── BookingStepDefinitions.java
│       │       │   └── ParameterDefinition.java
│       │       └── RunnerTest.java
│       │
│       └── resources/
│           ├── data/
│           │   └── booking.json
│           ├── features/
│           │   └── Book.feature
│           ├── schemas/
│           │   └── booking-schema.json
│           └── serenity.conf
│
├── evidencias/
│   └── video-ejecucion.mp4
│
├── reporte-serenity/
│   └── index.html
│
├── .gitignore
├── pom.xml

```

## 7. Datos y validaciones

Para realizar las pruebas se utiliza el archivo `booking.json`, donde se encuentran los datos de la reserva que se envían a la API.

También se utiliza `booking-schema.json` para comprobar que la respuesta de creación o actualización cumpla con la estructura JSON definida para el proyecto.

Las principales validaciones realizadas son:

- Código de respuesta HTTP.
- Tipo de contenido de la respuesta.
- Identificador generado para la reserva.
- Datos enviados y datos devueltos por el servicio.
- Persistencia de la información después de crear o actualizar.
- Comprobación de que la reserva eliminada ya no exista.
- Estructura de la respuesta mediante JSON Schema.


## Conclusión

Con este proyecto pude aplicar los conocimientos trabajados durante el curso para automatizar servicios REST. Se organizaron las pruebas utilizando Cucumber y Screenplay, separando las acciones, los modelos y las validaciones.

También se realizaron pruebas positivas y negativas para comprobar diferentes respuestas de la API. Finalmente, se utilizó Serenity para generar un reporte que permite revisar los resultados de la ejecución.

Este trabajo me permitió practicar la automatización de servicios, el uso de Maven y la organización de un proyecto de pruebas para poder mantenerlo y ejecutarlo nuevamente.



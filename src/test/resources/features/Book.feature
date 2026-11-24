@Book
Feature: Gestion de reservas con la API Restful Booker

  Como usuario del sistema
  Quiero consultar, crear, actualizar y eliminar reservas
  Para comprobar que las operaciones funcionan correctamente

  Background:
    Given el actor establece el endpoint de booking

  @positivo @GET @CP01
  Scenario: Consultar todas las reservas
    When el actor consulta todas las reservas
    Then la respuesta debe ser 200
    And la respuesta debe mostrar una lista de reservas

  @positivo @POST @CP02
  Scenario: Crear una nueva reserva
    When el actor crea una reserva con datos validos
    Then la respuesta debe ser 200
    And debe mostrar el ID de la reserva creada
    And los datos deben tener el formato correcto
    And la reserva creada debe existir

  @positivo @PUT @CP03
  Scenario: Actualizar una reserva
    Given el actor crea una reserva con datos validos
    And el actor obtiene un token de autenticacion
    When el actor actualiza los datos de la reserva
    Then la respuesta debe ser 200
    And debe mostrar los datos actualizados
    And los datos deben tener el formato correcto
    And los cambios deben guardarse correctamente

  @positivo @DELETE @CP04
  Scenario: Eliminar una reserva
    Given el actor crea una reserva con datos validos
    And el actor obtiene un token de autenticacion
    When el actor elimina la reserva
    Then la respuesta debe ser 201
    And la reserva eliminada ya no debe existir

  @negativo @GET @CN01
  Scenario: Buscar una reserva que no existe
    When el actor consulta una reserva inexistente
    Then la respuesta debe ser 404

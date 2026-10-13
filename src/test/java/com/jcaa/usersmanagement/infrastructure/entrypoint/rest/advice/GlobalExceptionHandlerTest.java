package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.exception.DomainException;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ApiErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  private static void assertResponse(
      final ApiErrorResponse actual, final int status, final String message) {
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(new ApiErrorResponse(status, message));
  }

  @Test
  void handleUserNotFound_returns404() {
    final UserNotFoundException exception = mock(UserNotFoundException.class);
    when(exception.getMessage()).thenReturn("no existe");

    assertResponse(handler.handleUserNotFound(exception), 404, "no existe");
  }

  @Test
  void handleUserAlreadyExists_returns409() {
    final UserAlreadyExistsException exception = mock(UserAlreadyExistsException.class);
    when(exception.getMessage()).thenReturn("ya existe");

    assertResponse(handler.handleUserAlreadyExists(exception), 409, "ya existe");
  }

  @Test
  void handleInvalidCredentials_returns401() {
    final InvalidCredentialsException exception = mock(InvalidCredentialsException.class);
    when(exception.getMessage()).thenReturn("credenciales invalidas");

    assertResponse(handler.handleInvalidCredentials(exception), 401, "credenciales invalidas");
  }

  @Test
  @SuppressWarnings("unchecked")
  void handleConstraintViolation_returns400WithPathAndMessage() {
    final ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
    final Path path = mock(Path.class);
    when(path.toString()).thenReturn("name");
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn("no debe estar vacio");
    final ConstraintViolationException exception =
        new ConstraintViolationException(Set.of(violation));

    assertResponse(
        handler.handleConstraintViolation(exception), 400, "name: no debe estar vacio");
  }

  @Test
  void handleMethodArgumentNotValid_returns400WithFieldAndMessage() throws Exception {
    final BeanPropertyBindingResult binding = new BeanPropertyBindingResult(new Object(), "request");
    binding.addError(new FieldError("request", "email", "formato invalido"));
    final MethodParameter parameter =
        new MethodParameter(Object.class.getDeclaredMethod("toString"), -1);
    final MethodArgumentNotValidException exception =
        new MethodArgumentNotValidException(parameter, binding);

    assertResponse(handler.handleMethodArgumentNotValid(exception), 400, "email: formato invalido");
  }

  @Test
  void handleDomain_returns400() {
    final DomainException exception = mock(DomainException.class);
    when(exception.getMessage()).thenReturn("regla de negocio");

    assertResponse(handler.handleDomain(exception), 400, "regla de negocio");
  }

  @Test
  void handlePersistence_returns500() {
    assertResponse(
        handler.handlePersistence(mock(PersistenceException.class)), 500, "Error de persistencia.");
  }

  @Test
  void handleGeneral_returns500() {
    assertResponse(
        handler.handleGeneral(new Exception("boom")), 500, "Error interno del servidor.");
  }
}
package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class CreateUserHandlerTest {

  private static final String INPUT = "u1\nAna\nana@test.com\nSecret123\nMEMBER\n";

  private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private final PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
  private final UserController controller = mock(UserController.class);
  private final UserResponsePrinter printer = mock(UserResponsePrinter.class);

  private CreateUserHandler handlerWith(final String input) {
    final ConsoleIO console = new ConsoleIO(new Scanner(input), out);
    return new CreateUserHandler(controller, console, printer);
  }

  private String output() {
    return buffer.toString(StandardCharsets.UTF_8);
  }

  @Test
  void handle_whenCreationSucceeds_printsSuccessAndUser() {
    final UserResponse created = mock(UserResponse.class);
    when(controller.createUser(any(CreateUserRequest.class))).thenReturn(created);

    handlerWith(INPUT).handle();

    assertThat(output()).contains("User created successfully.");
    verify(printer).print(created);
  }

  @Test
  void handle_whenUserAlreadyExists_printsError() {
    final UserAlreadyExistsException exception = mock(UserAlreadyExistsException.class);
    when(exception.getMessage()).thenReturn("ya existe");
    when(controller.createUser(any(CreateUserRequest.class))).thenThrow(exception);

    handlerWith(INPUT).handle();

    assertThat(output()).contains("Error: ya existe");
  }
}
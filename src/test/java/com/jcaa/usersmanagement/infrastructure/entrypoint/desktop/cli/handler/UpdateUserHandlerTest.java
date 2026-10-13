package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class UpdateUserHandlerTest {

  private static final String INPUT_WITH_PASSWORD =
      "u1\nAna\nana@test.com\nNewSecret123\nMEMBER\nACTIVE\n";
  private static final String INPUT_WITHOUT_PASSWORD =
      "u1\nAna\nana@test.com\n\nMEMBER\nACTIVE\n";

  private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private final PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
  private final UserController controller = mock(UserController.class);
  private final UserResponsePrinter printer = mock(UserResponsePrinter.class);

  private UpdateUserHandler handlerWith(final String input) {
    final ConsoleIO console = new ConsoleIO(new Scanner(input), out);
    return new UpdateUserHandler(controller, console, printer);
  }

  private String output() {
    return buffer.toString(StandardCharsets.UTF_8);
  }

  @Test
  void handle_withNewPassword_printsSuccessAndUser() {
    final UserResponse updated = mock(UserResponse.class);
    when(controller.updateUser(any(UpdateUserRequest.class))).thenReturn(updated);

    handlerWith(INPUT_WITH_PASSWORD).handle();

    assertThat(output()).contains("User updated successfully.");
    verify(printer).print(updated);
  }

  @Test
  void handle_withBlankPassword_keepsCurrentPasswordAndSucceeds() {
    final UserResponse updated = mock(UserResponse.class);
    when(controller.updateUser(any(UpdateUserRequest.class))).thenReturn(updated);

    handlerWith(INPUT_WITHOUT_PASSWORD).handle();

    assertThat(output()).contains("User updated successfully.");
    verify(printer).print(updated);
  }

  @Test
  void handle_whenUserNotFound_printsError() {
    final UserNotFoundException exception = mock(UserNotFoundException.class);
    when(exception.getMessage()).thenReturn("no existe");
    when(controller.updateUser(any(UpdateUserRequest.class))).thenThrow(exception);

    handlerWith(INPUT_WITH_PASSWORD).handle();

    assertThat(output()).contains("Not found: no existe");
  }
}
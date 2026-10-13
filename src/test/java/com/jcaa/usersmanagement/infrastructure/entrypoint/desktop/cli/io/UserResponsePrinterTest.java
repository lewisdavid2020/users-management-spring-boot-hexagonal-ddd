package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class UserResponsePrinterTest {

  private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private final PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
  private final ConsoleIO console = new ConsoleIO(new Scanner(""), out);
  private final UserResponsePrinter printer = new UserResponsePrinter(console);

  private String output() {
    return buffer.toString(StandardCharsets.UTF_8);
  }

  @Test
  void print_writesEveryFieldLabel() {
    printer.print(mock(UserResponse.class));

    assertThat(output()).contains("ID", "Name", "Email", "Role", "Status");
  }

  @Test
  void printList_withEmptyList_writesNoUsersMessage() {
    printer.printList(List.of());

    assertThat(output()).contains("No users found.");
  }

  @Test
  void printList_withUsers_writesTotalAndEachUser() {
    printer.printList(List.of(mock(UserResponse.class), mock(UserResponse.class)));

    assertThat(output()).contains("Total: 2 user(s)");
  }
}
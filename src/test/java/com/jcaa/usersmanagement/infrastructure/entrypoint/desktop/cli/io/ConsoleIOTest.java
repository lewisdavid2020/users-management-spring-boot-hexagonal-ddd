package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class ConsoleIOTest {

  private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private final PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);

  private ConsoleIO consoleWith(final String input) {
    return new ConsoleIO(new Scanner(input), out);
  }

  private String output() {
    return buffer.toString(StandardCharsets.UTF_8);
  }

  @Test
  void readRequired_retriesUntilValueIsNotBlank() {
    final ConsoleIO console = consoleWith("\n   \nvalor\n");

    final String result = console.readRequired("Dato: ");

    assertThat(result).isEqualTo("valor");
    assertThat(output()).contains("Dato: ").contains("Value cannot be blank");
  }

  @Test
  void readOptional_returnsTrimmedValue() {
    final ConsoleIO console = consoleWith("  abc  \n");

    assertThat(console.readOptional("Opcional: ")).isEqualTo("abc");
  }

  @Test
  void readInt_retriesUntilNumberIsValid() {
    final ConsoleIO console = consoleWith("x\n42\n");

    final int result = console.readInt("Numero: ");

    assertThat(result).isEqualTo(42);
    assertThat(output()).contains("Invalid input");
  }

  @Test
  void println_withMessage_writesMessage() {
    consoleWith("").println("hola");

    assertThat(output()).contains("hola");
  }

  @Test
  void println_withoutArguments_writesBlankLine() {
    consoleWith("").println();

    assertThat(output()).isEqualTo(System.lineSeparator());
  }

  @Test
  void printf_formatsArguments() {
    consoleWith("").printf("%s-%d", "a", 7);

    assertThat(output()).isEqualTo("a-7");
  }
}
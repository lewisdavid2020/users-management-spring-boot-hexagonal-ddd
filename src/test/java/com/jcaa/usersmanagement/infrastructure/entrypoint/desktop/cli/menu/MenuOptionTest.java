package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MenuOptionTest {

  @Test
  void fromNumber_withKnownNumber_returnsOption() {
    assertThat(MenuOption.fromNumber(1)).contains(MenuOption.LIST_USERS);
    assertThat(MenuOption.fromNumber(0)).contains(MenuOption.EXIT);
  }

  @Test
  void fromNumber_withUnknownNumber_returnsEmpty() {
    assertThat(MenuOption.fromNumber(99)).isEmpty();
  }

  @Test
  void fromNumber_findsEveryOptionByItsOwnNumber() {
    for (final MenuOption option : MenuOption.values()) {
      assertThat(MenuOption.fromNumber(option.getNumber())).contains(option);
    }
  }

  @Test
  void getters_returnNumberAndDescription() {
    assertThat(MenuOption.CREATE_USER.getNumber()).isEqualTo(3);
    assertThat(MenuOption.CREATE_USER.getDescription()).isEqualTo("Create user");
  }
}
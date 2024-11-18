package com.example.tgbotrusweb.logic.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
public class InputValidatorTest {

  @Autowired
  InputValidator inputValidator;

  @Test
  void isValidWordsInputTest() {
    String input = "/add world";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput1() {
    String input = "/add world; ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput2() {
    String input = "/add world; qwe q";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput3() {
    String input = "/add 1; asd; fik t; 49_kf soioif lkn9; ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput4() {
    String input = "/add 1; asd вfd; апф t; 49_kf soioif lkn9";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput5() {
    String input = "/add world; "
        + "worldd";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput6() {
    String input = "/add world, afs ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(false, result);
  }

  @Test
  void isValidWordsInput7() {
    String input = "/add world, ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(false, result);
  }

  @Test
  void isValidWordsInput8() {
    String input = "/add 1; asd вfd; апф, t; 49_kf soioif lkn9 ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(false, result);
  }

  @Test
  void isValidWordsInput9() {
    String input = "world; ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(false, result);
  }

  @Test
  void isValidWordsInput10() {
    String input = "1; asd вfd; апф, t; 49_kf soioif lkn9 ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(false, result);
  }

  @Test
  void isValidWordsInput11() {
    String input = "world, afs";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(false, result);
  }
}

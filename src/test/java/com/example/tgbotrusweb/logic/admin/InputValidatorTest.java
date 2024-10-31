package com.example.tgbotrusweb.logic.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.tgbotrusweb.logic.domain.admin.InputValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InputValidatorTest {

  @Autowired
  InputValidator inputValidator;

  @Test
  void isValidWordsInputTest() {
    String input = "world";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput1() {
    String input = "world; ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput2() {
    String input = "world; qwe q";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput3() {
    String input = "1; asd; fik t; 49_kf soioif lkn9; ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput4() {
    String input = "1; asd вfd; апф t; 49_kf soioif lkn9";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput5() {
    String input = "world; "
        + "worldd";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput6() {
    String input = "world, afs ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(false, result);
  }

  @Test
  void isValidWordsInput7() {
    String input = "world, ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(false, result);
  }

  @Test
  void isValidWordsInput8() {
    String input = "1; asd вfd; апф, t; 49_kf soioif lkn9 ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(false, result);
  }

 /* @Test
  void isValidWordsInput9() {
    String input = "world; ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }

  @Test
  void isValidWordsInput10() {
    String input = "world; ";

    Boolean result = inputValidator.isValidWordsInput(input);

    assertEquals(true, result);
  }*/
}

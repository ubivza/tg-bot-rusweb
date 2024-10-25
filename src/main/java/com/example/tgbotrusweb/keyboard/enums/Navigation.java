package com.example.tgbotrusweb.keyboard.enums;

import lombok.Getter;

@Getter
public enum Navigation {
  ABOUT_US("О нас", "Мы занимаемся веб-разработкой и осуществляем волонтёрскую поддержку российских некоммерческих проектов.\n"
      + "Мы разрабатываем: \n"
      + "\uD83D\uDD34 информационные / новостные порталы\n"
      + "\uD83D\uDD34 веб-сайты любой сложности\n"
      + "\uD83D\uDD34 приложения\n"
      + "\uD83D\uDD34 чат-боты для Telegram\n"
      + "\uD83D\uDD34 настраиваем интеграцию систем с Telegram\n"
      + "Работаем как с готовыми решениями (популярные opensource CMS), так и разрабатываем с нуля.\n"
      + "\n"
      + "Технологический стек: \n"
      + "\uD83D\uDD34 Java, Spring\n"
      + "\uD83D\uDD34 PHP (и CMS: Wordpress, Joomla, DLE, Opencart)\n"
      + "\uD83D\uDD34 Javascript (и фреймворки)\n"
      + "\uD83D\uDD34 SQL (MySQL, Postgre)"),
  JOIN("Стать частью команды", "Если вы технический специалист, разделяете наши взгляды и хотите помочь в реализации волонтерских проектов, укажите ваши данные для связи \uD83D\uDC47"),
  NON_COMMERCIAL("Некоммерческая разработка", "Мы осуществляем бесплатную разработку для некоммерческих проектов:\n"
      + "\uD83D\uDD34 ведущих информационную борьбу с западной пропагандой \n"
      + "\uD83D\uDD34 честно освещающих события в зоне СВО и занимающихся аналитикой ситуации в зоне конфликта\n"
      + "\uD83D\uDD34 волонтеров и активистов, занимающихся полезной для государства и общества деятельностью\n"
      + "\n"
      + "Если вы относитесь к одной из данных категорий и хотите оставить заявку, укажите ваши данные для связи \uD83D\uDC47"),
  COMMERCIAL("Коммерческая разработка", "Если вы хотите, чтобы с вами связались по вашему запросу, укажите ваши данные для связи \uD83D\uDC47");
  private String name;
  private String message;
  Navigation(String name, String message) {
    this.name = name;
    this.message = message;
  }
}

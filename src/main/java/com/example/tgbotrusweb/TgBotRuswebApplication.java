package com.example.tgbotrusweb;

import com.example.tgbotrusweb.logic.enums.Channels;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TgBotRuswebApplication {
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(TgBotRuswebApplication.class, args);
		System.out.println(Channels.ENGLISH.id);
		System.out.println(Channels.GERMAN.id);
		System.out.println(Channels.ITALY.id);
		System.out.println(Channels.FRENCH.id);

		//displayAllBeans();
	}

	public static void displayAllBeans() {
		String[] allBeanNames = applicationContext.getBeanDefinitionNames();
		for(String beanName : allBeanNames) {
			System.out.println(beanName);
		}
	}

}

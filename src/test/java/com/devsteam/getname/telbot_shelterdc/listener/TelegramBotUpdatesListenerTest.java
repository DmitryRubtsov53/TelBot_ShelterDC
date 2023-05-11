package com.devsteam.getname.telbot_shelterdc.listener;


import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.service.ReportService;
import com.google.gson.Gson;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static java.nio.file.Files.readString;


@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {


    @Mock
    private TelegramBot telegramBot;
    @Mock
    private ReportService reportService;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    /**
     * объект приюта собак, поля которого заполняются из БД для работы бота
     */
    private Shelter dogsShelter;
    /**
     * объект приюта кошек, поля которого заполняются из БД для работы бота
     */
    private Shelter catsShelter;

    @BeforeEach
    void setUp() throws IOException {
        this.dogsShelter = new Gson().fromJson(readString(Path.of("src/main/resources/", "dogShelter.json")), Shelter.class);

        this.catsShelter = new Gson().fromJson(readString(Path.of("src/main/resources/", "catShelter.json")), Shelter.class);
    }

    @Test
    public void handleStartTest() throws URISyntaxException, IOException {

        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("text_update.json").toURI()));
        Update update = getUpdate(json, "/start");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                "   Привет! Данный бот предоставляет информацию о двух приютах. Кошачий приют \"" + this.catsShelter.getTitle()+"\"" + " и собачий приют \"" + this.dogsShelter.getTitle() + "\". Выберите один");
        Assertions.assertThat(actual.getParameters().get("parse_mode"))
                .isEqualTo(ParseMode.Markdown.name());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Dogs? Вы выбрали приют Лучший друг",
            "InfoDogsShelter? Приют был основан в 1999 году, на данный момент в нем находится более 10 собак разных пород. За животными ухаживают волонтеры",
            "DogsShelterSecurity? +79999999999 Федор",
            "BackFromDogsInfo? Вы выбрали приют Лучший друг",
            "MainMenu? Выберите приют",
            "InfoDogs? Приют Лучший друг",
            "InfoCats? Приют Пушистик",
            "DogsShelterContact? Нажмите на кнопку оставить контакты для приюта",
            "CatsShelterContact? Нажмите на кнопку оставить контакты для приюта"
    }, delimiter = '?'
    )
    public void handleButtonParameterized(String callBackData, String menuMessage) throws URISyntaxException, IOException {
        handleButtonCallBackData(callBackData, menuMessage);
    }

    @Test
    public void handleButtonInfoDogsShelter() throws URISyntaxException, IOException {
        handleButtonCallBackData("InfoDogsShelter", dogsShelter.getInfo());
    }

    @Test
    public void handleButtonScheduleDogs() throws URISyntaxException, IOException {
        handleButtonCallBackData("ScheduleDogs", dogsShelter.getAddress() + "\n\n" + dogsShelter.getSchedule() + "\n\n " + "<a href=\"" + dogsShelter.getMapLink() + "\">Ссылка на Google Maps</a>");
    }
    @Test
    public void handleButtonScheduleCats() throws URISyntaxException, IOException {
        handleButtonCallBackData("ScheduleCats", catsShelter.getAddress() + "\n\n" + catsShelter.getSchedule() + "\n\n " + "<a href=\"" + catsShelter.getMapLink() + "\">Ссылка на Google Maps</a>");
    }

    @Test
    public void handleButtonSafetyRecommendationsDogsShelter() throws URISyntaxException, IOException {
        handleButtonCallBackData("SafetyRecommendationsDogsShelter", dogsShelter.getSafetyPrecautions());
    }
    @Test
    public void handleButtonSafetyRecommendationsCatsShelter() throws URISyntaxException, IOException {
        handleButtonCallBackData("SafetyRecommendationsCatsShelter", catsShelter.getSafetyPrecautions());
    }

    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }

    @Test
    public void handleValidReport() throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("photo_update.json").toURI()));
        Update update = getUpdate(json, "/report test");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(
                SendMessage.class);
        Mockito.verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage actualSendMessage = sendMessageArgumentCaptor.getValue();

        ArgumentCaptor<String> descriptionArgumentCaptor = ArgumentCaptor.forClass(
                String.class);
        ArgumentCaptor<String> photoArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(reportService).addReport(
                longArgumentCaptor.capture(),
                descriptionArgumentCaptor.capture(),
                photoArgumentCaptor.capture()
        );
        String actualDescription = descriptionArgumentCaptor.getValue();
        String actualPhoto = photoArgumentCaptor.getValue();
        Long actualLong = longArgumentCaptor.getValue();

        Assertions.assertThat(actualDescription)
                .isEqualTo(" test");
        Assertions.assertThat(actualPhoto).isEqualTo("photo");
        Assertions.assertThat(actualLong).isEqualTo(123L);

        Assertions.assertThat(actualSendMessage.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actualSendMessage.getParameters().get("text")).isEqualTo(
                "добавляем отчёт");
    }

    public void handleButtonCallBackData(String callBackData, String menuMessage) throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback_data_update.json").toURI()));
        Update update = getUpdate(json, callBackData);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                menuMessage);
        Assertions.assertThat(actual.getParameters().get("reply_markup")).isNotNull();




    }
}
package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.pageobject.DashboardPage;
import ru.netology.pageobject.LoginPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    private final String login = "vasya";
    private final String password = "qwerty123";
    private final String verificationCode = "12345";
    private final String firstCardNumber = "0001";
    private final String secondCardNumber = "0002";

    @BeforeEach
    void setUp() {
        Configuration.headless = false;
        Configuration.timeout = 15000;
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        DashboardPage dashboard = new LoginPage()
                .validLogin(login, password)
                .validVerify(verificationCode);

        int initialBalanceFirst = dashboard.getCardBalanceByNumber(firstCardNumber);
        int initialBalanceSecond = dashboard.getCardBalanceByNumber(secondCardNumber);
        int amount = 1000;

        dashboard.replenishCardByNumber(secondCardNumber)
                .makeTransfer(String.valueOf(amount), firstCardNumber);

        // Крошечная пауза, чтобы страница гарантированно обновилась
        sleep(500);

        int newBalanceFirst = dashboard.getCardBalanceByNumber(firstCardNumber);
        int newBalanceSecond = dashboard.getCardBalanceByNumber(secondCardNumber);

        assertEquals(initialBalanceFirst - amount, newBalanceFirst);
        assertEquals(initialBalanceSecond + amount, newBalanceSecond);
    }

    @Test
    void shouldNotTransferMoreThanBalance() {
        DashboardPage dashboard = new LoginPage()
                .validLogin(login, password)
                .validVerify(verificationCode);

        int initialBalanceFirst = dashboard.getCardBalanceByNumber(firstCardNumber);
        int initialBalanceSecond = dashboard.getCardBalanceByNumber(secondCardNumber);
        int tooMuch = initialBalanceFirst + 1;

        dashboard.replenishCardByNumber(secondCardNumber)
                .makeTransfer(String.valueOf(tooMuch), firstCardNumber);

        // Проверяем, что остались на странице перевода
        $("input.input__control[type='tel'][placeholder='0000 0000 0000 0000']").shouldBe(visible);

        // Заново заходим на дашборд
        open("http://localhost:9999");
        dashboard = new LoginPage()
                .validLogin(login, password)
                .validVerify(verificationCode);

        int newBalanceFirst = dashboard.getCardBalanceByNumber(firstCardNumber);
        int newBalanceSecond = dashboard.getCardBalanceByNumber(secondCardNumber);

        assertEquals(initialBalanceFirst, newBalanceFirst);
        assertEquals(initialBalanceSecond, newBalanceSecond);
    }
}
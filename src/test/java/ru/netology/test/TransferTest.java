package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pageobject.DashboardPage;
import ru.netology.pageobject.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        String login = DataHelper.getValidAuthInfo().getLogin();
        String password = DataHelper.getValidAuthInfo().getPassword();
        String code = DataHelper.getVerificationCode().getCode();
        String firstCard = DataHelper.getFirstCardNumber();
        String secondCard = DataHelper.getSecondCardNumber();

        DashboardPage dashboard = new LoginPage()
                .validLogin(login, password)
                .validVerify(code);

        int initialBalanceFirst = dashboard.getCardBalanceByNumber(firstCard);
        int initialBalanceSecond = dashboard.getCardBalanceByNumber(secondCard);
        int amount = 1000;

        dashboard.replenishCardByNumber(secondCard)
                .makeTransfer(String.valueOf(amount), firstCard);

        // Явное ожидание возврата на дашборд
        new DashboardPage();

        DashboardPage updatedDashboard = new DashboardPage();
        int newBalanceFirst = updatedDashboard.getCardBalanceByNumber(firstCard);
        int newBalanceSecond = updatedDashboard.getCardBalanceByNumber(secondCard);

        assertEquals(initialBalanceFirst - amount, newBalanceFirst);
        assertEquals(initialBalanceSecond + amount, newBalanceSecond);
    }

    @Test
    void shouldNotTransferMoreThanBalance() {
        String login = DataHelper.getValidAuthInfo().getLogin();
        String password = DataHelper.getValidAuthInfo().getPassword();
        String code = DataHelper.getVerificationCode().getCode();
        String firstCard = DataHelper.getFirstCardNumber();
        String secondCard = DataHelper.getSecondCardNumber();

        DashboardPage dashboard = new LoginPage()
                .validLogin(login, password)
                .validVerify(code);

        int initialBalanceFirst = dashboard.getCardBalanceByNumber(firstCard);
        int initialBalanceSecond = dashboard.getCardBalanceByNumber(secondCard);
        int tooMuch = initialBalanceFirst + 1;

        // Пытаемся перевести больше, чем есть на карте
        dashboard.replenishCardByNumber(secondCard)
                .makeTransfer(String.valueOf(tooMuch), firstCard);

        // Сервер не должен позволять такой перевод; ожидаем, что балансы НЕ изменились.
        // Заново заходим на дашборд и проверяем балансы
        open("http://localhost:9999");
        DashboardPage finalDashboard = new LoginPage()
                .validLogin(login, password)
                .validVerify(code);

        int newBalanceFirst = finalDashboard.getCardBalanceByNumber(firstCard);
        int newBalanceSecond = finalDashboard.getCardBalanceByNumber(secondCard);

        assertEquals(initialBalanceFirst, newBalanceFirst,
                "Баланс первой карты не должен измениться");
        assertEquals(initialBalanceSecond, newBalanceSecond,
                "Баланс второй карты не должен измениться");
    }
}
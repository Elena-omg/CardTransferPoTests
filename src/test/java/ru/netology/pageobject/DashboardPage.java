package ru.netology.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    public int getCardBalanceByNumber(String cardNumber) {
        ElementsCollection buttons = $$("[data-test-id=action-deposit]");
        for (SelenideElement button : buttons) {
            SelenideElement cardDiv = button.closest("div");
            String text = cardDiv.getText();
            if (text.contains(cardNumber)) {
                int start = text.indexOf("баланс:") + "баланс:".length();
                int end = text.indexOf("р.", start);
                if (start < "баланс:".length() || end == -1) {
                    throw new RuntimeException("Не найден баланс в строке: " + text);
                }
                String numberStr = text.substring(start, end).trim().replaceAll("\\s+", "");
                return Integer.parseInt(numberStr);
            }
        }
        throw new RuntimeException("Карта " + cardNumber + " не найдена");
    }

    public TransferPage replenishCardByNumber(String cardNumber) {
        ElementsCollection buttons = $$("[data-test-id=action-deposit]");
        for (SelenideElement button : buttons) {
            SelenideElement cardDiv = button.closest("div");
            if (cardDiv.getText().contains(cardNumber)) {
                button.click();
                return new TransferPage();
            }
        }
        throw new RuntimeException("Карта " + cardNumber + " не найдена");
    }
}
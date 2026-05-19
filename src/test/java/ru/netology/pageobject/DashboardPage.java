package ru.netology.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    public int getCardBalanceByNumber(String cardNumber) {
        // Явно ждём, пока на странице появится слово "баланс:"
        $("body").shouldHave(text("баланс:"));

        // Читаем весь текст страницы
        String pageText = $("body").getText();

        // Разбиваем на строки и ищем ту, где есть номер карты и "баланс:"
        for (String line : pageText.split("\n")) {
            if (line.contains(cardNumber) && line.contains("баланс:")) {
                int idx = line.indexOf("баланс:") + "баланс:".length();
                String digits = "";
                for (int i = idx; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (Character.isDigit(c)) {
                        digits += c;
                    } else if (!digits.isEmpty()) {
                        break;
                    }
                }
                if (!digits.isEmpty()) {
                    return Integer.parseInt(digits);
                }
            }
        }
        throw new RuntimeException("Баланс для карты " + cardNumber + " не найден");
    }

    public TransferPage replenishCardByNumber(String cardNumber) {
        ElementsCollection buttons = $$(byText("Пополнить"));
        for (SelenideElement button : buttons) {
            SelenideElement cardDiv = button.parent();
            if (cardDiv.getText().contains(cardNumber)) {
                button.click();
                $("input.input__control[type='tel'][placeholder='0000 0000 0000 0000']").shouldBe(visible);
                return new TransferPage();
            }
        }
        throw new RuntimeException("Карта " + cardNumber + " не найдена для пополнения");
    }
}
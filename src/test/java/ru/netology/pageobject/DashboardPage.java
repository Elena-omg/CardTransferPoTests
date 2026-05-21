package ru.netology.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    private final ElementsCollection cards = $$("div[data-test-id]");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        $("[data-test-id=action-deposit]").shouldBe(visible);
    }

    public int getCardBalanceByNumber(String cardNumber) {
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        SelenideElement card = cards.findBy(text(lastFourDigits));
        return extractBalance(card.getText());
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage replenishCardByNumber(String cardNumber) {
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        SelenideElement card = cards.findBy(text(lastFourDigits));
        card.find("button[data-test-id=action-deposit]").click();
        return new TransferPage();
    }
}
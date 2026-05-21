package ru.netology.pageobject;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountField = $("input[type='text']");
    private final SelenideElement fromField = $("input[type='tel']");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");

    public TransferPage() {
        // Явное ожидание загрузки формы перевода
        amountField.shouldBe(visible);
    }

    public void makeTransfer(String amount, String fromCardNumber) {
        amountField.setValue(amount);
        fromField.setValue(fromCardNumber);
        transferButton.click();
    }
}
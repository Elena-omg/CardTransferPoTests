package ru.netology.pageobject;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountField = $("input.input__control[type='text'][maxlength='14']");
    private final SelenideElement fromField = $("input.input__control[type='tel'][placeholder='0000 0000 0000 0000']");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");

    public DashboardPage makeTransfer(String amount, String fromCardNumber) {
        amountField.setValue(amount);
        fromField.setValue(fromCardNumber);
        transferButton.click();
        // Не ждём дашборд, просто возвращаем новый объект
        return new DashboardPage();
    }
}
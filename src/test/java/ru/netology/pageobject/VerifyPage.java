package ru.netology.pageobject;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerifyPage {
    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public DashboardPage validVerify(String code) {
        codeField.setValue(code);
        verifyButton.click();
        // Простое ожидание появления кнопки
        $("[data-test-id=action-deposit]").shouldBe(visible);
        return new DashboardPage();
    }
}
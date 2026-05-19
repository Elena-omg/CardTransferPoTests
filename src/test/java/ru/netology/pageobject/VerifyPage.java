package ru.netology.pageobject;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.Wait;

public class VerifyPage {
    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public DashboardPage validVerify(String code) {
        codeField.setValue(code);
        verifyButton.click();
        // Ждём, пока URL изменится на /dashboard
        Wait().until(driver -> driver.getCurrentUrl().contains("dashboard"));
        return new DashboardPage();
    }
}
package com.uff.pdvselenium;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class GrupoSeleniumTest {
    private final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open(BASE_URL);
        $("input[id=user]").sendKeys("gerente");
        $("input[id=password]").sendKeys("123");
        $("button[type=submit]").click();
        $$("img").findBy(Condition.attribute("alt", "Produto")).click();
        $$("a").findBy(Condition.href("/grupo")).click();
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    @Test
    public void createGrupo(){
        int amountBefore = $("tbody").findAll("tr").size();
        while ($("a[class='btn btn-info btn-proxima']").exists()) {
            $("a[class='btn btn-info btn-proxima']").click();
            amountBefore += $("tbody").findAll("tr").size();
        }

        $$("a").findBy(Condition.text("Novo")).click();
        $("#descricao").sendKeys("Alimentação");
        $("input[value=Salvar]").click();
        assert $$("span").findBy(Condition.text("Grupo salvo com sucesso")).isDisplayed();
        $(byText("Listar")).click();

        int newAmount = $("tbody").findAll("tr").size();
        while ($("a[class='btn btn-info btn-proxima']").exists()) {
            $("a[class='btn btn-info btn-proxima']").click();
            newAmount += $("tbody").findAll("tr").size();
        }
        assert newAmount > amountBefore;
    }
}

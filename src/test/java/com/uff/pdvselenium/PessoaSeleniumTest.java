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
import java.util.Random;

public class PessoaSeleniumTest {
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
        $$("img").findBy(Condition.attribute("alt", "Pessoas")).click();
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    @Test
    public void createPessoaValid(){
        Random random = new Random();
        String cpf = "";
        for (int i = 0; i < 11; i++){
            cpf += String.valueOf(random.nextInt(9));
        }
        int amountBefore = $("tbody").findAll("tr").size();
        $$("a").findBy(Condition.text("Novo")).click();
        $("#nome").sendKeys("Teste da Silva");
        $("#apelido").sendKeys("Testin");
        $("#cpfcnpj").sendKeys(cpf);
        $("#nascimento").sendKeys("10/10/1996");
        $("#observacao").sendKeys("Pessoa de teste");
        $("a[href='#menu1']").click();
        $("#cidade").sendKeys("Rolim de Moura");
        $("#rua").sendKeys("Rua Domingos Freire");
        $("#bairro").sendKeys("Todos os Santos");
        $("#numero").sendKeys("44");
        $("#cep").sendKeys("20735220");
        $("#referencia").sendKeys("Posto de Saúde do Engenho de Dentro");
        $("a[href='#menu2']").click();
        $("#fone").sendKeys("25939536");
        $("#tipo").sendKeys("CELULAR");
        $("input[value=Salvar]").click();
        confirm("Pessoa salva com sucesso");
        $(byText("Listar")).click();
        assert $("tbody").findAll("tr").size() > amountBefore;
    }

    @Test
    public void createPessoaInvalid(){
        int amountBefore = $("tbody").findAll("tr").size();
        $$("a").findBy(Condition.text("Novo")).click();
        $("#nome").sendKeys("Teste da Silva");
        $("#apelido").sendKeys("Testin");
        $("#nascimento").sendKeys("10/10/1996");
        $("#observacao").sendKeys("Pessoa de teste");
        $("a[href='#menu1']").click();
        $("#cidade").sendKeys("Rolim de Moura");
        $("#rua").sendKeys("Rua Domingos Freire");
        $("#bairro").sendKeys("Todos os Santos");
        $("#numero").sendKeys("44");
        $("#cep").sendKeys("20735220");
        $("#referencia").sendKeys("Posto de Saúde do Engenho de Dentro");
        $("a[href='#menu2']").click();
        $("#fone").sendKeys("25939536");
        $("#tipo").sendKeys("CELULAR");
        $("input[value=Salvar]").click();
        assert $("#cpfcnpj-error").isDisplayed();
        $(byText("Listar")).click();
        assert $("tbody").findAll("tr").size() == amountBefore;
    }
}

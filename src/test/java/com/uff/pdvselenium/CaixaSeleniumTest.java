package com.uff.pdvselenium;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CaixaSeleniumTest {
    private static final String BASE_URL = "http://localhost:8080";
    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeAll
    static void setUp() {
        open(BASE_URL);
        $("input[id=user]").sendKeys("gerente");
        $("input[id=password]").sendKeys("123");
        $("button[type=submit]").click();
        $$("img").findBy(Condition.attribute("alt", "Caixa")).click();
    }

    @AfterAll
    static void tearDown() {
        closeWebDriver();
    }

    @Test
    @Order(1)
    public void openCaixa() {
        $$("a").findBy(Condition.text("Abrir Novo")).click();
        $("#descricao").sendKeys("Caixa preferencial");
        $("#caixatipo").sendKeys("CAIXA");
        $("#valorAbertura").sendKeys("25000");
        $$("a").findBy(Condition.text("Abrir")).click();
        assert $$("tr[class=success] td").find(Condition.text("R$ 250")).should(exist).exists();
    }

    // TODO test wait issue
    @Test
    @Order(2)
    public void retirarCaixa() {
        $("#btnSangria").click();
        $("#idvl").sendKeys("15000");
        $("#idobs").sendKeys("Despesa com funcionário");
        $$("a").findBy(Condition.text("Confirmar")).click();
        confirm();
        sleep(500);
        SelenideElement el = $$("tr td").findBy(Condition.text("Despesa com funcionário"));
        assert el.exists();
        assert el.find(By.xpath("..//td[contains(text(), 'R$ -150,00')]")).exists();
        assert el.find(By.xpath("..//td[contains(text(), 'Despesa com funcionário')]")).exists();
    }

    @Test
    @Order(3)
    public void inserirCaixa() {
        $("#btnSuprimento").click();
        $("#idvalor").sendKeys("30000");
        $("#idObs").sendKeys("Entrada externa");
        $$("a").findBy(Condition.text("Confirmar")).click();
        confirm();
        sleep(500);
        SelenideElement el = $$("tr td").findBy(Condition.text("Entrada externa"));
        assert el.exists();
        assert el.find(By.xpath("..//td[contains(text(), 'R$ 300,00')]")).exists();
        assert el.find(By.xpath("..//td[contains(text(), 'Entrada externa')]")).exists();
    }

    @Test
    @Order(4)
    public void checkCaixa() {
        $$("img").findBy(Condition.attribute("alt", "Caixa")).click();
        assert $$("th").find(Condition.text("#")).exists();
        assert $$("th").find(Condition.text("Descrição")).exists();
        assert $$("th").find(Condition.text("Valor Total")).exists();
        assert $$("th").find(Condition.text("Data Abertura")).exists();
        assert $$("th").find(Condition.text("Data Fechamento")).exists();
    }

    @Test
    @Order(5)
    public void transferirCaixa() {
        $("td a img").click();
        $("#btnTransferencia").click();
        $("#vltotal").sendKeys("30000");
        $("#idobservacao").sendKeys("Transferência entre caixas");
        $("#iddestino").sendKeys("Caixa preferencial - 1");
        $$("a").findBy(Condition.text("Confirmar")).click();
        confirm("Transferência realizada com sucesso");
        sleep(500);
        SelenideElement el = $$("tr td").findBy(Condition.text("Transferência entre caixas"));
        assert el.exists();
        assert el.find(By.xpath("..//td[contains(text(), 'R$ -300,00')]")).exists();
        assert el.find(By.xpath("..//td[contains(text(), 'Transferência entre caixas')]")).exists();
    }

    @Test
    @Order(6)
    public void fecharCaixa() {
        open(BASE_URL);
        $$("img").findBy(Condition.attribute("alt", "Caixa")).click();
        $("td a img").click();
        $("#btnfechacaixa").click();
        $("#admsenha").sendKeys("123");
        $$("div a").find(Condition.text("Fechar")).click();
        confirm("Caixa fechado com sucesso");
    }
}

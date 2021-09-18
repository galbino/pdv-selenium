package com.uff.pdvselenium;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.util.Random;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PedidoSeleniumTest {
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
        $$("a").findBy(Condition.text("Pedidos")).click();
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    @Test
    @Order(1)
    public void createTitulo(){
        $$("img").findBy(Condition.attribute("alt", "Parâmetros")).click();
        $$("a").findBy(Condition.href("/titulos")).click();
        $$("a").findBy(Condition.text("Novo")).click();
        $("#descricao").sendKeys("Teste Selenium");
        $("input[value=Salvar]").click();
    }

    @Test
    @Order(2)
    public void createPedidoWithoutCaixa(){
        $$("a").findBy(Condition.text("Novo Pedido")).click();
        $("#cliente").sendKeys("Teste da Silva");
        $("#observacao").sendKeys("Teste Selenium");
        $("#btn-salva").click();
        $(byXpath("//select[@id='codigoProduto']//..//button")).click();
        $(byXpath("//select[@id='codigoProduto']//..//input")).sendKeys("Picolé", Keys.ENTER);
        $("#js-url").click();
        Selenide.sleep(1000);
        $("#btn-venda").click();
        $("#pagamento").sendKeys("À vista");
        $$("a").find(Condition.text("Pagar")).click();
        confirm("nenhum caixa aberto");
        assert $$("span").find(Condition.text("ABERTA")).isDisplayed();
    }

    @Test
    @Order(3)
    public void openCaixa() {
        open(BASE_URL);
        $$("img").findBy(Condition.attribute("alt", "Caixa")).click();
        $$("a").findBy(Condition.text("Abrir Novo")).click();
        $("#descricao").sendKeys("Caixa preferencial");
        $("#caixatipo").sendKeys("CAIXA");
        $("#valorAbertura").sendKeys("25000");
        $$("a").findBy(Condition.text("Abrir")).click();
        assert $$("tr[class=success] td").find(Condition.text("R$ 250")).should(exist).exists();
    }

    @Test
    @Order(4)
    public void createPedidoValid(){
        $$("a").findBy(Condition.text("Novo Pedido")).click();
        $("#cliente").sendKeys("Teste da Silva");
        $("#observacao").sendKeys("Teste Selenium");
        $("#btn-salva").click();
        $(byXpath("//select[@id='codigoProduto']//..//button")).click();
        $(byXpath("//select[@id='codigoProduto']//..//input")).sendKeys("Picolé", Keys.ENTER);
        $("#js-url").click();
        Selenide.sleep(1000);
        $("#btn-venda").click();
        $("#pagamento").sendKeys("À vista");
        $$("a").find(Condition.text("Pagar")).click();
        confirm("Venda finalizada com sucesso");
        assert $$("span").find(Condition.text("FECHADA")).isDisplayed();
    }

    @Test
    @Order(5)
    public void createPedidoInvalid(){
        $$("a").findBy(Condition.text("Novo Pedido")).click();
        $("#cliente").sendKeys("Teste da Silva");
        $("#observacao").sendKeys("Teste Selenium");
        $("#btn-salva").click();
        $("#js-url").click();
        Selenide.sleep(1000);
        $("#btn-venda").click();
        $("#pagamento").sendKeys("À vista");
        $$("a").find(Condition.text("Pagar")).click();
        confirm("Venda sem valor, verifique");
        assert $$("span").find(Condition.text("ABERTA")).isDisplayed();
    }

    @Test
    @Order(6)
    public void fecharCaixa() {
        open(BASE_URL);
        $$("img").findBy(Condition.attribute("alt", "Caixa")).click();
        $("td a img[src='/icons/glyphicons-459-money.png']").click();
        $("#btnfechacaixa").click();
        $("#admsenha").sendKeys("123");
        $$("div a").find(Condition.text("Fechar")).click();
        confirm("Caixa fechado com sucesso");
    }

}

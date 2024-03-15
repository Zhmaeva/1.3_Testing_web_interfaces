package ru.netology.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderFormTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test // end to end позитивная проверка
    void shouldSendCardOrderForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Мария Петрова - Сидорова");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79998887744");
        driver.findElement(By.cssSelector("[data-test-id=agreement] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test // отправка пустой формы
    void shouldNotSendCardOrderFormWithEmptyFields() {
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", actual.trim());
    }

    @Test // отправка формы с незаполненным полем имя
    void shouldNotSendCardOrderFormWithEmptyFieldName() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", actual.trim());
    }

    @Test // отправка формы с незаполненным полем телефон
    void shouldNotSendCardOrderFormWithEmptyFieldPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Мария Петрова");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", actual.trim());
    }

    @Test // отправка формы с неактивным чекбоксом
    void shouldNotSendCardOrderFormWithNotActiveCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Мария Петрова");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79998887744");
        driver.findElement(By.cssSelector(".form-field button")).click();
        driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).isDisplayed();
    }

    @Test // имя на иностранном языке
    void shouldNotSendCardOrderFormWithLatinName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ekaterina Kim");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79998887744");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expectedText, actualText.trim());
    }

    @Test // имя из символов
    void shouldNotSendCardOrderFormWithSymbolName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("/<>");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79998887744");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expectedText, actualText.trim());
    }

    @Test  // имя с буквой ё
    void shouldNotSendCardOrderFormWithSpecificCharName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Алёна Фёдоровна");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79998887744");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expectedText, actualText.trim());
    }

    @Test // имя из цифр
    void shouldNotSendCardOrderFormWithNumberName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("1111");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79998887744");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expectedText, actualText.trim());
    }

    @Test // телефон меньше 11 цифр
    void shouldNotSendCardOrderFormWithPhoneNumberLess11() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Екатерина Ким");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+1234567891");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid  .input__sub")).getText();
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expectedText, actualText.trim());
    }

    @Test // телефон больше 11 цифр
    void shouldNotSendCardOrderFormWithPhoneNumberMore11() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Екатерина Ким");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+123456789123");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid  .input__sub")).getText();
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expectedText, actualText.trim());
    }

    @Test // телефон без знака +
    void shouldNotSendCardOrderFormWithPhoneNumberWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Екатерина Ким");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("89001110000");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid  .input__sub")).getText();
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expectedText, actualText.trim());
    }

    @Test // телефон с символами и пробелами
    void shouldNotSendCardOrderFormWithPhoneNumberWithSymbols() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Екатерина Ким");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7 (900) 000-00-00");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector(".form-field button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid  .input__sub")).getText();
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expectedText, actualText.trim());
    }
}

package com.devtiro.bookstore.selenium

import org.junit.jupiter.api.*
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.Duration

@Tag("selenium")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminBookFormTest {

    private lateinit var driver: WebDriver

    @BeforeAll
    fun setUp() {
        val options = ChromeOptions()
        options.addArguments(
            "--remote-allow-origins=*",
            "--disable-dev-shm-usage",
            "--no-sandbox",
            "--disable-gpu",
            "--window-size=1920,1080"
        )
        driver = ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))
    }

    @AfterAll
    fun tearDown() {
        if (::driver.isInitialized) {
            try {
                driver.quit()
            } catch (_: Exception) {
                println("Browser already closed.")
            }
        }
    }

    private fun waitUntilFrontendIsAvailable(url: String, timeoutSeconds: Long = 30) {
        val deadline = System.currentTimeMillis() + timeoutSeconds * 1000
        while (System.currentTimeMillis() < deadline) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 2000
                connection.readTimeout = 2000
                connection.requestMethod = "GET"
                if (connection.responseCode in 200..399) return
            } catch (_: Exception) {}
            Thread.sleep(1000)
        }
        throw RuntimeException("Frontend not available at $url")
    }

    private fun doSafeClick(element: WebElement) {
        (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", element)
    }

    @Test
    fun testCreateBookFormFields() {
        val frontendHost = "http://localhost:3000"
        waitUntilFrontendIsAvailable(frontendHost)
        driver.get("$frontendHost/admin/books")

        if ((driver as? ChromeDriver)?.sessionId == null) {
            throw IllegalStateException("WebDriver session is not active. Browser may have crashed.")
        }

        val wait = WebDriverWait(driver, Duration.ofSeconds(30))

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[aria-label='ISBN']"))).apply {
            clear()
            sendKeys("978-99927-0-000-1")
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[aria-label='Title']"))).apply {
            clear()
            sendKeys("Selenium Book")
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("textarea[aria-label='Description']"))).apply {
            clear()
            sendKeys("Description for Selenium testing book.")
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[aria-label='Image']"))).apply {
            clear()
            sendKeys("book-test.jpg")
        }

        // Use the label to locate dropdown trigger
        val label = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(), 'Select Author')]")))
        val dropdownTrigger = label.findElement(By.xpath("../following-sibling::div//button"))
        doSafeClick(dropdownTrigger)

        // Wait and select first item in dropdown
        val authorOptions = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("ul[role='listbox'] li"))
        )
        require(authorOptions.isNotEmpty()) { "No authors available in dropdown" }
        doSafeClick(authorOptions[0])

        val submitButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Create Book') or contains(text(),'Update Book')]")
            )
        )
        submitButton.click()

        Thread.sleep(2000)

        val screenshot = (driver as TakesScreenshot).getScreenshotAs(OutputType.FILE)
        val destFile = File("target/screenshots/book_form_submit.png")
        destFile.parentFile.mkdirs()
        Files.copy(screenshot.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        println("Screenshot saved to: ${'$'}{destFile.absolutePath}")
    }
}

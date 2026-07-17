package com.devtiro.bookstore.selenium

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
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
class AdminAuthorFormTest {
    private lateinit var driver: WebDriver

    @BeforeAll
    fun setUp() {
        val options = ChromeOptions()
        driver = ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))
    }

    @AfterAll
    fun tearDown() {
        if (::driver.isInitialized) {
            driver.quit()
        }
    }

    private fun waitUntilFrontendIsAvailable(
        url: String,
        timeoutSeconds: Long = 30,
    ) {
        val deadline = System.currentTimeMillis() + timeoutSeconds * 1000
        while (System.currentTimeMillis() < deadline) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 2000
                connection.readTimeout = 2000
                connection.requestMethod = "GET"
                if (connection.responseCode in 200..399) return
            } catch (_: Exception) {
            }
            Thread.sleep(1000)
        }
        throw RuntimeException("Frontend not available at $url")
    }

    @Test
    fun testCreateAuthorFormFields() {
        val frontendHost = "http://localhost:3000"
        waitUntilFrontendIsAvailable(frontendHost)
        driver.get("$frontendHost/admin/authors")

        val wait = WebDriverWait(driver, Duration.ofSeconds(30))

        try {
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[aria-label='Name']"),
                ),
            )
        } catch (ex: Exception) {
            val screenshot = (driver as TakesScreenshot).getScreenshotAs(OutputType.FILE)
            val errorScreenshot = File("target/screenshots/error_form_name_input_not_found.png")
            errorScreenshot.parentFile.mkdirs()
            Files.copy(screenshot.toPath(), errorScreenshot.toPath(), StandardCopyOption.REPLACE_EXISTING)
            println("ERROR: Name input not found. Screenshot: ${errorScreenshot.absolutePath}")
            throw ex
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[aria-label='Name']"))).apply {
            clear()
            sendKeys("Selenium Test")
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[aria-label='Age']"))).apply {
            clear()
            sendKeys("45")
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("textarea[aria-label='Description']"))).apply {
            clear()
            sendKeys("Biography of test author.")
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[aria-label='Image Filename']"))).apply {
            clear()
            sendKeys("author-benjamin.png")
        }

        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Create Author') or contains(text(),'Update Author')]"),
            ),
        ).click()

        Thread.sleep(2000)

        val screenshot = (driver as TakesScreenshot).getScreenshotAs(OutputType.FILE)
        val destFile = File("target/screenshots/author_form_submit.png")
        destFile.parentFile.mkdirs()
        Files.copy(screenshot.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        println("Screenshot saved to: ${destFile.absolutePath}")
    }
}

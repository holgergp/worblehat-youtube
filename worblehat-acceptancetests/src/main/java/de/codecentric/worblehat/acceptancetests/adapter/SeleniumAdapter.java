package de.codecentric.worblehat.acceptancetests.adapter;

import de.codecentric.worblehat.acceptancetests.adapter.wrapper.HtmlBookList;
import de.codecentric.worblehat.acceptancetests.adapter.wrapper.Page;
import de.codecentric.worblehat.acceptancetests.adapter.wrapper.PageElement;
import org.apache.commons.io.FileUtils;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.ScenarioType;
import org.joda.time.LocalDateTime;
import org.junit.ClassRule;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.lifecycle.TestDescription;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

/**
 * Itegrates Selenium into the tests.
 */
@Component("SeleniumAdapter")
public class SeleniumAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumAdapter.class);

    private WebDriver driver;

    private String folderName;

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    @BeforeStories
    public void initSelenium() {
        folderName = LocalDateTime.now().toString("yyyy-MM-dd HH:mm").concat(File.separator);
        folderName = "target" + File.separator + "screenshots" + File.separator + folderName;
        new File(folderName).mkdirs();
    }

    public void gotoPage(Page page) {
        goToUrl(page.getUrl());
    }

    public void gotoPageWithParameter(Page page, String parameter) {
        String url = page.getUrl(parameter);
        goToUrl(url);
    }

    private void goToUrl(String url) {
        String concreteUrl = Config.getApplicationURL() + "/" + url;
        driver.get(concreteUrl);
    }

    public void typeIntoField(String id, String value) {
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(value);
    }

    public HtmlBookList getTableContent(PageElement pageElement) {
        WebElement table = driver.findElement(By.className(pageElement.getElementId()));
        return new HtmlBookList(table);
    }

    public void clickOnPageElementById(PageElement pageElement) {
        WebElement element = driver.findElement(By.id(pageElement.getElementId()));
        element.click();
    }

    public void clickOnPageElementByClassName(String className) {
        WebElement element = driver.findElement(By.className(className));
        element.click();

    }

    public List<String> findAllStringsForElement(PageElement pageElement) {
        List<WebElement> webElements = driver.findElements(By.className(pageElement.getElementId()));
        List<String> strings = new ArrayList<>();
        for (WebElement element : webElements) {
            strings.add(element.getText());
        }
        return strings;
    }

    @AfterScenario(uponType = ScenarioType.EXAMPLE)
    public void afterAnyScenario() {
        driver.manage().deleteAllCookies();
    }

    public String getTextFromElement(PageElement pageElement) {
        WebElement element = driver.findElement(By.id(pageElement.getElementId()));
        return element.getText();
    }

    public void takeScreenShot(String filename) {

        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(folderName.concat(filename).concat(".png")));
        } catch (IOException e) {
            LOGGER.error("Could not take screenshot!", e);
        }

    }

    public boolean containsTextOnPage(String text) {
        return driver.getPageSource().contains(text);
    }
}

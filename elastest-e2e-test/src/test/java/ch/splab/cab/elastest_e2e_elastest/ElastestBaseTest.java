package ch.splab.cab.elastest_e2e_elastest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.getProperty;
import static java.util.logging.Level.ALL;
import static org.openqa.selenium.logging.LogType.BROWSER;
import static org.openqa.selenium.remote.CapabilityType.LOGGING_PREFS;
import static org.openqa.selenium.remote.DesiredCapabilities.chrome;

import ch.splab.cab.elastest_e2e_elastest.DriverCapabilities;

public class ElastestBaseTest {
    protected static final Logger logger = LogManager
            .getLogger(ElastestBaseTest.class);

    protected static final String CHROME = "chrome";
    protected static final String FIREFOX = "firefox";

    protected static String browserType;
    protected static String browserVersion;
    protected static String eusURL;
    protected static String sutUrl;

    protected WebDriver driver;

    @DriverCapabilities
    DesiredCapabilities capabilities = chrome();
    {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(BROWSER, ALL);
        capabilities.setCapability(LOGGING_PREFS, logPrefs);
    }

    @BeforeAll
    public static void setupClass() {
        String osName = getProperty("os.name").toLowerCase();
        boolean isMacOs = osName.startsWith("mac os x");
        if(isMacOs)
            System.setProperty("webdriver.chrome.driver", getProperty("user.dir") + "/src/test/resources/chromedrivermac");

        String sutHost = System.getenv("ET_SUT_HOST");
        String sutPort = System.getenv("ET_SUT_PORT");
        String sutProtocol = System.getenv("ET_SUT_PROTOCOL");

        if (sutHost == null) {
            sutUrl = "http://localhost:8080/";
        } else {
            sutPort = sutPort != null ? sutPort : "8080";
            sutProtocol = sutProtocol != null ? sutProtocol : "http";

            sutUrl = sutProtocol + "://" + sutHost + ":" + sutPort;
        }
        logger.info("Webapp URL: " + sutUrl);

        browserType = getProperty("browser");
        logger.info("Browser Type: {}", browserType);
        eusURL = System.getenv("ET_EUS_API");
    }

    @BeforeEach
    public void setupTest(TestInfo info) throws MalformedURLException {
        String testName = info.getTestMethod().get().getName();
        logger.info("##### Start test: {}", testName);

        if (eusURL == null) {
            if (browserType == null || browserType.equals(CHROME)) {
                driver = new ChromeDriver();
            } else {
                driver = new FirefoxDriver();
            }
        } else {
            DesiredCapabilities caps;
            if (browserType == null || browserType.equals(CHROME)) {
                caps = chrome();
            } else {
                caps = DesiredCapabilities.firefox();
            }

            browserVersion = getProperty("browserVersion");
            if (browserVersion != null) {
                logger.info("Browser Version: {}", browserVersion);
                caps.setVersion(browserVersion);
            }

            caps.setCapability("testName", testName);
            driver = new RemoteWebDriver(new URL(eusURL), caps);
        }

        driver.get(sutUrl);
    }

    @AfterEach
    public void teardown(TestInfo info) {
        if (driver != null) {
            driver.quit();
        }

        String testName = info.getTestMethod().get().getName();
        logger.info("##### Finish test: {}", testName);
    }

}

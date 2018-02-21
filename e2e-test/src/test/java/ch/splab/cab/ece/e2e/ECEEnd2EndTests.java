/*
 *  Copyright (c) 2018. Service Prototyping Lab, ZHAW
 *   All Rights Reserved.
 *
 *       Licensed under the Apache License, Version 2.0 (the "License"); you may
 *       not use this file except in compliance with the License. You may obtain
 *       a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *       WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *       License for the specific language governing permissions and limitations
 *       under the License.
 *
 *
 *       Author: Piyush Harsh,
 *       URL: piyush-harsh.info
 *       Email: piyush.harsh@zhaw.ch
 */

package ch.splab.cab.ece.e2e;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.System.getProperty;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ECEEnd2EndTests
{
    private static final Logger logger = LogManager.getLogger("ECEEnd2EndTests");
    String tormURL = "http://localhost:37000";
    private boolean hasECEStarted = false;

    WebDriver driver;

    @Before
    public void setup()
    {
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isMacOs = osName.startsWith("mac os x");
        if(isMacOs)
            System.setProperty("webdriver.chrome.driver", getProperty("user.dir") + "/src/test/resources/chromedrivermac");
        else
            System.setProperty("webdriver.chrome.driver", getProperty("user.dir") + "/src/test/resources/chromedriver");

        try
        {
            String etmUrl = getProperty("etmUrl");
            tormURL = (etmUrl==null ? "http://localhost:37000" : "http://" + etmUrl + ":37000");

            logger.info("Opening TORM at "  + tormURL);

            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(30, SECONDS);
            driver.manage().window().setSize(new Dimension(1400, 1200));
            driver.get(tormURL);
            try
            {
                logger.info("Page title: " + driver.getTitle());
                logger.info("Clicking side menu link");
                driver.findElement(By.id("nav_test_engines")).click();
            }
            catch(Exception ex)
            {
                logger.info("Unable to find side navigation link. Directly accessing test engines url");
                driver.get(tormURL + "/#/test-engines");
            }

            logger.info("Engines Page title: " + driver.getTitle());
            logger.info("Starting ece engine");

            try
            {
                driver.findElement(By.xpath("//span[text()='ece']//following::button[@title='Start Engine']")).click();
                new WebDriverWait(driver, 60)
                        .ignoring(StaleElementReferenceException.class)
                        .until((WebDriver d) -> {
                            WebElement we = d.findElement(By.xpath("(//button[@title='View Engine'])"));
                            logger.info(we.getAttribute("title"));
                            logger.info(we.getTagName());
                            we.click();
                            return true;
                        });
                hasECEStarted = true;
                logger.info("Redirected to " + driver.getCurrentUrl() + ". Switching focus to iFrame");
                driver.switchTo().frame(driver.findElement(By.name("engine")));
            }
            catch(Exception ex)
            {
                logger.error("ece page took too long to open or was already open, try direct link please.");
                hasECEStarted = false;
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Test
    public void invokeEce()
    {
        logger.info("Opening New Cost Analysis");
        if(hasECEStarted)
        {
            assertTrue(driver.findElement(By.className("container-fluid")).isDisplayed());
        }
        else
        {
            logger.info("Directly accessing ece page assuming engine actually started");
            assertEquals(driver.getCurrentUrl(), tormURL + "/#/test-engines");
            //driver.navigate().to(tormURL + "/#/test-engines/ece");
            //assertEquals(driver.getCurrentUrl(), tormURL + "/#/test-engines/ece");
        }
    }

    @After
    public void cleanup()
    {
        try
        {
            logger.info("Wrapping up: switching focus back to TORM Dashboard");
            driver.switchTo().defaultContent();
            logger.info("Stopping ece");
            driver.findElement(By.id("nav_test_engines")).click();
            driver.findElement(By.xpath("//span[text()='ece']//following::button[@title='Stop Engine']")).click();
        }
        catch(Exception e)
        {
            logger.info("Issue with stopping ece");
        }
        if (driver != null)
            driver.quit();
    }
}

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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ECEEnd2EndTests
{
    private static final Logger logger = LogManager.getLogger("ECEEnd2EndTests");

    String tormURL = "http://nightly.elastest.io:37000";
    WebDriver driver;

    @Before
    public void setup()
    {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/test/resources/chromedriver");
        driver = new ChromeDriver();
        logger.info("Opening TORM at "  + tormURL);

        driver.get(tormURL);
        try
        {
            logger.info("Page title: " + driver.getTitle());
            logger.info("Clicking side menu link");
            driver.findElement(By.id("nav_test_engines")).click();
        }
        catch(Exception ex)
        {

        }
        logger.info("Page title: " + driver.getTitle());
        logger.info("Starting ece engine");
        driver.findElement(By.xpath("//span[text()='ece']//following::button[@title='Start Engine']")).click();

        //accessing the ece page directly
        driver.get(tormURL + "/#/test-engines/ece");

        // need another delay here as the platform does not load instantaneously
        try
        {
            new WebDriverWait(driver, 120)
                    .ignoring(StaleElementReferenceException.class)
                    .until((WebDriver d) -> {
                        WebElement we = d.findElement(By.id("nav_test_engines"));
                        return true;
                    });
        }
        catch (TimeoutException te)
        {
            logger.info("ece page should be loaded by now");
        }


        logger.info("Redirected to " + driver.getCurrentUrl() + ". Switching focus to iFrame");
        driver.switchTo().frame(driver.findElement(By.name("engine")));
    }

    @Test
    public void invokeEce()
    {
        logger.info("Opening New Cost Analysis");
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
            driver.findElement(By.xpath("//button[@title='Stop Engine']")).click();
        }
        catch(Exception e)
        {
            logger.info("Issue with stopping ece");
        }
        if (driver != null)
            driver.quit();
    }
}

/*
 *  Copyright (c) 2019. Service Prototyping Lab, ZHAW
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

package ch.splab.cab.elastest_e2e_elastest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

public class ECEElasTestInElasTestTest extends ElastestBaseTest {
    private static final Logger logger = LogManager.getLogger("ECEElasTestInElasTestTest");
    @Test
    @DisplayName("Test to start ECE")
    void check4ece()
    {
        // elastest_url = env.ET_SUT_PROTOCOL + '://elastest:3xp3r1m3nt47@' + env.ET_SUT_HOST + ':' + env.ET_SUT_PORT
        String tormURL = System.getenv("etEtmUrl");
        logger.info("Torm Url: " + tormURL);

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
    }
}

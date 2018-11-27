/*
 *
 *  Copyright (c) 2018. Service Prototyping Lab, ZHAW
 *   All Rights Reserved.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License"); you may
 *      not use this file except in compliance with the License. You may obtain
 *      a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *      WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *      License for the specific language governing permissions and limitations
 *      under the License.
 *
 *
 *      Author: Piyush Harsh,
 *      URL: piyush-harsh.info
 *      Email: piyush.harsh@zhaw.ch
 *
 */

package ch.splab.cab.costengine;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class AppConfiguration {
    final static Logger logger = Logger.getLogger(AppConfiguration.class);

    @Value("${ET_ETM_API}")
    String etmAPI;

    @Value("${ET_ESM_API}")
    String esmAPI;

    @Value("${ET_EMP_API}")
    String empAPI;

    @Value("${ET_EMP_APIKEY}")
    String empAPIkey;

    @Value("${ET_EMP_USER}")
    String empUser;

    @Value("${ET_EMP_TOPIC}")
    String empTopic;

    @Value("${ET_EMP_SERIES}")
    String empSeries;

    private static String ET_ETM_API;
    private static String ET_ESM_API;
    private static String ET_EMP_API;
    private static String ET_EMP_APIKEY;
    private static String ET_EMP_USER;
    private static String ET_EMP_TOPIC;
    private static String ET_EMP_SERIES;

    @PostConstruct
    public void init() {
        Map<String, String> env = System.getenv();
        if(env.containsKey("ET_ETM_API")) {
            logger.info("loading etm api value from environment: " + env.get("ET_ETM_API"));
            ET_ETM_API = env.get("ET_ETM_API");
        }
        else {
            logger.info("loading etm api value from application.properties: " + etmAPI);
            ET_ETM_API = etmAPI;
        }

        if(env.containsKey("ET_ESM_API")) {
            logger.info("loading esm api value from environment: " + env.get("ET_ESM_API"));
            ET_ESM_API = env.get("ET_ESM_API");
        }
        else {
            logger.info("loading esm api value from application.properties: " + esmAPI);
            ET_ESM_API = esmAPI;
        }

        if(env.containsKey("ET_EMP_API")) {
            logger.info("loading emp api value from environment: " + env.get("ET_EMP_API"));
            ET_EMP_API = env.get("ET_EMP_API");
        }
        else {
            logger.info("loading emp api value from application.properties: " + empAPI);
            ET_EMP_API = empAPI;
        }

        if(env.containsKey("ET_EMP_APIKEY")) {
            logger.info("loading emp api key value from environment: " + env.get("ET_EMP_APIKEY"));
            ET_EMP_APIKEY = env.get("ET_EMP_APIKEY");
        }
        else {
            logger.info("loading emp api key value from application.properties: " + empAPIkey);
            ET_EMP_APIKEY = empAPIkey;
        }

        if(env.containsKey("ET_EMP_USER")) {
            logger.info("loading emp user from environment: " + env.get("ET_EMP_USER"));
            ET_EMP_USER = env.get("ET_EMP_USER");
        }
        else {
            logger.info("loading emp user from application.properties: " + empUser);
            ET_EMP_USER = empUser;
        }

        if(env.containsKey("ET_EMP_TOPIC")) {
            logger.info("loading emp topic from environment: " + env.get("ET_EMP_TOPIC"));
            ET_EMP_TOPIC = env.get("ET_EMP_TOPIC");
        }
        else {
            logger.info("loading emp topic from application.properties: " + empTopic);
            ET_EMP_TOPIC = empTopic;
        }

        if(env.containsKey("ET_EMP_SERIES")) {
            logger.info("loading emp series from environment: " + env.get("ET_EMP_SERIES"));
            ET_EMP_SERIES = env.get("ET_EMP_SERIES");
        }
        else {
            logger.info("loading emp series from application.properties: " + empSeries);
            ET_EMP_SERIES = empSeries;
        }
    }

    public static String getETMApi()
    {
        return ET_ETM_API;
    }

    public static String getESMApi()
    {
        return ET_ESM_API;
    }

    public static String getEMPApi()
    {
        return ET_EMP_API;
    }

    public static String getEMPApikey()
    {
        return ET_EMP_APIKEY;
    }

    public static String getEMPUser()
    {
        return ET_EMP_USER;
    }

    public static String getEMPTopic()
    {
        return ET_EMP_TOPIC;
    }

    public static String getEMPSeries()
    {
        return ET_EMP_SERIES;
    }
}

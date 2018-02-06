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

    private static String ET_ETM_API;
    private static String ET_ESM_API;

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
    }

    public static String getETMApi()
    {
        return ET_ETM_API;
    }

    public static String getESMApi()
    {
        return ET_ESM_API;
    }
}

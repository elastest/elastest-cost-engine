/*
 * Copyright (c) 2015. Zuercher Hochschule fuer Angewandte Wissenschaften
 *  All Rights Reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License"); you may
 *     not use this file except in compliance with the License. You may obtain
 *     a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *     WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *     License for the specific language governing permissions and limitations
 *     under the License.
 */
package io.elastest.ece.load;

import io.elastest.ece.load.model.ElasTestSettings;
import io.elastest.ece.load.model.EstimationSettings;
import io.elastest.ece.load.model.HibernateCredentials;

import java.util.*;

/**
 * Copyright (c) 2015. Zuercher Hochschule fuer Angewandte Wissenschaften
 * All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 * <p>
 * Created by Manu Perez on 28/07/16.
 */
public class Settings {


    // List of different settings that are being loaded from configuration file
    protected HibernateCredentials hibernateCredentials;
    protected ElasTestSettings elasTestSettings;
    protected EstimationSettings estimationSettings;
    // Object for reading and accessing configuration properties
    private Properties properties;

    /**
     * Load settings based on provided settings
     */
    public Settings(Properties prop) {
        properties = prop;
    }

    //=============== Hibernate credentials and settings

    /**
     * Load Estimation Settings
     *
     * @return etimationSettings
     */
    private EstimationSettings loadEstimationSettings() {
        EstimationSettings estimationSettings = new EstimationSettings();
        Map<String, String> env = System.getenv();

        if (env.containsKey("ESTIMATIONRANGE")) {
            List range = new ArrayList<>();
            List intRange = new ArrayList<>();
            range.addAll(Arrays.asList(env.get("ESTIMATIONRANGE").split(",")));
            for (int i = 0; i < range.size(); i++) {
                intRange.add(Integer.parseInt((String) range.get(i)));
            }
            estimationSettings.setEstimationRange(intRange);
        } else {
            List range = new ArrayList<>();
            List intRange = new ArrayList<>();
            range.addAll(Arrays.asList(properties.getProperty("EstimationRange").split(",")));
            for (int i = 0; i < range.size(); i++) {
                intRange.add(Integer.parseInt((String) range.get(i)));
            }
            estimationSettings.setEstimationRange(intRange);
        }

        return estimationSettings;
    }

    /**
     * Access loaded Estimation settings
     *
     * @return estimation settings
     */
    public EstimationSettings getEstimationSettings() {

        if (estimationSettings == null) {
            estimationSettings = loadEstimationSettings();
        }

        return estimationSettings;
    }

    /**
     * Load Hibernate credentials
     *
     * @return credentials
     */
    private HibernateCredentials loadHibernateConfiguration() {
        HibernateCredentials hibernateCredentials = new HibernateCredentials();
        Map<String, String> env = System.getenv();

        if (env.containsKey("HIBERNATEURL"))
            hibernateCredentials.setHibernateURL(env.get("HIBERNATEURL"));
        else
            hibernateCredentials.setHibernateURL(properties.getProperty("HibernateURL"));

        if (env.containsKey("HIBERNATEUSERNAME"))
            hibernateCredentials.setHibernateUsername(env.get("HIBERNATEUSERNAME"));
        else
            hibernateCredentials.setHibernateUsername(properties.getProperty("HibernateUsername"));

        if (env.containsKey("HIBERNATEPASSWORD"))
            hibernateCredentials.setHibernatePassword(env.get("HIBERNATEPASSWORD"));
        else
            hibernateCredentials.setHibernatePassword(properties.getProperty("HibernatePassword"));

        if (env.containsKey("HIBERNATEDRIVER"))
            hibernateCredentials.setHibernateDriver(env.get("HIBERNATEDRIVER"));
        else
            hibernateCredentials.setHibernateDriver(properties.getProperty("HibernateDriver"));

        if (env.containsKey("HIBERNATEDIALECT"))
            hibernateCredentials.setHibernateDialect(env.get("HIBERNATEDIALECT"));
        else
            hibernateCredentials.setHibernateDialect(properties.getProperty("HibernateDialect"));

        return hibernateCredentials;
    }

    /**
     * Access loaded Hibernate credentials
     *
     * @return server settings
     */
    public HibernateCredentials getHibernateCredentials() {

        if (hibernateCredentials == null) {
            hibernateCredentials = loadHibernateConfiguration();
        }

        return hibernateCredentials;
    }

    /**
     * Load ElasTest Settings
     *
     * @return
     */
    private ElasTestSettings loadElasTestSettings() {
        ElasTestSettings elasTestSettings = new ElasTestSettings();
        Map<String, String> env = System.getenv();

        if (env.containsKey("ET_ETM_API"))
            elasTestSettings.setElasTestTormAPI(env.get("ET_ETM_API"));
        else
            elasTestSettings.setElasTestTormAPI((String) properties.get("ElasTestTormAPI"));

        if (env.containsKey("ET_ESM_API"))
            elasTestSettings.setElasTestESMAPI(env.get("ET_ESM_API"));
        else
            elasTestSettings.setElasTestESMAPI((String) properties.get("ElasTestESMAPI"));

        if (env.containsKey("ELASTESTTORMTJOBENDPOINT"))
            elasTestSettings.setElasTestTormTJobEndpoint(env.get("ELASTESTTORMTJOBENDPOINT"));
        else
            elasTestSettings.setElasTestTormTJobEndpoint((String) properties.get("ElasTestTormTJobEndpoint"));

        if (env.containsKey("ELASTESTESMCATALOGENDPOINT"))
            elasTestSettings.setElasTestESMCatalogEndpoint(env.get("ELASTESTESMCATALOGENDPOINT"));
        else
            elasTestSettings.setElasTestESMCatalogEndpoint((String) properties.get("ElasTestESMCatalogEndpoint"));

        if (env.containsKey("ELASTESTESMINSTANCEENDPOINT"))
            elasTestSettings.setElasTestESMInstanceEndpoint(env.get("ELASTESTESMINSTANCEENDPOINT"));
        else
            elasTestSettings.setElasTestESMInstanceEndpoint((String) properties.get("ElasTestESMInstanceEndpoint"));

        return elasTestSettings;
    }

    /**
     * Access loaded ElasTest credentials
     *
     * @return elastest settings
     */
    public ElasTestSettings getElastestSettings() {

        if (elasTestSettings == null) {
            elasTestSettings = loadElasTestSettings();
        }

        return elasTestSettings;
    }
}

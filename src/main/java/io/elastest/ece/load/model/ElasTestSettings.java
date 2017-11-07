package io.elastest.ece.load.model;

/**
 * Copyright (c) 2017. Zuercher Hochschule fuer Angewandte Wissenschaften
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
 * Created by Manu Perez on 07/11/17.
 */

public class ElasTestSettings {
    private String ElasTestTormAPI;
    private String ElasTestTormTJobEndpoint;
    private String ElasTestESMAPI;

    public String getElasTestTormTJobEndpoint() {
        return ElasTestTormTJobEndpoint;
    }
    public void setElasTestTormTJobEndpoint(String elasTestTormTJobEndpoint) {
        ElasTestTormTJobEndpoint = elasTestTormTJobEndpoint;
    }

    public String getElasTestTormAPI() {
        return ElasTestTormAPI;
    }
    public void setElasTestTormAPI(String elasTestTormAPI) {
        ElasTestTormAPI = elasTestTormAPI;
    }

    public String getElasTestESMAPI() {
        return ElasTestESMAPI;
    }
    public void setElasTestESMAPI(String elasTestESMAPI) {
        ElasTestESMAPI = elasTestESMAPI;
    }
}

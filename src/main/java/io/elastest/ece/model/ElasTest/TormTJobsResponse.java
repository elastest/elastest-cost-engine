package io.elastest.ece.model.ElasTest;

import java.util.List;
import java.util.Map;

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

public class TormTJobsResponse {
    private String commands;
    private String esmServicesString;
    private String execDashboardConfig;
    private String getExecDashboardConfigPath;
    private Boolean external;
    private int id;
    private String imageName;
    private String name;
    private List<Map> parameters;
    private Object project;
    private String resultsPath;
    private Object sut;
    private List tjobExecs;

    public TormTJobsResponse() {
    }

    public String getCommands() {
        return commands;
    }
    public void setCommands(String commands) {
        this.commands = commands;
    }

    public String getEsmServicesString() {
        return esmServicesString;
    }
    public void setEsmServicesString(String esmServicesString) {
        this.esmServicesString = esmServicesString;
    }

    public String getExecDashboardConfig() {
        return execDashboardConfig;
    }
    public void setExecDashboardConfig(String execDashboardConfig) {
        this.execDashboardConfig = execDashboardConfig;
    }

    public String getGetExecDashboardConfigPath() {
        return getExecDashboardConfigPath;
    }
    public void setGetExecDashboardConfigPath(String getExecDashboardConfigPath) {
        this.getExecDashboardConfigPath = getExecDashboardConfigPath;
    }

    public Boolean getExternal() {
        return external;
    }
    public void setExternal(Boolean external) {
        this.external = external;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Map> getParameters() {
        return parameters;
    }
    public void setParameters(List<Map> parameters) {
        this.parameters = parameters;
    }

    public Object getProject() {
        return project;
    }
    public void setProject(Object project) {
        this.project = project;
    }

    public String getResultsPath() {
        return resultsPath;
    }
    public void setResultsPath(String resultsPath) {
        this.resultsPath = resultsPath;
    }

    public Object getSut() {
        return sut;
    }
    public void setSut(Object sut) {
        this.sut = sut;
    }

    public List getTjobExecs() {
        return tjobExecs;
    }
    public void setTjobExecs(List tjobExecs) {
        this.tjobExecs = tjobExecs;
    }
}

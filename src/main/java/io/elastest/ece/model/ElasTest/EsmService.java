package io.elastest.ece.model.ElasTest;

import java.util.List;

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
 * Created by Manu Perez on 09/11/17.
 */

public class EsmService {
    private String id;
    private String name;
    private String short_name;
    private String description;
    private Boolean bindable;
    private List<String> tags;
    private EsmMetadata metadata;
    private List<String> requires;
    private Boolean plan_updateable;
    private List<EsmPlan> plans;
    private EsmDashboardClient dashboard_client;

    public EsmService() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getBindable() {
        return bindable;
    }

    public void setBindable(Boolean bindable) {
        this.bindable = bindable;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public EsmMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(EsmMetadata metadata) {
        this.metadata = metadata;
    }

    public List<String> getRequires() {
        return requires;
    }

    public void setRequires(List<String> requires) {
        this.requires = requires;
    }

    public Boolean getPlan_updateable() {
        return plan_updateable;
    }

    public void setPlan_updateable(Boolean plan_updateable) {
        this.plan_updateable = plan_updateable;
    }

    public List<EsmPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<EsmPlan> plans) {
        this.plans = plans;
    }

    public EsmDashboardClient getDashboard_client() {
        return dashboard_client;
    }

    public void setDashboard_client(EsmDashboardClient dashboard_client) {
        this.dashboard_client = dashboard_client;
    }
}

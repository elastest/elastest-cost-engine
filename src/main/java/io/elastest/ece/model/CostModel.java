package io.elastest.ece.model;

import javax.persistence.*;
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
 * Created by Manu Perez on 13/07/17.
 */

@Entity
public class CostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String type;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Double> fix_cost;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Double> var_rate;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Object> components;

    private String description;

    protected CostModel() {
    }

    public CostModel(String name, String type, Map fix_cost) {
        this.name = name;
        this.type = type;
        this.fix_cost = fix_cost;
    }

    public CostModel(String name, String type, Map<String, Double> fix_cost, Map<String, Double> var_rate, Map<String, Object> components, String description) {
        this.name = name;
        this.type = type;
        this.fix_cost = fix_cost;
        this.var_rate = var_rate;
        this.components = components;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Double> getFix_cost() {
        return fix_cost;
    }

    public void setFix_cost(Map<String, Double> fix_cost) {
        this.fix_cost = fix_cost;
    }

    public Map<String, Double> getVar_rate() {
        return var_rate;
    }

    public void setVar_rate(Map<String, Double> var_rate) {
        this.var_rate = var_rate;
    }

    public Map<String, Object> getComponents() {
        return components;
    }

    public void setComponents(Map<String, Object> components) {
        this.components = components;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CostModel)) {
            return false;
        }
        CostModel costModel = (CostModel) o;
        return (costModel.getName().equals(((CostModel) o).getName())
                && costModel.getType().equals(((CostModel) o).getType())
                && costModel.getVar_rate().equals(((CostModel) o).getVar_rate())
                && costModel.getFix_cost().equals(((CostModel) o).getFix_cost())
                && costModel.getDescription().equals(((CostModel) o).getDescription()));
    }
}

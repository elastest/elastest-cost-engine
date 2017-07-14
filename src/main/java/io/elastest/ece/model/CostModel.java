package io.elastest.ece.model;

import javax.persistence.*;
import java.util.HashMap;
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
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String type;

//    @OneToMany(cascade = CascadeType.ALL)
//    @MapKey(name = "cost")
//    private Map<String,CostItem> cost;

    private Map<String,Double> cost;

    @Column(columnDefinition = "TEXT")
    private String description;

    public CostModel(String name, String type, Map cost) {
        this.name = name;
        this.type = type;
        this.cost = cost;
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

    public Map getCost() {
        return cost;
    }

    public void setCost(HashMap cost) {
        this.cost = cost;
    }
}

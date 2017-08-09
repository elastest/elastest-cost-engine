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
public class TJob {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

//    @OneToMany(cascade = CascadeType.ALL)
//    @MapKey(name = "quantity")
//    private Map<String, TJobItem> metadata;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> metadata;

    @Column(columnDefinition = "TEXT")
    private String description;

    public TJob() {
    }

    public TJob(String name, Map metadata) {
        this.name = name;
        this.metadata = metadata;
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

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}

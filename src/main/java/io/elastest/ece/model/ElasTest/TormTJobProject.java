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
 * Created by Manu Perez on 07/11/17.
 */

public class TormTJobProject {

    private Integer id;
    private String name;
    private List suts;
    private List tjobs;

    public TormTJobProject() {
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List getSuts() {
        return suts;
    }
    public void setSuts(List suts) {
        this.suts = suts;
    }

    public List getTjobs() {
        return tjobs;
    }
    public void setTjobs(List tjobs) {
        this.tjobs = tjobs;
    }
}

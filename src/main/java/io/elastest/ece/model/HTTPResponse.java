package io.elastest.ece.model;

import com.google.gson.Gson;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
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

public class HTTPResponse {
    private String object;

    public HTTPResponse(String obj) {
        object = obj;
    }

    public String getAsString() throws Exception {
        return object;
    }

    public List getAsList() throws Exception {
        return new Gson().fromJson(object, List.class);
    }

    public Map getAsMap() throws Exception {
        return new Gson().fromJson(object, Map.class);
    }

    public Object getAsClass(Class clazz) throws Exception {
        return new Gson().fromJson(object, clazz);
    }

    public List getAsListOfType(Class clazz) throws Exception {
        List<Map> list = new Gson().fromJson(object, List.class);
        return ParseQueryResult(list, clazz);
    }

    private List ParseQueryResult(List<Map> list, Class clazz){
        List<Object> mapped = new ArrayList<>();

        // iterate and map those objects
        if (list != null) {
            for (Map map : list) {
                try {
                    Object bean = clazz.newInstance();

                    // map HashMap to POJO
                    BeanUtils.populate(bean, map);

                    // add it to list of mapped CDRs
                    mapped.add(bean);

                } catch (Exception ignored) {
                    return null;
                }
            }
        }

        return mapped;
    }
}

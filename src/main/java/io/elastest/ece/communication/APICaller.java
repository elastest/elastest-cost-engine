package io.elastest.ece.communication;

import com.google.gson.Gson;
import io.elastest.ece.model.HTTPResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URL;

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


public class APICaller {

    /**
     * Perform POST query and return Response
     * @param endpoint to be called
     * @param object to be passed
     * @return Response object
     * @throws Exception
     */
    public HTTPResponse post(URL endpoint, Object object) throws Exception {
        // prepare connection
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().build();

        // create request
        HttpPost request = new HttpPost(endpoint.toURI());
        StringEntity entity = new StringEntity(new Gson().toJson(object));
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(entity);

        // execute response
        org.apache.http.HttpResponse response = client.execute(request);
        return new HTTPResponse(IOUtils.toString(response.getEntity().getContent()));
    }

    /**
     * Perform GET query and return Response
     * @param endpoint to be called
     * @return Response object
     * @throws Exception
     */
    public HTTPResponse get(URL endpoint) throws Exception {
        // prepare connection
        HttpClient client = HttpClientBuilder.create().build();

        // create request
        HttpGet request = new HttpGet(endpoint.toURI());
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/json");

        // execute response
        HttpResponse response = client.execute(request);
        return new HTTPResponse(IOUtils.toString(response.getEntity().getContent()));
    }
}
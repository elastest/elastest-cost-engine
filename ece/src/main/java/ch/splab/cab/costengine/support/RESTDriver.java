/*
 *
 *  Copyright (c) 2018. Service Prototyping Lab, ZHAW
 *   All Rights Reserved.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License"); you may
 *      not use this file except in compliance with the License. You may obtain
 *      a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *      WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *      License for the specific language governing permissions and limitations
 *      under the License.
 *
 *
 *      Author: Piyush Harsh,
 *      URL: piyush-harsh.info
 *      Email: piyush.harsh@zhaw.ch
 *
 */

package ch.splab.cab.costengine.support;

import okhttp3.*;
import okhttp3.Request.Builder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

public class RESTDriver {
    final static Logger logger = Logger.getLogger(RESTDriver.class);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Response doGet(String url, Map<String, String> headers)
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, "");
        Builder requestBuilder = new Builder();
        requestBuilder.url(url);
        requestBuilder.header("User-Agent", "OkHttp Headers.java");

        if(headers != null)
            for(String key:headers.keySet())
            {
                requestBuilder.addHeader(key, headers.get(key));
            }

        Request request = requestBuilder.get().build();

        try
        {
            Response response = client.newCall(request).execute();
            logger.info("GET response-code: " + response.code() + " : URL: " + url);
            return response;
        }
        catch(IOException ioex)
        {
            ioex.printStackTrace();
        }
        return null;
    }
}

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
package support;

import ch.splab.cab.costengine.AppConfiguration;
import ch.splab.cab.costengine.support.RESTDriver;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfiguration.class, loader=AnnotationConfigContextLoader.class)
public class RESTDriverTest {
    @Test
    public void testRESTDriver4ETM()
    {
        Response restResponse = RESTDriver.doGet(AppConfiguration.getETMApi() + "api/tjob", null);
        assertEquals(restResponse.code(), 200);
    }

    @Test
    public void testRESTDriver4ESM()
    {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X_Broker_Api_Version", "2.12");
        Response restResponse = RESTDriver.doGet(AppConfiguration.getESMApi() + "v2/catalog", headers);
        assertEquals(restResponse.code(), 200);
    }
}

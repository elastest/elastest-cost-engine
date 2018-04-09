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

import ch.splab.cab.costengine.AppConfiguration;
import ch.splab.cab.costengine.Controller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfiguration.class, loader=AnnotationConfigContextLoader.class)
public class ControllerTest {

    Model model = new Model() {
        @Override
        public Model addAttribute(String s, Object o) {
            return null;
        }

        @Override
        public Model addAttribute(Object o) {
            return null;
        }

        @Override
        public Model addAllAttributes(Collection<?> collection) {
            return null;
        }

        @Override
        public Model addAllAttributes(Map<String, ?> map) {
            return null;
        }

        @Override
        public Model mergeAttributes(Map<String, ?> map) {
            return null;
        }

        @Override
        public boolean containsAttribute(String s) {
            return false;
        }

        @Override
        public Map<String, Object> asMap() {
            return null;
        }
    };


    @Test
    public void showIndexTest()
    {
        Controller controller = new Controller();
        String value = controller.showIndex(null, null, model);
        assertEquals("index", value);
    }

    @Test
    public void showStaticAnalysisTest()
    {
        Controller controller = new Controller();
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        req.addParameter("tjobid", "1");
        req.addParameter("tjobname", "Teacher and Student Testing");
        req.addParameter("tjobservices", "[{\"id\": \"29216b91-497c-43b7-a5c4-6613f13fa0e9\",\"name\": \"EUS\",\"selected\": true,\"config\":{\"webRtcStats\":{\"name\": \"webRtcStats\",\"type\": \"boolean\",\"label\": \"Gather WebRTC Statistics\",\"default\": false,\"value\": false}}}]");
        String value = controller.showStaticAnalysis(req, response, model);
        assertEquals("staticanalysis", value);
    }
}

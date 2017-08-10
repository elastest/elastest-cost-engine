package elastest.io.ece;

import io.elastest.ece.application.APIController;
import io.elastest.ece.model.CostModel;
import io.elastest.ece.persistance.HibernateClient;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;

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
 * Created by Manu Perez on 21/06/17.
 */

public class APIControllerTest extends TestCase {

    APIController apiController;

    /**
     * @return the suite of tests being tested
     */
    public static junit.framework.Test suite() {
        return new TestSuite(APIControllerTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

//    HibernateClient hibernateClient = HibernateClient.getInstance();
//
//    @PostConstruct
//    public void init(){
//        CostModel costModel = new CostModel("costModel0", "ONDEMAND", null, null, null, "");
//        hibernateClient.persistObject(costModel);
//    }
//
//    @Test
//    public void testGetCostModel() {
//        apiController = new APIController();
//
//    assertEquals(apiController.getCostModel("costModel0", null), "redirection");
//    }
//
//    @Test
//    public void testDeleteCostModel() {
//        apiController = new APIController();
//
//        assertEquals(apiController.deleteCostModel("costModel0"), "redirection");
//    }
//
//    @Test
//    public void testAddCostModel(String name, String description, String fixName, Double fixValue, double cpus, double memory,double disk, Model model){
//        CostModel costModel = new CostModel
//
//    }
}
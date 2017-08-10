package elastest.io.ece;

import io.elastest.ece.application.APIController;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

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

//    @Test
//    public void testGetIndex() {
//        apiController = new APIController();
//
//        assertEquals(apiController.getIndex(null), "redirection");
//    }

//    @Test
//    public void testAddCostModel() {
//        apiController = new APIController();
//
//        assertEquals(apiController.addCostModel("", 0.0, 0.0, 0.0, null), "redirection");
//    }

    @Test
    public void testGetCostModel() {
        apiController = new APIController();

        assertEquals(apiController.getCostModel("model0", null), "redirection");
    }

    @Test
    public void testDeleteCostModel() {
        apiController = new APIController();

        assertEquals(apiController.deleteCostModel("model0"), "redirection");
    }

    @Test
    public void testEstimate() {
        apiController = new APIController();

        assertEquals(apiController.estimate("test0", "model0", null), "redirection");
    }

    @Test
    public void testPostEstimate() {
        apiController = new APIController();

        assertEquals(apiController.estimate("", null, null), "redirection");
    }

//    @Test
//    public void testGetStatus() {
//        apiController = new APIController();
//
//        assertEquals(apiController.getStatus("SuT0", null), "redirection", null);
//    }
//
//    @Test
//    public void testgetCost() {
//        apiController = new APIController();
//
//        assertEquals(apiController.getCost("SuT0", null), "redirection", null);
//    }


}
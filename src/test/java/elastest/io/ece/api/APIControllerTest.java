package elastest.io.ece.api;

import io.elastest.ece.application.APIController;
import io.elastest.ece.load.Loader;
import io.elastest.ece.load.model.HibernateCredentials;
import io.elastest.ece.model.CostModel;
import io.elastest.ece.model.TJob;
import io.elastest.ece.persistance.HibernateClient;
import io.elastest.ece.persistance.HibernateConfiguration;
import io.elastest.ece.persistance.QueryHelper;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ExtendedModelMap;

import java.util.HashMap;
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
 * Created by Manu Perez on 21/06/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
@PropertySource("classpath:application.properties")
public class APIControllerTest extends TestCase {

    private APIController apiController;
    private HibernateClient hibernateClient;
    private CostModel costModel;
    private TJob tJob;

    /**
     * @return the suite of tests being tested
     */
    public static junit.framework.Test suite() {
        return new TestSuite(APIControllerTest.class);
    }

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(APIControllerTest.class.getName());

    public void setup(){
        try {
            Loader.createInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Setting up the Database connection.");
        checkAndConfigureHibernate();
        hibernateClient = HibernateClient.getInstance();
        List<CostModel> oldCostModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));
        for(CostModel costModel : oldCostModels){
            hibernateClient.deleteObject(costModel);
        }

        List<TJob> oldTjobs = hibernateClient.executeQuery(QueryHelper.createListQuery(TJob.class));
        for(TJob tJob : oldTjobs){
            hibernateClient.deleteObject(tJob);
        }

        logger.info("Creating Demo T-Job Values.");
        HashMap<String, String> test0Map = new HashMap<>();
        test0Map.put("cpus", "8.0");
        test0Map.put("memory", "20.0");
        test0Map.put("disk", "500.0");

        this.tJob = new TJob("test0 8 cores, 20gb ram, 500gb disk ", test0Map);
        this.hibernateClient.persistObject(tJob);

        logger.info("Creating Demo Cost Model Values.");
        HashMap varCosts = new HashMap();
        varCosts.put("cpus", 0.0);
        varCosts.put("memory", 0.0);
        varCosts.put("disk", 0.0);
        HashMap fixCost = new HashMap();
        fixCost.put("deployment", 0.0);

        this.costModel = new CostModel("Free Cost Model", "ONDEMAND", fixCost, varCosts, null, "Everything for free, test purposes");
        this.hibernateClient.persistObject(costModel);
    }

    @Test
    public void testGetIndex() {
        setup();
        apiController = new APIController();

        assertEquals("index", apiController.getIndex(new ExtendedModelMap()));
    }

    @Test
    public void testAddCostModel() {
        setup();
        apiController = new APIController();
        String name = costModel.getName();
        String description = costModel.getDescription();
        String fixName = (String) costModel.getFix_cost().keySet().toArray()[0];
        Double fixValue = costModel.getFix_cost().get(fixName);
        double cpus = costModel.getVar_rate().get("cpus");
        double memory = costModel.getVar_rate().get("memory");
        double disk = costModel.getVar_rate().get("disk");

        apiController.addCostModel(name, description, fixName, fixValue, cpus, memory, disk, new ExtendedModelMap());
        List<CostModel> costModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));

        assertTrue(costModels.contains(costModel));
    }

    @Test
    public void testGetCostModel() {
        setup();
        apiController = new APIController();
        String name = costModel.getName();
        String description = costModel.getDescription();
        String fixName = (String) costModel.getFix_cost().keySet().toArray()[0];
        Double fixValue = costModel.getFix_cost().get(fixName);
        double cpus = costModel.getVar_rate().get("cpus");
        double memory = costModel.getVar_rate().get("memory");
        double disk = costModel.getVar_rate().get("disk");

        apiController.addCostModel(name, description, fixName, fixValue, cpus, memory, disk, new ExtendedModelMap());
        List<CostModel> costModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));
        int index = costModels.indexOf(costModel);
        Long id = costModels.get(index).getId();
        CostModel obtained = (CostModel) hibernateClient.getObject(CostModel.class, id);

        assertEquals(obtained, costModel);
    }

    @Test
    public void testDeleteCostModel() {
        setup();
        apiController = new APIController();
        List<CostModel> costModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));
        int index = costModels.indexOf(costModel);
        Long id = costModels.get(index).getId();

        apiController.deleteCostModel(String.valueOf(id));

        assertEquals(null, hibernateClient.getObject(CostModel.class, id));
    }

    @Test
    public void testEstimatePost() {
        setup();
        apiController = new APIController();

        List<CostModel> costModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));
        int costModelIndex = costModels.indexOf(costModel);
        Long costModelId = costModels.get(costModelIndex).getId();

        List<TJob> tJobs = hibernateClient.executeQuery(QueryHelper.createListQuery(TJob.class));
        int tJobIndex = tJobs.indexOf(tJob);
        Long tJobId = tJobs.get(tJobIndex).getId();

        ExtendedModelMap model = new ExtendedModelMap();
        apiController.estimatePost(String.valueOf(tJobId), String.valueOf(costModelId), model);

        Double cost = (Double) model.get("estimate");

        assertEquals(0d , cost);
    }

    /**
     * Check and configure Hibernate
     */
    private static void checkAndConfigureHibernate() {
        try {
            // get credentials
            HibernateCredentials credentials = Loader.getSettings().getHibernateCredentials();

            // create configuration
            Configuration configuration = HibernateConfiguration.createConfiguration(credentials);

            // create Hibernate
            HibernateClient.createInstance(configuration);

        } catch (Exception e) {
            String log = String.format("Couldn't connect to Hibernate: %s", e.getMessage());
            logger.error(log);
            System.err.println(log);
            System.exit(0);
        }
    }

//    @Test
//    public void testGetCostModel() {
//        apiController = new APIController();
//
//        assertEquals(apiController.getCostModel("costModel0", null), "redirection");
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
//    public void testEstimatePost(){
//
//        apiController = new APIController();
//
//        assertEquals(apiController.estimatePost("costModel0"), "redirection");
//    }
}
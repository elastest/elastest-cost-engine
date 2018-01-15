package io.elastest.ece.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.elastest.ece.communication.APICaller;
import io.elastest.ece.load.Loader;
import io.elastest.ece.model.ElasTest.*;
import io.elastest.ece.model.HTTPResponse;
import io.elastest.ece.model.TJob;
import io.elastest.ece.persistance.HibernateClient;
import io.elastest.ece.persistance.QueryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

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

@Controller
public class APIController {

    private static final Logger logger = LoggerFactory.getLogger(APIController.class.getName());

    @PostConstruct
    public void init() {
        //TODO: Communicate with TORM to get all the TJobs
        //TODO: remove, legacy code for testing
        testCostModelValues();
        testTestValues();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndex(Model model) {
        logger.info("Creating test values.");
        HibernateClient hibernateClient = HibernateClient.getInstance();
        List<CostModel> costModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));
        List<TJob> tJobs = hibernateClient.executeQuery(QueryHelper.createListQuery(TJob.class));

        String tormURL = Loader.getSettings().getElastestSettings().getElasTestTormAPI() + Loader.getSettings().getElastestSettings().getElasTestTormTJobEndpoint();
        String esmURL = Loader.getSettings().getElastestSettings().getElasTestESMAPI() + Loader.getSettings().getElastestSettings().getElasTestESMCatalogEndpoint();
        APICaller apiCaller = new APICaller();
        List<TormTJobsResponse> tjobs = new ArrayList<>();
        EsmServiceCatalogResponse serviceCatalog = new EsmServiceCatalogResponse();
        try {
            // Get all TJobs from TORM
            HTTPResponse response = apiCaller.get(new URL(tormURL));
//            response.unescape();
            tjobs = response.getAsListOfType(TormTJobsResponse.class);

            // Get all Services from ESM
            response = apiCaller.getXBrokerApiVersion(new URL(esmURL));
            serviceCatalog = (EsmServiceCatalogResponse) response.getAsClass(EsmServiceCatalogResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("Adding attributes to the model.");
        model.addAttribute("tjobs", tjobs);
        model.addAttribute("serviceCatalog", serviceCatalog);
        model.addAttribute("tests", tJobs);
        model.addAttribute("costModels", costModels);
        logger.info("Redirecting to the ECE's Index Page.");
        return "tJobSelection";
    }

    @RequestMapping(value = "/estimate", method = RequestMethod.GET)
    public String getEstimateCostModels(Model model) {
        //TODO: remove, legacy code
        logger.info("Creating test values.");
        HibernateClient hibernateClient = HibernateClient.getInstance();
        List<CostModel> costModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));
        List<TJob> tJobs = hibernateClient.executeQuery(QueryHelper.createListQuery(TJob.class));

        logger.info("Adding attributes to the model.");
        model.addAttribute("tests", tJobs);
        model.addAttribute("costModels", costModels);
        logger.info("Redirecting to the ECE's Cost Estimation Page.");
        return "estimate";
    }

    @RequestMapping(value = "/deletecostmodel", method = RequestMethod.GET)
    public String getDeleteCostModel(Model model) {
        //TODO: remove, legacy code
        logger.info("Creating test values.");
        HibernateClient hibernateClient = HibernateClient.getInstance();
        List<CostModel> costModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));

        logger.info("Adding attributes to the model.");
        model.addAttribute("costModels", costModels);
        logger.info("Redirecting to the ECE's Delete Cost Model Page.");
        return "deletecostmodel";
    }

    @RequestMapping(value = "/costmodeldetails", method = RequestMethod.GET)
    public String getCostModelDetails(Model model) {
        //TODO: remove, legacy code
        logger.info("Creating test values.");
        HibernateClient hibernateClient = HibernateClient.getInstance();
        List<CostModel> costModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));

        logger.info("Adding attributes to the model.");
        model.addAttribute("costModels", costModels);
        logger.info("Redirecting to the ECE's Cost Model Details Page.");
        return "costmodeldetails";
    }

    @RequestMapping(value = "/createcostmodel", method = RequestMethod.GET)
    public String getCreateCostModel() {
        //TODO: remove, legacy code
        logger.info("Redirecting to the ECE's Cost Model Creation Page.");
        return "createcostmodel";
    }

    @RequestMapping(value = "/costmodel", method = RequestMethod.POST)
    public String addCostModel(@RequestParam String name, @RequestParam String description, @RequestParam String[] fixNames, @RequestParam Double[] fixValues, @RequestParam String[] varNames, @RequestParam Double[] varValues, Model model) {
        logger.info("Creating Cost Model.");
        //TODO: remove, legacy code
        HashMap<String, Double> varCosts = new HashMap<>();
        HashMap<String, Double> fixCosts = new HashMap<>();

        for (int i = 0; i < fixNames.length; i++) {
            fixCosts.put(fixNames[i], fixValues[i]);
        }

        for (int i = 0; i < varNames.length; i++) {
            varCosts.put(varNames[i], varValues[i]);
        }

        CostModel costModel = new CostModel(name, "ONDEMAND", fixCosts, varCosts, null, description);
        logger.info("Persisting the created Cost Model into the DB");
        HibernateClient hibernateClient = HibernateClient.getInstance();
        hibernateClient.persistObject(costModel);
        logger.info("Added a new Cost Model.");
        return "costModelCreated";
    }

    @RequestMapping(value = "/costmodel", method = RequestMethod.GET)
    public String getCostModel(@RequestParam String costModelId, Model model) {
        //TODO: remove, legacy code
        logger.info("Returning information about the cost model: " + costModelId);
        HibernateClient hibernateClient = HibernateClient.getInstance();
        CostModel costModel = (CostModel) hibernateClient.getObject(CostModel.class, Long.valueOf(costModelId));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        model.addAttribute("costModel", gson.toJson(costModel));
        return "costModelInfo";
    }

    @RequestMapping(value = "/deleteCostModel", method = RequestMethod.POST)
    public String deleteCostModel(@RequestParam String costModelId) {
        //TODO: remove, legacy code
        logger.info("Deleting the Cost Model: " + costModelId);
        HibernateClient hibernateClient = HibernateClient.getInstance();
        CostModel costModel = (CostModel) hibernateClient.getObject(CostModel.class, Long.valueOf(costModelId));
        hibernateClient.deleteObject(costModel);
        logger.info("Deleted.");
        return "costModelDeleted";
    }

    @RequestMapping(value = "/estimation", method = RequestMethod.GET)
    public String getEstimate(@RequestParam("tJobId") String tJobId, @RequestParam("costModelId") String costModelId, Model model) {
        //TODO: remove, legacy code
        return estimatePost(tJobId, costModelId, model);
    }

    @RequestMapping(value = "/estimation", method = RequestMethod.POST)
    public String estimatePost(@RequestParam String testId, @RequestParam String costModelId, Model model) {
        //TODO: remove, legacy code
        HibernateClient hibernateClient = HibernateClient.getInstance();
        CostModel costModel = (CostModel) hibernateClient.getObject(CostModel.class, Long.valueOf(costModelId));
        TJob tJob = (TJob) hibernateClient.getObject(TJob.class, Long.valueOf(testId));
        double totalCost = 0;
        String costModelType = costModel.getType();
        if (costModelType.equalsIgnoreCase("ONDEMAND")) {
            Map<String, Double> fixCost = costModel.getFix_cost();
            Map<String, Double> varRate = costModel.getVar_rate();
            Map<String, String> metadata = tJob.getMetadata();

            logger.info("Adding all fix costs from the cost model.");
            for (Object key : fixCost.keySet()) {
                totalCost += fixCost.get(key);
            }

            logger.info("Adding all the accounted variable costs in the model");
            for (Object key : varRate.keySet()) {
                if (tJob.getMetadata().containsKey(key)) {
                    totalCost += (Double.parseDouble(metadata.get(key)) * (varRate.get(key)));
                }
            }

            //TODO:add component costs.
            logger.info("Adding all the component costs.");
        } else if (costModelType.equalsIgnoreCase("PAYG")) {
            //TODO: implement
        }

        model.addAttribute("estimate", totalCost);
        logger.info("Returning an estimate for the test jobs: " + testId + " and the Cost Model: " + costModelId);
        return "estimation";
    }

    @RequestMapping(value = "/tJobEstimation", method = RequestMethod.POST)
    public String getTJobEstimation(@RequestParam("tJobId") String tJobId, @RequestParam(value = "minutes", required = false) Integer minutes, Model model) {
        APICaller apiCaller = new APICaller();
        String tormURL = Loader.getSettings().getElastestSettings().getElasTestTormAPI() + Loader.getSettings().getElastestSettings().getElasTestTormTJobEndpoint() + "/" + tJobId;
        TormTJobsResponse tJob;
        try {
            // Try to get the TJob with the selected Id from TORM
            HTTPResponse response = apiCaller.get(new URL(tormURL));
            tJob = (TormTJobsResponse) response.getAsClass(TormTJobsResponse.class);

            ArrayList<EsmService> services = new ArrayList<>();
            // Try to get the services it's using.
            String esmServiceString = tJob.getEsmServicesString();
            if (!esmServiceString.equalsIgnoreCase("")) {
                Gson gson = new Gson();
                Type esmServiceArray = new TypeToken<ArrayList<EsmServiceStringObject>>() {
                }.getType();
                ArrayList<EsmServiceStringObject> esmServices = gson.fromJson(esmServiceString, esmServiceArray);
                // For each service present on the TJob String get it's definition from the ESM and add them to a map
                for (EsmServiceStringObject service : esmServices) {
                    if (service.getSelected()) {
                        // Get the Service Type from the catalog
                        String esmURL = Loader.getSettings().getElastestSettings().getElasTestESMAPI() + Loader.getSettings().getElastestSettings().getElasTestESMCatalogEndpoint();//getElasTestESMInstanceEndpoint() + service.getId();
                        response = apiCaller.getXBrokerApiVersion(new URL(esmURL));
                        EsmServiceCatalogResponse esmServiceCatalogResponse = (EsmServiceCatalogResponse) response.getAsClass(EsmServiceCatalogResponse.class);
                        for (EsmService serviceType : esmServiceCatalogResponse.getServices()) {
                            if (serviceType.getId().equals(service.getId())) {
                                services.add(serviceType);
                            }
                        }
                    }
                }
            }
            // Extract the Cost models out of the services that are being used plus the tJob cost model
            ArrayList<CostModel> costModels = new ArrayList<>();
//            models.add(tJob.getCost());
            for (EsmService service : services) {
                List<EsmPlan> plans = service.getPlans();
                for (EsmPlan plan : plans) {
                    costModels.add(plan.getMetadata().getCosts());
                }
            }

            // Creating different estimation ranges depending on config file
            HashMap<String, LinkedHashMap<String, Double>> estimations = new HashMap<>();
            LinkedList<Map<String, Double>> estimationList = new LinkedList<>();
            List<Integer> estimationRanges = new ArrayList<>();

            if (minutes != null)
                estimationRanges.add(minutes);
            else
                estimationRanges = Loader.getSettings().getEstimationSettings().getEstimationRange();

            // Creating estimations
            for (Integer range : estimationRanges) {
                LinkedHashMap<String, Double> costReport = new LinkedHashMap<>();
                Double price = 0.0;
                for (CostModel costModel : costModels) {
                    Map<String, Double> fix = costModel.getFix_cost();
                    Map<String, Double> var = costModel.getVar_rate();

                    // Add fix values to the cost report
                    Iterator itFix = fix.keySet().iterator();
                    while (itFix.hasNext()) {
                        String key = (String) itFix.next();
                        Double value = fix.get(key);
                        // Check if the field is already present
                        if (costReport.containsKey(key))
                            value = value + costReport.get(key);
                        costReport.put(key, value);
                        price = price + value;
                    }

                    // Add variable rate values to the cost report
                    Iterator varFix = var.keySet().iterator();
                    while (varFix.hasNext()) {
                        String key = (String) varFix.next();
                        Double value = var.get(key) * range;
                        // Check if the field is already present
                        if (costReport.containsKey(key))
                            value = value + costReport.get(key);
                        costReport.put(key, value);
                        price = price + value;
                    }
                }
                // Add the final field and value for the cost estimation
                costReport.put("Cost Estimation", price);

                estimations.put("Estimation based on " + range + " minutes", costReport);
                estimationList.add(costReport);
            }
            model.addAttribute("estimations", estimations);
            model.addAttribute("estimationList", estimationList);

        } catch (Exception e) {
            logger.error("No Tjobs were retrieved with the provided Id: " + tJobId);
        }
        return "estimation";
    }

    private ArrayList testCostModelValues() {
        logger.info("Creating Demo Cost Model Values.");
        ArrayList<CostModel> result = new ArrayList();
        HashMap varCosts = new HashMap();
        varCosts.put("cpus", 50.0);
        varCosts.put("memory", 10.0);
        varCosts.put("disk", 1.0);
        HashMap fixCost = new HashMap();
        fixCost.put("deployment", 5.0);

        CostModel costModel = new CostModel("On Demand 5 + Charges", "ONDEMAND", fixCost, varCosts, null, "On Demand 5 per deployment, 50 per core, 10 per GB ram and 1 per GB disk");
        result.add(costModel);

        HashMap varCosts1 = new HashMap();
        varCosts1.put("cpus", 1.0);
        varCosts1.put("memory", 1.0);
        varCosts1.put("disk", 1.0);
        HashMap fixCost1 = new HashMap();
        fixCost1.put("deployment", 10.0);

        CostModel costModel1 = new CostModel("On demand 10 + Charges", "ONDEMAND", fixCost1, varCosts1, null, "On Demand 10 per deployment, 1 per core, 1 per GB ram and 1 per GB disk");
        result.add(costModel1);

        logger.info("Persisting demo values");
        HibernateClient hibernateClient = HibernateClient.getInstance();

        List<CostModel> oldCostModels = hibernateClient.executeQuery(QueryHelper.createListQuery(CostModel.class));
        for (CostModel oldCostModel : oldCostModels)
            hibernateClient.deleteObject(oldCostModel);

        hibernateClient.persistObject(costModel);
        hibernateClient.persistObject(costModel1);
        return result;
    }

    private ArrayList testTestValues() {
        logger.info("Creating Demo T-Job Values.");
        ArrayList<TJob> result = new ArrayList();
        HashMap<String, String> test0Map = new HashMap<>();
        test0Map.put("cpus", "8.0");
        test0Map.put("memory", "20.0");
        test0Map.put("disk", "500.0");

        HashMap<String, String> test1Map = new HashMap<>();
        test1Map.put("cpus", "1.0");
        test1Map.put("memory", "1.0");
        test1Map.put("disk", "1.0");

        TJob tJob = new TJob("test0 8 cores, 20gb ram, 500gb disk ", test0Map);
        TJob tJob1 = new TJob("test1 1 core, 1gb ram, 1gb disk ", test1Map);
        tJob.setDescription("Test using a Big VM");

        result.add(tJob);
        result.add(tJob1);

        logger.info("Persisting demo values");
        HibernateClient hibernateClient = HibernateClient.getInstance();

        List<TJob> oldTJobs = hibernateClient.executeQuery(QueryHelper.createListQuery(TJob.class));
        for (TJob oldTJob : oldTJobs)
            hibernateClient.deleteObject(oldTJob);

        hibernateClient.persistObject(tJob);
        hibernateClient.persistObject(tJob1);
        return result;
    }
}

package io.elastest.ece.application;

import io.elastest.ece.model.CostItem;
import io.elastest.ece.model.CostModel;
import io.elastest.ece.model.TJob;
import io.elastest.ece.persistance.HibernateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<String, CostModel> costModelStorage = new HashMap<>();
    private HashMap<String, TJob> tJobStorage = new HashMap<>();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndex(Model model) {
        logger.info("Creating test values.");
        ArrayList tests = testTestValues();
        ArrayList costs = testCostModelValues();

        logger.info("Adding attributes to the model.");
        model.addAttribute("tests", tests);
        model.addAttribute("costModels", costs);
        logger.info("Redirecting to the ECE's Index Page.");
        return "index";
    }

    @RequestMapping(value = "/costmodel", method = RequestMethod.POST)
    public String addCostModel(@RequestParam String name, @RequestParam double cpus, @RequestParam double memory, @RequestParam double disk, Model model) {
        logger.info("Creating Cost Model.");
        HashMap<String, Double> costs = new HashMap<>();
        costs.put("cpus", cpus);
        costs.put("memory", memory);
        costs.put("disk", disk);
//        HashMap<String, CostItem> costs = new HashMap<>();
//        costs.put("cpus", new CostItem(cpus));
//        costs.put("memory", new CostItem(memory));
//        costs.put("disk", new CostItem(disk));
        CostModel costModel = new CostModel(name, "ONDEMAND", costs);
        costModelStorage.put(name, costModel);
        logger.info("Persisting the created Cost Model into the DB");
        HibernateClient hibernateClient = HibernateClient.getInstance();
        hibernateClient.persistObject(costModel);
        logger.info("Added a new Cost Model.");
        return "costModelCreated";
    }

    @RequestMapping(value = "/costmodel/{id}", method = RequestMethod.GET)
    public String getCostModel(@PathVariable String id, Model model) {
        logger.info("Returning information about the cost model: " + id);
        return "redirection";
    }

    @RequestMapping(value = "/costmodel/{id}", method = RequestMethod.DELETE)
    public String deleteCostModel(@PathVariable String id, Model model) {
        logger.info("Deletes the Cost Model: " + id);
        return "redirection";
    }

    @RequestMapping(value = "/estimate", method = RequestMethod.POST)
    public String estimatePost(@RequestParam String testName, @RequestParam String costModelName, Model model) {
        CostModel costModel = costModelStorage.get(costModelName);
        TJob tJob = tJobStorage.get(testName);
        double totalCost = 0;
        if (costModel.getType().equalsIgnoreCase("PAYG")) {
            for (Object key : costModel.getCost().keySet()) {
                if (tJob.getMetadata().containsKey(String.valueOf(key))) {

                }
            }
        } else if (costModel.getType().equalsIgnoreCase("ONDEMAND")) {
            for (Object key : costModel.getCost().keySet()) {
                if (tJob.getMetadata().containsKey(String.valueOf(key))) {
                    totalCost += (Double.parseDouble(String.valueOf(tJob.getMetadata().get(String.valueOf(key)))) * Double.parseDouble(String.valueOf(costModel.getCost().get(key))));
                }
            }
        }

        model.addAttribute("estimate", totalCost);
        logger.info("Returning an estimate for the test jobs: " + testName + " and the Cost Model: " + costModelName);
        return "estimate";
    }

    @RequestMapping(value = "/estimate/{testId}/costmodel/{costModelId}", method = RequestMethod.GET)
    public String estimate(@PathVariable String testId, @PathVariable String costModelId, Model model) {
        logger.info("Returning an estimate of the test job: " + testId + " and the Cost Model: " + costModelId);
        return "redirection";
    }

    @RequestMapping(value = "/status/{SuTId}", method = RequestMethod.GET)
    public String getStatus(@PathVariable String SuTId, Model model) {
        logger.info("Returning the status of the SuT: " + SuTId);
        return "redirection";
    }

    @RequestMapping(value = "/cost/{SuTId}", method = RequestMethod.GET)
    public String getCost(@PathVariable String SuTId, Model model) {
        logger.info("Returning the cost of running the SuT: " + SuTId);
        return "redirection";
    }

    private ArrayList testCostModelValues() {
        logger.info("Creating Demo Cost Model Values.");
        ArrayList<CostModel> result = new ArrayList();
        HashMap costs = new HashMap();
//        costs.put("cpus", new CostItem(50.0));
//        costs.put("memory", new CostItem(10.0));
//        costs.put("disk", new CostItem(1.0));
//
//        HashMap costs1 = new HashMap();
//        costs1.put("cpus", new CostItem(1.0));
//        costs1.put("memory", new CostItem(1.0));
//        costs1.put("disk", new CostItem(1.0));
        costs.put("cpus", 50.0);
        costs.put("memory", 10.0);
        costs.put("disk", 1.0);

        HashMap costs1 = new HashMap();
        costs1.put("cpus", 1.0);
        costs1.put("memory", 1.0);
        costs1.put("disk", 1.0) ;

        CostModel costModel0 = new CostModel("On Demand, 50 per core, 10 per GB ram and 1 per GB disk", "ONDEMAND", costs);
        CostModel costModel1 = new CostModel("On Demand, 1 Per Each", "ONDEMAND", costs1);
        CostModel costModel2 = new CostModel("PAYG", "PAYG", costs);
        result.add(costModel0);
        result.add(costModel1);
        result.add(costModel2);
        costModelStorage.put(result.get(0).getName(), result.get(0));
        costModelStorage.put(result.get(1).getName(), result.get(1));
        costModelStorage.put(result.get(2).getName(), result.get(2));

//        logger.info("Persisting demo values");
//        HibernateClient hibernateClient = HibernateClient.getInstance();
//        hibernateClient.persistObject(costModel0);
//        hibernateClient.persistObject(costModel1);
//        hibernateClient.persistObject(costModel2);

        return result;
    }

    private ArrayList testTestValues() {
        ArrayList<TJob> result = new ArrayList();
        HashMap<String, Double> test0Map = new HashMap<>();
        test0Map.put("cpus", 8.0);
        test0Map.put("memory", 20.0);
        test0Map.put("disk", 500.0);
        HashMap<String, Double> test1Map = new HashMap<>();
        test1Map.put("cpus", 4.0);
        test1Map.put("memory", 10.0);
        test1Map.put("disk", 250.0);
        HashMap<String, Double> test2Map = new HashMap<>();
        test2Map.put("cpus", 1.0);
        test2Map.put("memory", 1.0);
        test2Map.put("disk", 1.0);

        TJob test0 = new TJob("test0 8 cores, 20gb ram, 500gb disk ", test0Map);
        TJob test1 = new TJob("test1 4 cores, 10gb ram, 250gb disk", test1Map);
        TJob test2 = new TJob("test2 1 core, 1gb ram, 1gb disk", test2Map);
        test0.setDescription("Test using a Big VM");
        test1.setDescription("Test using a Medium VM");
        test2.setDescription("Test using 1 of each unit");

        result.add(test0);
        result.add(test1);
        result.add(test2);
        tJobStorage.put(result.get(0).getName(), result.get(0));
        tJobStorage.put(result.get(1).getName(), result.get(1));
        tJobStorage.put(result.get(2).getName(), result.get(2));
        return result;
    }
}

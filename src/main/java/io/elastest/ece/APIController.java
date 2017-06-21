package io.elastest.ece;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndex(){
        logger.info("Redirecting to the ECE's Index Page.");
        return "redirection";
    }

    @RequestMapping(value = "/costmodel", method = RequestMethod.POST)
    public String addCostModel(){
        logger.info("Added a new Cost Model.");
        return "redirection";
    }

    @RequestMapping(value = "/costmodel/{id}", method = RequestMethod.GET)
    public String getCostModel(@PathVariable String id){
        logger.info("Returning information about the cost model: " + id);
        return "redirection";
    }

    @RequestMapping(value = "/costmodel/{id}", method = RequestMethod.DELETE)
    public String deleteCostModel(@PathVariable String id){
        logger.info("Deletes the Cost Model: " + id);
        return "redirection";
    }

    @RequestMapping(value = "/estimate/{testId}/costmodel/{costModelId}", method = RequestMethod.GET)
    public String estimate(@PathVariable String testId, @PathVariable String costModelId){
        logger.info("Returning an estimate of the test job: " + testId + " and the Cost Model: " + costModelId);
        return "redirection";
    }

    @RequestMapping(value = "/status/{SuTId}", method = RequestMethod.GET)
    public String getStatus(@PathVariable String SuTId){
        logger.info("Returning the status of the SuT: " + SuTId);
        return "redirection";
    }

    @RequestMapping(value = "/cost/{SuTId}", method = RequestMethod.GET)
    public String getCost(@PathVariable String SuTId){
        logger.info("Returning the cost of running the SuT: " + SuTId);
        return "redirection";
    }
}

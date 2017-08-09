package io.elastest.ece.application;

import io.elastest.ece.model.CostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
 * License for the specific language governing permis sions and limitations
 * under the License.
 * <p>
 * Created by Manu Perez on 26/07/17.
 */

@Controller
public class PersistanceController { // implements CommandLineRunner { //cmdr para ejecutarlo al empezar la app

//    @Autowired
//    private CostModelRepository costModelRepository;
//
//    @RequestMapping(value = "/costmodels", method = RequestMethod.GET)
//    public String getCostModels() {
//        List<CostModel> costModels = costModelRepository.findAll();
//        return "";
//    }

//    @RequestMapping(value = "/costmodel", method = RequestMethod.POST)
//    public String addCostModel(@RequestParam) {
//        costModelRepository.save(new CostModel("", "", null));
//        return "";
//    }

//    @Override
//    public void run(String... strings) throws Exception {
//        costModelRepository.findById(1l);
//    }
}

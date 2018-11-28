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

package ch.splab.cab.costengine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.splab.cab.costengine.model.*;
import ch.splab.cab.costengine.support.RESTDriver;
import com.google.gson.Gson;
import okhttp3.Response;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@org.springframework.stereotype.Controller
public class Controller {
    final static Logger logger = Logger.getLogger(Controller.class);

    @RequestMapping(value="/error", method = RequestMethod.GET)
    public String showError(Model model)
    {
        return "error";
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String showIndex(HttpServletRequest request, HttpServletResponse response, Model model)
    {
        Response restResponse = RESTDriver.doGet(AppConfiguration.getETMApi() + "api/tjob", null);
        if(restResponse.code() == 200)
        {
            logger.info("Call to TORM API: status OK");
            try
            {
                String body = restResponse.body().string();
                Gson gson = new Gson();
                TJob[] jobList = gson.fromJson(body, TJob[].class);
                logger.info("from TORM received TJob count: " + jobList.length);
                model.addAttribute("tjoblist", jobList);
            }
            catch(IOException ioex)
            {
                ioex.printStackTrace();
                logger.warn("Error in parsing TORM response: " + ioex.getMessage() + ":" + ioex.getLocalizedMessage());
            }
        }
        else
            logger.warn("Unable to call TORM API");
        return "index";
    }

    @RequestMapping(value="/dynamicanalysis", method = RequestMethod.POST)
    public String getDynamicAnalysisData(HttpServletRequest request, HttpServletResponse response, Model model) {
        String tjobId = request.getParameter("tjobid");
        String tjobName = request.getParameter("tjobname");
        model.addAttribute("tjobid", tjobId);
        model.addAttribute("tjobname", tjobName);
        String tjobServices = request.getParameter("tjobservices");
        model.addAttribute("tjobservices", tjobServices);
        logger.info("Support services list for TJob " + tjobName + ": " + tjobServices);
        logger.info("Dynamic analysis request received: job-id:" + tjobId + " job-name:" + tjobName + " services-string: " + tjobServices);
        Gson gson = new Gson();
        TJobService[] services;
        try
        {
            services = gson.fromJson(tjobServices, TJobService[].class);
            logger.info("Starting analysis, services list count: " + services.length);
        }
        catch (Exception ex)
        {
            logger.warn("Error in parsing support service string received from TORM: " + ex.getMessage() + ":" + ex.getLocalizedMessage());
            throw ex; //to show the error message
        }

        //getting the service catalog
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X_Broker_Api_Version", "2.12");
        Response restResponse = RESTDriver.doGet(AppConfiguration.getESMApi() + "v2/catalog", headers);

        if(restResponse.code() == 200) {
            logger.info("Call to ESM API: status OK");
            try {
                String body = restResponse.body().string();
                ESMCatalog catalog = gson.fromJson(body, ESMCatalog.class);
                logger.info("from ESM received catalog entries: " + catalog.services.length);
                LinkedList<ECECostModel> serviceCostModelList = new LinkedList();

                for(TJobService service:services)
                {
                    if(service.selected) {
                        logger.info("Found an enabled service for selected TJob: " + service.name + " id: " + service.id);
                        //now analyzing the cost for this particular service and adding a separate entry in model variable

                        for (ESMService catalogService : catalog.services) {

                            if (catalogService.id.equalsIgnoreCase(service.id)) {
                                logger.info("Catalog matching entry for support service: " + service.id + " with short-name: " + catalogService.short_name + " located.");

                                for (ESMServicePlan plan : catalogService.plans) {
                                    logger.info("Proceeding to analyze cost with plan-id: " + plan.id);
                                    logger.info("Found ece cost model: " + plan.metadata.costs.description);
                                    ECECostModel cModel = plan.metadata.costs;
                                    cModel.shortName = catalogService.short_name;

                                    serviceCostModelList.add(cModel);
                                }
                            }
                        }
                    }
                }
                restResponse.close();

                ECECostModel cModel = new ECECostModel();
                cModel.currency = "eur";
                cModel.description = "Infrastructure cost model";
                cModel.model = "pay-as-you-go";
                HashMap mParam = new HashMap();
                mParam.put("setup_cost", 0.0d);
                cModel.model_param = mParam;
                cModel.shortName = "Infrastructure";
                HashMap[] mList = new HashMap[3];
                mList[0] = new HashMap();
                mList[1] = new HashMap();
                mList[2] = new HashMap();
                mList[0].put("meter_name", "ram");
                mList[0].put("meter_type", "gauge");
                mList[0].put("unit_cost", 0.00125d);
                mList[0].put("unit", "mb-hour");
                mList[1].put("meter_name", "cpu_usage");
                mList[1].put("meter_type", "gauge");
                mList[1].put("unit_cost", 0.021d);
                mList[1].put("unit", "core-hour");
                mList[2].put("meter_name", "net_traffic");
                mList[2].put("meter_type", "gauge");
                mList[2].put("unit_cost", 0.00075d);
                mList[2].put("unit", "kb");
                cModel.meter_list = mList;

                serviceCostModelList.add(cModel);
                model.addAttribute("servicecostmodellist", serviceCostModelList);

                //make a call to events service using TJob-ID as account field, and time range 0 until now
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                long currTime = System.currentTimeMillis();
                cal.setTimeInMillis(currTime); // compute start of the day for the timestamp
                Date date = new java.util.Date(currTime);
                // the format of your date
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                // give a timezone reference for formatting (see comment at the bottom)
                sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                String formattedDate = sdf.format(date);
                model.addAttribute("timestring", formattedDate);

                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                long timeStart = cal.getTimeInMillis()/1000L;
                long timeEnd = System.currentTimeMillis() / 1000L;
                long timeBucket = 3600L; //buckets of 1 hr window

                logger.info("Timestamp at start of this date in UTC is: " + (cal.getTimeInMillis()/1000));
                LinkedHashSet<Object> resourceList = new LinkedHashSet<Object>(); //to collect unique resourceNames
                LinkedList<Object> usageList = new LinkedList<>(); //to collect usage for googleMap
                LinkedList<Object> infraUsageList = new LinkedList<>(); //to collect usage for googleMap
                LinkedHashSet<Object> infraResourceList = new LinkedHashSet<Object>(); //to collect unique resourceNames
                LinkedList<Object> scostList = new LinkedList<>(); //to collect service cost components for googleMap
                Object[][] piedata = null;
                double totalCost = 0.0d;

                headers = new HashMap<>();
                headers.put("x-auth-apikey", AppConfiguration.getEMPApikey());
                headers.put("x-auth-login", AppConfiguration.getEMPUser());
                headers.put("x-series-name", AppConfiguration.getEMPSeries());
                headers.put("x-topic-name", AppConfiguration.getEMPTopic());
                logger.debug("REST API headers: " + headers.toString());

                restResponse = RESTDriver.doGet(AppConfiguration.getEMPApi() + tjobId, headers);

                resourceList.add("Test Run ID");
                resourceList.add("Duration");
                usageList.add(0, resourceList);

                infraResourceList.add("Test Run ID");
                infraResourceList.add("Net Rx (bytes)");
                infraResourceList.add("Net Tx (bytes)");
                infraResourceList.add("Memory");
                infraUsageList.add(0, infraResourceList);

                LinkedHashSet<Object> costbreakuplabel = new LinkedHashSet<Object>(); //to collect cost component names
                costbreakuplabel.add("Test Run ID");
                costbreakuplabel.add("CPU cost");
                costbreakuplabel.add("MEM cost");
                costbreakuplabel.add("NET cost");
                scostList.add(0, costbreakuplabel);

                //pass 1 to build the resource index

                if(restResponse.code() == 200)
                {
                    body = restResponse.body().string();
                    TestRuns tUsage = gson.fromJson(body, TestRuns.class);
                    if(tUsage != null && tUsage.ExecutionRun != null)
                    {
                        int index = 1;
                        piedata = new Object[tUsage.ExecutionRun.length+1][2];
                        piedata[0][0] = "Test run";
                        piedata[0][1] = "Cost";

                        for(ExecutionRunData tRun :tUsage.ExecutionRun) {
                            LinkedList<Object> dataList = new LinkedList<>(); //to collect usage for googleMap
                            LinkedList<Object> infradataList = new LinkedList<>(); //to collect usage for googleMap
                            LinkedList<Object> costList = new LinkedList<>(); //to collect cost breakup for googleMap

                            piedata[index][0] = tRun.ExecId;
                            costList.add(tRun.ExecId);

                            //convert active time to hour
                            double duration = tRun.duration / (60.0d * 60.0d);

                            double cpu_cost = 0.0d;
                            double mem_cost = 0.0d;
                            double net_cost = 0.0d;

                            double net_total_usage = tRun.network_rx_bytes + tRun.network_tx_bytes;
                            if(mList[2].get("unit").toString().equalsIgnoreCase("kb"))
                                net_total_usage /= 1024.0d;

                            net_cost = net_total_usage * Double.parseDouble(mList[2].get("unit_cost").toString());
                            mem_cost = (tRun.memory_mean / (1024.0d * 1024.0d)) * duration
                                    * Double.parseDouble(mList[0].get("unit_cost").toString());
                            cpu_cost = duration * Double.parseDouble(mList[1].get("unit_cost").toString()) * 1.0d; //assuming only single core for now

                            costList.add(cpu_cost);
                            costList.add(mem_cost);
                            costList.add(net_cost);

                            piedata[index][1] = (net_cost + mem_cost + cpu_cost);
                            totalCost += (net_cost + mem_cost + cpu_cost);

                            dataList.add(tRun.ExecId);
                            dataList.add(tRun.duration);

                            infradataList.add(tRun.ExecId);
                            infradataList.add(tRun.network_rx_bytes);
                            infradataList.add(tRun.network_tx_bytes);
                            infradataList.add(tRun.memory_mean);

                            logger.info("Found exec id: " + tRun.ExecId + ", for TJob: " + tUsage.TJobId + ", Usage:[(mem, " + tRun.memory_mean +
                                    "), (net-rx, " + tRun.network_rx_bytes + "), (net-tx, " + tRun.network_tx_bytes + ")]");
                            usageList.add(index, dataList);
                            infraUsageList.add(index, infradataList);
                            scostList.add(index, costList);
                            index++;
                        }
                    }
                    else
                    {
                        logger.warn("No data received.");
                    }
                }
                restResponse.close();

                //if resourceList is empty, add a placeholder 2nd column for GCharts
                if(resourceList.size() == 1)
                    resourceList.add("usageVal");

                //model.addAttribute("timeline", timeline);
                model.addAttribute("usage", usageList);
                //now create the structure that google charts expect
                model.addAttribute("infrausagebardata", infraUsageList);
                model.addAttribute("piedata", piedata);
                model.addAttribute("totalcost", Math.round(totalCost * 100.0) / 100.0);
                model.addAttribute("costbreakup", scostList);
            }
            catch(IOException ioex)
            {
                ioex.printStackTrace();
                logger.warn("Error in parsing ESM catalog details: " + ioex.getMessage() + ":" + ioex.getLocalizedMessage());
            }
            catch(Exception ex)
            {
                logger.warn("Unhandled exception is processing catalog data: " + ex.getMessage() + ":" + ex.getLocalizedMessage());
                throw ex;
            }
        }
        else
        {
            logger.warn("Unable to call ESM API");
        }

        return "dynamicanalysis";
    }

    private int getIndexAt(String resourceName, List<Object> resourceList) {
        int index = -1;
        for(Object obj: resourceList)
        {
            index++;
            if(obj.equals(resourceName)) return index;
        }
        return -1;
    }

    @RequestMapping(value="/staticanalysis", method = RequestMethod.POST)
    public String getStaticAnalysisData(HttpServletRequest request, HttpServletResponse response, Model model)
    {
        String tjobId = request.getParameter("tjobid");
        String tjobName = request.getParameter("tjobname");
        model.addAttribute("tjobid", tjobId);
        model.addAttribute("tjobname", tjobName);
        String tjobServices = request.getParameter("tjobservices");
        model.addAttribute("tjobservices", tjobServices);
        logger.info("Support services list for TJob " + tjobName + ": " + tjobServices);
        logger.info("Static analysis request received: job-id:" + tjobId + " job-name:" + tjobName + " services-string: " + tjobServices);
        Gson gson = new Gson();
        TJobService[] services;
        try
        {
            services = gson.fromJson(tjobServices, TJobService[].class);
            logger.info("Starting analysis, services list count: " + services.length);
        }
        catch (Exception ex)
        {
            logger.warn("Error in parsing support service string received from TORM: " + ex.getMessage() + ":" + ex.getLocalizedMessage());
            throw ex; //to show the error message
        }

        //getting the service catalog
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X_Broker_Api_Version", "2.12");
        Response restResponse = RESTDriver.doGet(AppConfiguration.getESMApi() + "v2/catalog", headers);

        if(restResponse.code() == 200)
        {
            logger.info("Call to ESM API: status OK");
            try
            {
                String body = restResponse.body().string();
                ESMCatalog catalog = gson.fromJson(body, ESMCatalog.class);
                logger.info("from ESM received catalog entries: " + catalog.services.length);
                LinkedList<StaticCostComponent> costBreakup = new LinkedList<>();

                LinkedList<ECEMeterModel> serviceMeterList = new LinkedList();

                for(TJobService service:services)
                {
                    if(service.selected) {
                        logger.info("Found an enabled service for selected TJob: " + service.name + " id: " + service.id);
                        //now analyzing the cost for this particular service and adding a separate entry in model variable

                        for(ESMService catalogService:catalog.services)
                        {
                            if(catalogService.id.equalsIgnoreCase(service.id))
                            {
                                logger.info("Catalog matching entry for support service: " + service.id + " with short-name: " + catalogService.short_name + " located.");
                                for(ESMServicePlan plan:catalogService.plans)
                                {
                                    logger.info("Proceeding to analyze cost with plan-id: " + plan.id);
                                    logger.info("Found ece cost model: " + plan.metadata.costs.description);
                                    HashMap modelParam = plan.metadata.costs.model_param;
                                    ECEMeterModel newServiceMeter = new ECEMeterModel();
                                    newServiceMeter.serviceName = catalogService.short_name;
                                    newServiceMeter.meterList = plan.metadata.costs.meter_list;
                                    serviceMeterList.add(newServiceMeter);
                                }
                            }
                            model.addAttribute("servicemeterlist", serviceMeterList);
                        }
                    }
                }
            }
            catch(IOException ioex)
            {
                ioex.printStackTrace();
                logger.warn("Error in parsing ESM catalog details: " + ioex.getMessage() + ":" + ioex.getLocalizedMessage());
            }
            catch(Exception ex)
            {
                logger.warn("Unhandled exception is processing catalog data: " + ex.getMessage() + ":" + ex.getLocalizedMessage());
                throw ex;
            }

        }
        else
        {
            logger.warn("Unable to call ESM API properly.");
        }
        return "usagedata";
    }

    @RequestMapping(value="/showstaticanalysis", method = RequestMethod.POST)
    public String showStaticAnalysis(HttpServletRequest request, HttpServletResponse response, Model model) {

        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
            String paramName = params.nextElement();
            logger.info("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }

        boolean isInfraModelFound = false;

        String tjobId = request.getParameter("tjobid");
        String tjobName = request.getParameter("tjobname");
        String tjobServices = request.getParameter("tjobservices");
        model.addAttribute("tjobid", tjobId);
        model.addAttribute("tjobname", tjobName);
        model.addAttribute("tjobservices", tjobServices);

        logger.info("Support services list for TJob " + tjobName + ": " + tjobServices);
        logger.info("Static analysis request received: job-id:" + tjobId + " job-name:" + tjobName + " services-string: " + tjobServices);

        Gson gson = new Gson();
        TJobService[] services;
        try
        {
            services = gson.fromJson(tjobServices, TJobService[].class);
            logger.info("Starting analysis, services list count: " + services.length);
        }
        catch (Exception ex)
        {
            logger.warn("Error in parsing support service string received from TORM: " + ex.getMessage() + ":" + ex.getLocalizedMessage());
            throw ex; //to show the error message
        }

        //getting the service catalog
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X_Broker_Api_Version", "2.12");
        Response restResponse = RESTDriver.doGet(AppConfiguration.getESMApi() + "v2/catalog", headers);
        if(restResponse.code() == 200) {
            logger.info("Call to ESM API: status OK");
            try
            {
                String body = restResponse.body().string();
                ESMCatalog catalog = gson.fromJson(body, ESMCatalog.class);
                logger.info("from ESM received catalog entries: " + catalog.services.length);
                LinkedList<StaticCostComponent> costBreakup = new LinkedList<>();

                LinkedList<ECECostModel> serviceCostModelList = new LinkedList();
                LinkedList<ECEPieElement> totalCostPie = new LinkedList<>();
                LinkedList<ECEUsageBarElement> barElements = new LinkedList<>();
                for(TJobService service:services)
                {
                    if(service.selected)
                    {
                        logger.info("Found an enabled service for selected TJob: " + service.name + " id: " + service.id);
                        //now analyzing the cost for this particular service and adding a separate entry in model variable

                        for(ESMService catalogService:catalog.services)
                        {
                            if(catalogService.short_name.equalsIgnoreCase("epm"))
                                isInfraModelFound = true;

                            if(catalogService.id.equalsIgnoreCase(service.id))
                            {
                                logger.info("Catalog matching entry for support service: " + service.id + " with short-name: " + catalogService.short_name + " located.");

                                for(ESMServicePlan plan:catalogService.plans)
                                {
                                    logger.info("Proceeding to analyze cost with plan-id: " + plan.id);
                                    logger.info("Found ece cost model: " + plan.metadata.costs.description);
                                    ECECostModel cModel = plan.metadata.costs;
                                    cModel.shortName = catalogService.short_name;

                                    serviceCostModelList.add(cModel);
                                    if(cModel.model_param.containsKey("setup_cost"))
                                    {
                                        ECEPieElement pEle = new ECEPieElement();
                                        pEle.legend = cModel.shortName + "::setup_cost";
                                        pEle.value = ((Double)cModel.model_param.get("setup_cost")).doubleValue();
                                        totalCostPie.add(pEle);
                                    }

                                    StaticCostComponent part = new StaticCostComponent();
                                    part.serviceName = catalogService.short_name;
                                    part.description = plan.metadata.costs.description;
                                    part.planId = plan.id;
                                    part.serviceId = service.id;

                                    for(HashMap meter: cModel.meter_list)
                                    {
                                        if(request.getParameter(cModel.shortName + "_" + meter.get("meter_name")) != null)
                                        {
                                            try
                                            {
                                                logger.info("Meter: " + meter.get("meter_name") + ", cost: " + meter.get("unit_cost") + ", usage: "
                                                        + Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name")))
                                                        + ", total cost: "
                                                        + (Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name"))) * ((Double)meter.get("unit_cost")).floatValue()));
                                                double componentCost = Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name"))) * ((Double)meter.get("unit_cost")).doubleValue();
                                                ECEPieElement pEle = new ECEPieElement();
                                                pEle.legend = cModel.shortName + "::" + meter.get("meter_name");
                                                pEle.value = componentCost;
                                                totalCostPie.add(pEle);
                                                ECEUsageBarElement bEle = new ECEUsageBarElement();
                                                bEle.value = Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name")));
                                                bEle.legend = cModel.shortName + "::" + meter.get("meter_name") + " (" + meter.get("unit") + ")";
                                                barElements.add(bEle);
                                            }
                                            catch(Exception ex)
                                            {
                                                //NaN error
                                                logger.warn(ex.getMessage());
                                            }
                                        }
                                    }
                                    costBreakup.add(part);
                                }
                            }
                        }
                    }
                }

                LinkedList<ECEUsageBarElement> infrabarElements = new LinkedList<>();

                //process infra costs if not found in list from esm
                //TODO: find a reasonable way to get the below hardcoded parameters
                if(!isInfraModelFound)
                {
                    logger.info("Proceeding to analyze infrastructure costs next (not located in ESM data): ");
                    ECECostModel cModel = new ECECostModel();
                    cModel.currency = "eur";
                    cModel.description = "Infrastructure cost model";
                    cModel.model = "pay-as-you-go";
                    HashMap mParam = new HashMap();
                    mParam.put("setup_cost", 0.0d);
                    cModel.model_param = mParam;
                    cModel.shortName = "EPM";
                    HashMap[] mList = new HashMap[3];
                    mList[0] = new HashMap();
                    mList[1] = new HashMap();
                    mList[2] = new HashMap();
                    mList[0].put("meter_name", "ram");
                    mList[0].put("meter_type", "gauge");
                    mList[0].put("unit_cost", 0.00125d);
                    mList[0].put("unit", "mb-hour");
                    mList[1].put("meter_name", "cpu_usage");
                    mList[1].put("meter_type", "gauge");
                    mList[1].put("unit_cost", 0.021d);
                    mList[1].put("unit", "core-hour");
                    mList[2].put("meter_name", "net_traffic");
                    mList[2].put("meter_type", "gauge");
                    mList[2].put("unit_cost", 0.00075d);
                    mList[2].put("unit", "kb");
                    cModel.meter_list = mList;

                    serviceCostModelList.add(cModel);
                    if(cModel.model_param.containsKey("setup_cost"))
                    {
                        ECEPieElement pEle = new ECEPieElement();
                        pEle.legend = cModel.shortName + "::setup_cost";
                        pEle.value = ((Double)cModel.model_param.get("setup_cost")).doubleValue();
                        totalCostPie.add(pEle);
                    }

                    StaticCostComponent part = new StaticCostComponent();
                    part.serviceName = "";
                    part.description = cModel.description;
                    part.planId = "";
                    part.serviceId = "";

                    for(HashMap meter: cModel.meter_list)
                    {
                        if(request.getParameter(cModel.shortName + "_" + meter.get("meter_name")) != null)
                        {
                            try
                            {
                                logger.info("Meter: " + meter.get("meter_name") + ", cost: " + meter.get("unit_cost") + ", usage: "
                                        + Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name")))
                                        + ", total cost: "
                                        + (Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name"))) * ((Double)meter.get("unit_cost")).floatValue()));
                                double componentCost = Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name"))) * ((Double)meter.get("unit_cost")).doubleValue();
                                ECEPieElement pEle = new ECEPieElement();
                                pEle.legend = cModel.shortName + "::" + meter.get("meter_name");
                                pEle.value = componentCost;
                                totalCostPie.add(pEle);
                                ECEUsageBarElement bEle = new ECEUsageBarElement();
                                bEle.value = Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name")));
                                bEle.legend = cModel.shortName + "::" + meter.get("meter_name") + " (" + meter.get("unit") + ")";
                                infrabarElements.add(bEle);
                            }
                            catch(Exception ex)
                            {
                                //NaN error
                                logger.warn(ex.getMessage());
                            }
                        }
                    }
                    costBreakup.add(part);
                }


                for(ESMService catalogService:catalog.services)
                {
                    if (catalogService.name.equalsIgnoreCase("EPM"))
                    {
                        logger.info("Catalog matching entry for infrastructure service: " + catalogService.id + " with short-name: " + catalogService.short_name + " located.");
                        for(ESMServicePlan plan:catalogService.plans)
                        {
                            logger.info("Proceeding to analyze cost with plan-id: " + plan.id);
                            logger.info("Found ece cost model: " + plan.metadata.costs.description);
                            ECECostModel cModel = plan.metadata.costs;
                            cModel.shortName = catalogService.short_name;

                            serviceCostModelList.add(cModel);
                            if(cModel.model_param.containsKey("setup_cost"))
                            {
                                ECEPieElement pEle = new ECEPieElement();
                                pEle.legend = cModel.shortName + "::setup_cost";
                                pEle.value = ((Double)cModel.model_param.get("setup_cost")).doubleValue();
                                totalCostPie.add(pEle);
                            }

                            for(HashMap meter: cModel.meter_list)
                            {
                                if(request.getParameter(cModel.shortName + "_" + meter.get("meter_name")) != null)
                                {
                                    try
                                    {
                                        logger.info("Meter: " + meter.get("meter_name") + ", cost: " + meter.get("unit_cost") + ", usage: "
                                                + Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name")))
                                                + ", total cost: "
                                                + (Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name"))) * ((Double)meter.get("unit_cost")).floatValue()));
                                        double componentCost = Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name"))) * ((Double)meter.get("unit_cost")).doubleValue();
                                        ECEPieElement pEle = new ECEPieElement();
                                        pEle.legend = cModel.shortName + "::" + meter.get("meter_name");
                                        pEle.value = componentCost;
                                        totalCostPie.add(pEle);
                                        ECEUsageBarElement bEle = new ECEUsageBarElement();
                                        bEle.value = Double.parseDouble(request.getParameter(cModel.shortName + "_" + meter.get("meter_name")));
                                        bEle.legend = cModel.shortName + "::" + meter.get("meter_name") + " (" + meter.get("unit") + ")";
                                        infrabarElements.add(bEle);
                                    }
                                    catch(Exception ex)
                                    {
                                        //NaN error
                                        logger.warn(ex.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
                Object[][] piedata = new Object[totalCostPie.size()+1][2];
                piedata[0][0] = "service component";
                piedata[0][1] = "value";
                int counter=1;
                double totalCost = 0.0;
                for(ECEPieElement pieElement: totalCostPie)
                {
                    piedata[counter][0] = pieElement.legend;
                    piedata[counter][1] = pieElement.value;
                    totalCost += pieElement.value;
                    counter++;
                }

                Object[][] bardata = new Object[barElements.size()+1][2];
                bardata[0][0] = "service component";
                bardata[0][1] = "usage volume";
                counter=1;
                for(ECEUsageBarElement bElement: barElements)
                {
                    bardata[counter][0] = bElement.legend;
                    bardata[counter][1] = bElement.value;
                    counter++;
                }

                Object[][] infrabardata = new Object[infrabarElements.size()+1][2];
                infrabardata[0][0] = "infra resource";
                infrabardata[0][1] = "usage volume";
                counter=1;
                for(ECEUsageBarElement bElement: infrabarElements)
                {
                    infrabardata[counter][0] = bElement.legend;
                    infrabardata[counter][1] = bElement.value;
                    counter++;
                }

                model.addAttribute("servicecostmodellist", serviceCostModelList);
                model.addAttribute("usagebardata", bardata);
                model.addAttribute("infrausagebardata", infrabardata);
                model.addAttribute("piedata", piedata);
                model.addAttribute("costbreakup", costBreakup);
                model.addAttribute("totalcost", totalCost);
            }
            catch(IOException ioex)
            {
                ioex.printStackTrace();
                logger.warn("Error in parsing ESM catalog details: " + ioex.getMessage() + ":" + ioex.getLocalizedMessage());
            }
            catch(Exception ex)
            {
                logger.warn("Unhandled exception is processing catalog data: " + ex.getMessage() + ":" + ex.getLocalizedMessage());
                throw ex;
            }

        }
        else
        {
            logger.warn("Unable to call ESM API");
        }

        return "staticanalysis";
    }
}

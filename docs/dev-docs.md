# ElasTest Cost Engine (ECE)

The Cost, Energy and Resource Consumption Modeling Engines (ECE) will be used in order to make ElasTest consider the financial of using clouds. Using clouds cost money, in third party clouds you pay for the time and resources you use. As well as in on-premises clouds, you pay for the energy and the hardware resources utilization. If ElasTest does not consider this aspects, the risk of not being financially sustainable appears.

The ECE is a service that needs information from the ElasTest Service Manager (ESM) and the ElasTest Platform Manager (EPM) for pulling information about the Platforms and Services Cost Models, and also needs information from the ElasTest Test Orchestration and Recommendation Manager (TORM) in order to know what T-Jobs can the ElasTest user estimate.

## Features

The version 0.5.0 of the ElasTest Cost Engine, provides the following features:

- Estimate costs of T-Job executions based on the existing Service cost models defined by the [ESM](https://github.com/elastest/elastest-service-manager).

## How to run

1. Install Docker.

2. Install Docker Compose. 

3. From the root folder, run the image using the following command: 
   - `sudo docker-compose up`

## Basic uasge

When the ECE is instantiated, a basic UI will be exposed at http://localhost:8888

The 0.5.0 version of ECE uses real data coming from different ElasTest Components. The T-Jobs are 
pulled from the [TORM](https://github.com/elastest/elastest-torm) and the Cost Models that are used come from whatever ElasTest Services are used.

The ECE UI offers the functionality to estimate the price of running a specified T-Job running for different time intervals. 
![ElasTest Cost Engine Preconfigured Estimations Page](imgs/ECEPreconfiguredEstimations.png)

Even though it is not used in this version, the ECE UI also ofers a simple way to manage all the Cost Models, creating, deleting and requesting for the json structure.

## Use-case specific GUI

The ECE offers a graphical user interface for the following usecase:

- Estimate a TJob execution cost based on time basis using a predefined configuration or a specified value: http://localhost:8888/

---

## Development documentation


ECE is developed using Java and Spring MVC. 

## Development Environment

- Java 8 or higher. 
- Maven 3 or higher.
- Preferred IDE that can use Maven, Java and HTML.


### Architecture

The ElasTest Cost Engine has aims to provide a [HTTP Rest API](http://elastest.io/docs/api/ece/) that computes cost 
estimations for running T-Jobs under a concrete Infrastructure Cost Model.

ECE will be in contact with the following ElasTest services:
   - [The ESM](https://github.com/elastest/elastest-service-manager) will be used to query the Services pricing strategies.
   - [The EPM](https://github.com/elastest/elastest-platform-manager) will be used to query the Platform pricing strategies.
   - [The TORM](https://github.com/elastest/elastest-torm) will provide the T-Job details.
   - [The EMS](https://github.com/elastest/elastest-monitoring-service) will provide information about the running T-Jobs.


![ElasTest Cost Engine final architecture](imgs/ECEArch.png)

For now, the 0.5.0 version is using part of the final architecture and it will be completed as the project and the rest of ElasTest Components advance.

![ElasTest Cost Engine Mock up architecture](imgs/MockECE.png)

The Cost Models are defined in a generic way so can be adapted to several cases from VM orchestrations, Service 
instantiations and Services that contain VM orchestration as part of them. The data model used is the following:

   - `name` : Name of the Cost Model.
   - `type` : Cost Model type.
   - `fix_cost` : Map containing the keys and values of the fields that need to be accounted in a fix bases.
   - `var_rate` : Map containing the keys and values of the fields that need to be accounted under a pay as you go premises.
   - `components` : Map containing the links to another Cost Models.
   - `description` : Description of the Cost Model.

# TSS Developer support

## How to Define a TSS Cost Information

When creating a TSS, its cost information must be taken into account. In order to do that, the ElasTest Cost Engine (ECE) offers different APIs that help estimate the cost of them by using Cost Models. Every TJob could be composed of different TSSs, this TSSs will have a Cost Model associated to them since its creation, so the Cost can be ultimately computed.

### Cost Model

A Cost Model is a definition that usually describes attributes of a product or project so it can be used to estimate its cost.
 
In the ElasTest environment, every Service will have an associated Cost Model of its own, and the TJob costs will be accounted based on the services used in them. However, some Tests might need to use some external service which is not already in the ElasTest platform. If that is the case, an advanced feature of the ElasTest platform will be the ability to define an external cost models for those services.

### ElasTest Cost Model Definition

As the Cost Models are used to estimate the cost of a TJob, they are defined in a generic way. This allows different use cases to be estimated properly. The data model used is the following:

- name : String - Name of the Cost Model that will be displayed to the User. Helps identifying the Cost Model.
- type : String - Cost Model type (Currently supported “On Demand”, expected to be supported “PAYG” and “External”).
- fix_cost : Map containing the keys and values of the fields that need to be accounted in a fix bases. This fields will be included in the estimation cost regardless. Empty if there is no use of fixed cost elements. 
- var_rate : Map containing the keys and values of the fields that need to be accounted under a pay as you go premises. In order to make it understandable, all costs will be based on a minute basis. Empty if there is no use of variable rate elements. ECE accepts any value on this fields and will estimate the cost based on them, however we recommend using the same metrics that docker stats provide or the platform where ElasTest is deployed can provide and measure. At the moment the recommended values for clarity are are cpu_usage, mb_ram_usage, net_io_usage and block_io_usage .
- components : Map containing the links to other Cost Models (Currently supported “Services” as a key containing an array of Service Id’s). Empty if there is no use of separated components.
- description : String - Description of the Cost Model. Helps the user identifying the Cost Model.

An example of a Cost Model definition could be as the following:

```
{
  "name" : "CostModelX",
  "type" : "On Demand",
  "fix_cost" : {
    "deployment" : 12.5
  },
  "var_rate" : {
    "cpu_core" : 10, 
    "gb_ram" : 5, 
    "gb_ssd" : 0.6,
    "gb_hhd" : 0.0775
  },
  "components":{
    "Services" : ["CostModelYId"]
  },
  "description" : "On demand provisioning of a VM. Includes as a component a package X accounted by the Cost Model Y"
}
```

## How will the ECE Interact with the Cost Model

The ElasTest Cost Engine will receive the T-Job information from the TORM and will use the ESM APIs to query the cost information (in the expected format) of the services used in the T-Job.

The ECE will extract the information from the JSON body in order to compute the usage costs. In order to do that the ECE will use the “fix_cost” fields to simply add the present prices, the “var_rate” fields will be used in order to simulate several Pay As You Go (PAYG) situations (e.g. Different time running, different CPU Resources). The field “components” is used as a map referencing other cost models that also have to be taken into account, this way a single TJob cost model or TSS cost model can include different already-defined cost models.

## Advanced Features

We understand that a TJob may need to use complex services that might not be offered by the ElasTest Platform, however, an external service might be very difficult to monitor. In order to solve this, the ECE will allow to generate cost models that only contain fix costs under the “fix_cost” field. This way we ensure that if there is an existing cost for the external services that the developer knows in beforehand or a personal estimate for the use of the service. This data will be used to define a Cost model that the ECE that will be eligible as a TJob component as if it were a TSS so it can be used to estimate the final price.

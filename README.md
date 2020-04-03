# Poland: White list register of taxpayers

## Description
The Polish Tax authority publishes a so called White list - information about registered tax payers including the Tax number (NIP) and bank accounts. For certain contracts it is required that the payer transfers the funds to a white listed bank account of the payee. The White list is published on a daily basis, therefore the information about validity of the input pair is valid for current day only. 

The White list is published as .json file compressed with the 7zip algorithm. This application shows an example how to download the White list from the Tax office every day and serve the requests to validate the Tax number and Bank account number pairs.

This application is published as a minimum working example. You can use it as a starting point and adapt it to your business requirements.   

## Requirements
[Global account in the SAP Cloud Platform (SCP)](https://cloudplatform.sap.com/index.html)\
Cloud foundry Subaccount

### SCP Services
Application Runtime (with at least 3 GB memory)\
Application Logging (recommended) 

### Eclipse for JAVA Enterprise developers
[Download](https://www.eclipse.org/downloads/packages/) and install Eclipse for building JAVA application on your local PC. Follow the installation guide for possible implementation of additional libraries. 

### Oracle JAVA EE Development Kit (JDK)
The SDK might be already a part of the Eclipse bundle. If Eclipse fails to build the application due to missing JDK, [download](https://www.oracle.com/java/technologies/javaee-8-sdk-downloads.html) and install it on your local PC.  

### Apache Maven
Maven should be a part of the Eclipse bundle. If Eclipse fails to build the application due to missing Maven, [download](https://maven.apache.org/download.cgi) and install it on your local PC.

In case any other tool is needed, you might find the info how to obtain it on the [Tools](https://tools.hana.ondemand.com/#cloud) page.

## Download and installation
Run Eclipse. It will ask you for a Workspace directory. This will the common location for all JAVA projects.   
Create a new directory on your PC for the application inside the Workspace directory. This folder will be further referred as **APP_HOME**.\
[Download or Clone](https://help.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository) this repository into the APP_HOME directory.  

## Compilation
The source code contains a project file (.pom) which will be recognized by Eclipse. Open it and build the application. The dependent libraries should be automatically downloaded from a Maven repository during the build. You can reconfigure Eclipse to use a local Maven or Nexus directory. You might also need to configure Eclipse network settings if you are using a HTTP Proxy. 
The deployable artifact of the application should be created in APP_HOME/target with a .war suffix.  

## Deployment
Deploy the application (.war) into your organization Space. You can use the Deploy button in your Space in SCP Cockpit or use the [Cloud Foundry Command Line Interface - CLI](https://tools.hana.ondemand.com/#cloud).
The parameters of the application (e.g. Memory assignment) are set during the deployment. The repository contains an example manifest.yml in APP_HOME directory.
It is possible to use the manifest.yml directly after providing user-specific values. Make sure to save the manifest with unix-style line endings. E.g. replace\ 
name = _<app_name>_\
with a value, that is likely to be unique, e.g.\
name = my_company_com_pl_whitelist
    

# Consumption of the end points
After the application deployed and run, navigate to the application in your space and you will see the application routes in the URL form. This route will be further referred as **APP_ROUTE**.\
The application has exposed two endpoints, which are expected to be consumed in a client application.  
* APP_ROUTE/download : This endpoint can be called (via HTTP GET) to download the White list data for current date. The White list archive has about 200 MB, the client must set an appropriate timeout value (3 minutes is an absolute minimum) to prevent timeout during downloading. 
* APP_ROUTE/validate : This endpoint can be called (via HTTP GET) to validate the Tax Number/Bank account pairs. The format of the HTTP is described in the Request.xsd and Response.xsd in the APP_HOME directory. Processing error response (e.g. White list data not avaialble) is described in Error.xsd.        

# License
Copyright © 2020 SAP SE or an SAP affiliate company. All rights reserved. This project is licensed under the Apache Software License, version 2.0 except as noted otherwise in the [LICENSE](LICENSE.txt) file.

# Support
There is no support provided for this application.

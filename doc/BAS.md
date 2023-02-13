# Deployment with the Business Application Studio

[SAP Business Application Studio](https://help.sap.com/docs/SAP%20Business%20Application%20Studio/9d1db9835307451daa8c930fbd9ab264/8f46c6e6f86641cc900871c903761fd4.html) is a new SAP Business Technology Platform (SAP BTP) service that offers a modern development environment tailored for efficient development of business applications for the SAP Intelligent Enterprise.

* [Subscribe](https://help.sap.com/docs/SAP%20Business%20Application%20Studio/9d1db9835307451daa8c930fbd9ab264/6331319fd9ea4f0ea5331e21df329539.html) the Business Application Studio and make sure that all [necessary roles](https://help.sap.com/docs/SAP%20Business%20Application%20Studio/9d1db9835307451daa8c930fbd9ab264/01e69c53003c4b0a8a64310a3f08867d.html) are added to your user. 
* Run the BAS and create a space for Full Stack Cloud Application development
* Within your development space, [clone] the (https://github.com/SAP-samples/localization-toolkit-s4hana-cloud-bank-account-validation). 
* The repository contains an example manifest_bas.yml. Edit it and replace the name = _<app_name>_ with a value, that is likely to be unique, e.g. name = my_company_com_pl_whitelist
* Open the teminal using the main menu -> View -> Teminal
* In the terminal, change the working directory to the app home e.g. using the command `cd ~/projects/localization-toolkit-s4hana-cloud-bank-account-validation/`
* Execute the installation script using the command `sh install.sh` 

## What the described script does
The script contains the following commands which can also be executed manually, if needed.
* `mvn clean install` will compile the source code into a deploayble artifact. 
* `cf api https://api.cf.sap.hana.ondemand.com` will tell the BAS to communicate with the the SAP BTP Cloud Foundry API endpoint.
* `cf login` will login you into the BTP. You will be prompted for user name and password. If you have an access to multiple BTP subaccounts/spaces, you will be also promted to select one. 
* `cf push -f src/manifest_bas.yml` will deploy the application to the space selected in the previous step and start the application. If you receive an error about unsufficient memory, you might need to delete the old instance of this application first. For the deployment, the `manifest_bas.yml` will be used which already contains necessary settings. 

## Consumption of the end points
Continue with the setup of the consumption of the end points described on the [main page](../README.md). 

The guide leverages the [Cloud Foundry Command Line Interface - CLI](https://tools.hana.ondemand.com/#cloud). A useful tutorial on the CLI can be found [here](https://github.com/SAP-samples/hana-developer-cli-tool-example). 
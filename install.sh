#! usr/bin/bash
mvn clean install
cf api https://api.cf.sap.hana.ondemand.com
cf login
cf push -f src/manifest_bas.yml
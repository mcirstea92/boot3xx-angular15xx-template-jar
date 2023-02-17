#!/bin/bash
# A Bash script that reboots the OS
mvn clean
mvn install -DskipTests -T4C
cd template-backend/target && java -jar template.jar
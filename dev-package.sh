#!/bin/sh

mvn clean package -Dmaven.test.skip=true -Pdev -e

#!/bin/bash

export JAVA_BIN_DIR=/usr/java/latest/
export PYTHONPATH=/usr/local/lib64/python2.7/site-packages/
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib/
export PATH=$PATH:/usr/local/bin/

java -jar dist/FittingWizard.jar


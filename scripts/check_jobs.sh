#!/bin/bash

user=$USER
cluster="verdi.chemie.unibas.ch"
stop=0
myid=$1

while true;
do 
    out=$(ssh $cluster "qstat -u $user"| grep $myid | awk '{print $1}')
    arr=($out)
    size=${#arr[@]}
    if [ "$size" -eq "$stop" ]; then
        break
    fi
    echo "$size jobs still running ; sleeping 10 seconds and then looping ..."
    sleep 10
done

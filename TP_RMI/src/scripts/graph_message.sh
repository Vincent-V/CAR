#!/bin/bash

cd ../../bin
export CLASSPATH=$CLASSPATH:server/:client/

#Lancement servers
java tests.ServerMainGraph &


toKill=$!
sleep 0.1

#Generation du graph
java tests.CreateGraph

echo "Transfer from node 1"
java tests.BroadcastMessage 1 messageTest

sleep 2

echo "Transfer from node 3"
java tests.BroadcastMessage 3 messageTest

sleep 0.1
kill $toKill


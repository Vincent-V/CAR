#!/bin/bash

cd ../../bin
export CLASSPATH=$CLASSPATH:server/:client/

#Lancement servers
java tests.ServerMainTree &
echo "[OK] Lancement du serveur"

toKill=$!
sleep 0.1

#Generation de l'arbre
java tests.CreateTree 
echo "[OK] Création de l'arbre"


kill $toKill
echo "[OK] Kill du serveur"

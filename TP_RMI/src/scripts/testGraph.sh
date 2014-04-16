#!/bin/bash

cd ../../bin
export CLASSPATH=$CLASSPATH:server/:client/

#Lancement servers
java tests.ServerMainGraph > output &
toKill=$!
sleep 0.1

#Generation de l'arbre
java tests.CreateGraph 

#################### PREMIER TEST SIMPLE #############################

#envoi d'un message
java tests.BroadcastMessage 1 messageTest

#test du resultat
sleep 0.1
if [[ `cat output | grep -a messageTest | sort |  uniq | wc -l` == 6 ]]
	then echo "[OK]    TESTS SIMPLE  "
	else echo "[ERROR] TESTS SIMPLE  "
fi

#on vide le fichier
> output

#################### TEST DEPUIS TOUS LES NOEUDS #############################

for i in $(seq 1 6)
do

	java tests.BroadcastMessage $i messageTest$i
	sleep 0.1
	if [[ `cat output | grep -a messageTest$i | sort | uniq | wc -l` == 6 ]]
		then echo "[OK]    TESTS DEPUIS NOEUD $i"
		else echo "[ERROR] TESTS DEPUIS NOEUD $i"
	fi
	> output
done


kill $toKill

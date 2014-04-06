#!/bin/bash

cd ../bin
export CLASSPATH=$CLASSPATH:server/:client/

echo "Lancement serveur..."
java tests.ServerMainTree > ../src/output &
toKill=$!
sleep 1
echo "[OK]"

echo "****"

echo "CreateTree..."
java tests.CreateTree 
echo "[OK]"

echo "****"

echo "Envoi message..."
java tests.BroadcastMessage
echo "[OK]"

echo "****"

echo "Extinction serveur..."
kill $toKill
echo "[OK]"

echo "**********"
echo "**RESULT**"
echo "**********"

if [[ `cat ../src/output | grep totoro | uniq | wc -l` == 6 ]]
	then echo "TESTS TREE  [OK]"
	else echo "TESTS TREE  [ERROR]"
fi


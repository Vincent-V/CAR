#!/bin/bash

cd ../bin
export CLASSPATH=$CLASSPATH:server/:client/

echo "Lancement serveur..."
java tests.ServerMainGraph > ../src/output &
toKill=$!
sleep 1
echo "[OK]"

echo "****"

echo "CreateGraph..."
java tests.CreateGraph 
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
	then echo "TESTS Graph [OK]"
	else echo "TESTS Graph [ERROR]"
	
fi


#!/bin/sh

echo "Starting all the services. Assuming maven build is already complete and executeable jars reside in target folder"
echo "Start up order"


echo " 1. DISCOVERY-SERVER "

echo " 2. API-GATEWAY "

echo " 3. AUTH "

echo " 4. USER "

echo " 5. LOAN "

echo " 6. PAYMENT "

echo "Starting discovery-server .."


cd discovery-server/target
java -jar discovery-server*.jar &
echo ""
echo "wait for discovery-server to start .."
echo ""
//wait for 10 seconds for server to start
sleep 10
cd ../../

echo "Starting api-gateway .."
echo ""
cd api-gateway/target
java -jar api-gateway*.jar &
cd ../../
echo ""

echo "Starting auth .."
echo ""
cd auth/target
java -jar auth*.jar &
cd ../../
echo ""

echo "Starting user .."
echo ""
cd user/target
java -jar user*.jar &
cd ../../
echo ""

echo "Starting loan .."
echo ""
cd loan/target
java -jar loan*.jar &
cd ../../
echo ""

echo "Starting payment .."
echo ""
cd payment/target
java -jar payment*.jar &
cd ../../
echo ""


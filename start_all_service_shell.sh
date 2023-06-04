#!/bin/sh

echo "Starting all the services. Assuming maven build is already complete and executeable jars reside in target folder"
echo "========Start up order========"
echo ""

echo "========discovery-server"
echo ""
echo "========api-gateway"
echo ""
echo "========auth"
echo ""
echo "========user"
echo ""
echo "========loan"
echo ""

echo "Starting discovery-server .."
echo ""

cd discovery-server/target
java -jar discovery-server*.jar &
echo "server starting in different window"
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
echo "server starting in different window"
cd ../../
echo ""

echo "Starting auth .."
echo ""
cd auth/target
java -jar auth*.jar &
echo "server starting in different window"
cd ../../
echo ""

echo "Starting user .."
echo ""
cd user/target
java -jar user*.jar &
echo "server starting in different window"
cd ../../
echo ""

echo "Starting loan .."
echo ""
cd loan/target
java -jar loan*.jar &
echo "server starting in different window"
cd ../../
echo ""


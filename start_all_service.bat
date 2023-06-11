echo off

echo "\tStarting all the services. Assuming maven build is already complete and executeable jars reside in target folder\n\n"
echo "\tStart up order\n"


echo "\t 1. DISCOVERY-SERVER \n"

echo "\t API-GATEWAY \n"

echo "\t AUTH \n"

echo "\t USER \n"

echo "\t LOAN \n"


echo "Starting discovery-server .. \n"


cd discovery-server/target
for /f "delims=" %%x in ('dir /od /b discovery-server*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window"

echo "wait for discovery-server to start .. \n"

//hack to wait for 10 seconds
ping -n 10 127.0.0.1 > nul
cd ../../

echo "Starting api-gateway .. \n"

cd api-gateway/target
for /f "delims=" %%x in ('dir /od /b api-gateway*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window \n"
cd ../../


echo "Starting auth .."

cd auth/target
for /f "delims=" %%x in ('dir /od /b auth*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window \n"
cd ../../


echo "Starting user .."

cd user/target
for /f "delims=" %%x in ('dir /od /b user*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window \n"
cd ../../


echo "Starting loan .."

cd loan/target
for /f "delims=" %%x in ('dir /od /b loan*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window \n"
cd ../../


cmd /k ipconfig
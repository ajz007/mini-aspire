echo off

echo "Starting all the services. Assuming maven build is already complete and executeable jars reside in target folder"
echo "Start up order"


echo " 1. DISCOVERY-SERVER "

echo " 2. API-GATEWAY "

echo " 3. AUTH "

echo " 4. USER "

echo " 5. LOAN "

echo " 6. PAYMENT "


echo "Starting discovery-server .. "


cd discovery-server/target
for /f "delims=" %%x in ('dir /od /b discovery-server*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window"

echo "wait for discovery-server to start .. "

//hack to wait for 10 seconds
ping -n 10 127.0.0.1 > nul
cd ../../

echo "Starting api-gateway .. "

cd api-gateway/target
for /f "delims=" %%x in ('dir /od /b api-gateway*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window "
cd ../../


echo "Starting auth .."

cd auth/target
for /f "delims=" %%x in ('dir /od /b auth*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window "
cd ../../


echo "Starting user .."

cd user/target
for /f "delims=" %%x in ('dir /od /b user*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window "
cd ../../


echo "Starting loan .."

cd loan/target
for /f "delims=" %%x in ('dir /od /b loan*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window "
cd ../../

echo "Starting payment .."

cd payment/target
for /f "delims=" %%x in ('dir /od /b payment*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window "
cd ../../


cmd /k echo "Done!"
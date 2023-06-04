echo off

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
for /f "delims=" %%x in ('dir /od /b discovery-server*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window"
echo ""
echo "wait for discovery-server to start .."
echo ""
//hack to wait for 10 seconds
ping -n 10 127.0.0.1 > nul
cd ../../

echo "Starting api-gateway .."
echo ""
cd api-gateway/target
for /f "delims=" %%x in ('dir /od /b api-gateway*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window"
cd ../../
echo ""

echo "Starting auth .."
echo ""
cd auth/target
for /f "delims=" %%x in ('dir /od /b auth*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window"
cd ../../
echo ""

echo "Starting user .."
echo ""
cd user/target
for /f "delims=" %%x in ('dir /od /b user*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window"
cd ../../
echo ""

echo "Starting loan .."
echo ""
cd loan/target
for /f "delims=" %%x in ('dir /od /b loan*.jar') do set latestjar=%%x
start cmd /k java -jar %latestjar%
echo "server starting in different window"
cd ../../
echo ""

cmd /k ipconfig
mkdir -p out
javac $(find . -name "*.java")
cd client
jar cfe ../out/client.jar FTPClient *.class
cd ../server
jar cfe ../out/server.jar FTPServer *.class
cd ..
find . -name "*.class" -type f -delete
echo 'OK'

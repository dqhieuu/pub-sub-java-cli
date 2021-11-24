mkdir -p out
javac $(find . -name "*.java")
jar cfe ./out/broker.jar ./broker MQTTBroker *.class
find . -name "*.class" -type f -delete
echo 'OK'

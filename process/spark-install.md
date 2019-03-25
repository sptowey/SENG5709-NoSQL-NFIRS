Adapted from Medium's Intalling Scala and Spark article (https://medium.com/@josemarcialportilla/installing-scala-and-spark-on-ubuntu-5665ee4b62b1)

1. Make sure Java is installed (see druid-install.md for more info)
2. Update packages

```sudo apt-get update```

3. Install Scala

```
sudo apt-get install scala
scala
println("Hello World")
:q
```
4. Install Git

```sudo apt-get install git```

5. Download and install Spark

```
curl https://www-us.apache.org/dist/spark/spark-2.4.0/spark-2.4.0-bin-hadoop2.7.tgz -o spark-2.4.0-bin-hadoop2.7.tgz
tar -xvf spark-2.4.0-bin-hadoop2.7.tgz
cd spark-2.4.0-bin-hadoop2.7/
cd bin 
export __JAVA_OPTIONS='-Xmx8096M -Xms512M'
./spark-shell --driver-memory 8g
println("Spark shell is running")
Ctrl+C
```

Adapted from Medium's [Intalling Scala and Spark](https://medium.com/@josemarcialportilla/installing-scala-and-spark-on-ubuntu-5665ee4b62b1) article and [sbt documentation](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html)

*All commands should be run from the user's home directory.

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
6. Install sbt

```
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt-get update
sudo apt-get install sbt
```

7. Install zip
```sudo apt-get install zip unzip```

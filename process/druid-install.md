Adapted from Druid's [Getting started](http://druid.io/docs/latest/tutorials/index.html) instructions

1. Create Ubuntu 18.04 VM (Azure Standard D2s v3 (2 vcpus, 8 GB memory))
2. Install Java (needs Java 8, this installed Java 10)
   ```
   sudo add-apt-repository ppa:webupd8team/java
   sudo apt-get update
   sudo apt-get install oracle-java8-installer
   ```
3. Download and install Druid
   ```
   cd /bin
   sudo mkdir druid
   cd druid
   sudo curl http://apache.mirrors.ionfish.org/incubator/druid/0.13.0-incubating/apache-druid-0.13.0-incubating-bin.tar.gz -o apache-druid-0.13.0-incubating-bin.tar.gz
   sudo tar -xzf apache-druid-0.13.0-incubating-bin.tar.gz
   sudo rm apache-druid-0.13.0-incubating-bin.tar.gz
   cd apache-druid-0.13.0-incubating
   ```
4. Download and install Zookeeper
   ```
   sudo curl https://archive.apache.org/dist/zookeeper/zookeeper-3.4.11/zookeeper-3.4.11.tar.gz -o zookeeper-3.4.11.tar.gz
   sudo tar -xzf zookeeper-3.4.11.tar.gz
   sudo rm zookeeper-3.4.11.tar.gz
   sudo mv zookeeper-3.4.11 zk
   ```
 5. Run Druid
   `sudo bin/supervise -c quickstart/tutorial/conf/tutorial-cluster.conf`

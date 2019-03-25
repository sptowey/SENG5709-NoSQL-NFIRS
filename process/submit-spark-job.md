1. Make sure Spark and sbt are installed. (All commands will assume Spark is installed in the user's home directory).
2. Make sure all of the original csv files from the Enigma Public source are located in the ~/NFIRS directory
3. In user's home directory, run the following commands to set up the proper folder hierarchy for an sbt project.

```
mkdir nfirs-build
cd nfirs-build
mkdir src
cd src
mkdir main
cd main
mkdir scala
cd scala
mkdir nfirs
```
4. Copy the build.sbt file from this repository into the nfirs-build directory.
5. Copy the SparkLoadAndTransform.scala file from this repository into the nfirs-build/src/main/scala/nfirs directory.
6. From the nfirs-build directory run the following command:

```sbt package```

7. From the user's home directory run the following command to submit the Spark job:

```spark-2.4.0-bin-hadoop2.7/bin/spark-submit --class SparkLoadAndTransform --master local nfirs-build/target/scala-2.11/nfirs_2.11-0.1-SNAPSHOT.jar ""```

8. Once the job has completed, the exported csv files will show up in the ~/sparkNfirs/combinedIncidentsAllYears directory.

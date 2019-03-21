 import org.apache.spark.sql.SQLContext
 val sqlContext = new SQLContext(sc)

val generalIncidents2009 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2009.csv")
val generalIncidents2009FixedDate = generalIncidents2009.withColumn("inc_date", when(length(col("inc_date")) === 7, concat(lit("0"), col("inc_date"))).otherwise(col("inc_date")))
generalIncidents2009FixedDate.select($"inc_date").show()
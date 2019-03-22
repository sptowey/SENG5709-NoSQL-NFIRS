 import org.apache.spark.sql.SQLContext
 val sqlContext = new SQLContext(sc)

val generalIncidents2009 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2009.csv")
val generalIncidents2009FixedDate = generalIncidents2009.withColumn("inc_date", when(length(col("inc_date")) === 7, concat(lit("0"), col("inc_date"))).otherwise(col("inc_date")))
generalIncidents2009FixedDate.select($"inc_date").show()

 val generalIncidents2010 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2010.csv")
 val generalIncidents2010FixedDate = generalIncidents2010.withColumn("inc_date", when(length(col("inc_date")) === 7, concat(lit("0"), col("inc_date"))).otherwise(col("inc_date"
)))

generalIncidents2010FixedDate.select($"inc_date").show()

val generalIncidents2011 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2011.csv")
val generalIncidents2011FixedDate = generalIncidents2011.withColumn("inc_date", when(length(col("inc_date")) === 7, concat(lit("0"), col("inc_date"))).otherwise(col("inc_date"
)))

generalIncidents2011FixedDate.select($"inc_date").show()

val combinedInicidentsOldFormatDf = generalIncidents2009FixedDate.union(generalIncidents2010FixedDate).union(generalIncidents2011FixedDate)
combinedInicidentsOldFormatDf.select($"inc_date").show()
combinedInicidentsOldFormatDf.count

val generalIncidents2012 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2012.csv")
generalIncidents2012.select($"inc_date").show()

val generalIncidents2013 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2013.csv")
generalIncidents2013.select($"inc_date").show()

val generalIncidents2014 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2014.csv")
generalIncidents2014.select($"inc_date").show()

val combinedInicidentsNewFormatDf = generalIncidents2012.union(generalIncidents2013).union(generalIncidents2014)
combinedInicidentsNewFormatDf.select($"inc_date").show()
combinedInicidentsNewFormatDf.count
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

val combinedIncidentsOldFormatDf = generalIncidents2009FixedDate.union(generalIncidents2010FixedDate).union(generalIncidents2011FixedDate)
combinedIncidentsOldFormatDf.select($"inc_date").show()
combinedIncidentsOldFormatDf.count

val generalIncidents2012 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2012.csv")
generalIncidents2012.select($"inc_date").show()

val generalIncidents2013 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2013.csv")
generalIncidents2013.select($"inc_date").show()

val generalIncidents2014 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2014.csv")
generalIncidents2014.select($"inc_date").show()

val combinedIncidentsNewFormatDf = generalIncidents2012.union(generalIncidents2013).union(generalIncidents2014)
combinedIncidentsNewFormatDf.select($"inc_date").show()
combinedIncidentsNewFormatDf.count

val oldFormatColumnNames =  combinedIncidentsOldFormatDf.schema.names
val newFormatColumnNames = combinedIncidentsNewFormatDf.schema.names

val columnsInOldButNotNewFormat = oldFormatColumnNames.diff(newFormatColumnNames)
val columnsInNewButNotOldFormat = newFormatColumnNames.diff(oldFormatColumnNames)
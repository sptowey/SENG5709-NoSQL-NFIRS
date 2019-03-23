 import org.apache.spark.sql.SQLContext
 val sqlContext = new SQLContext(sc)

val generalIncidents2009 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2009.csv")
val generalIncidents2010 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2010.csv")
val generalIncidents2011 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2011.csv")

val combinedIncidentsOldFormatDf = generalIncidents2009.union(generalIncidents2010).union(generalIncidents2011)
val combinedIncidentsOldFormatFixedDatesDf = combinedIncidentsOldFormatDf
    .withColumn("inc_date", when(length(col("inc_date")) === 7, concat(lit("0"), col("inc_date"))).otherwise(col("inc_date")))
    .withColumn("alarm", when(length(col("alarm")) === 11, concat(lit("0"), col("alarm"))).otherwise(col("alarm")))
    .withColumn("arrival", when(length(col("arrival")) === 11, concat(lit("0"), col("arrival"))).otherwise(col("arrival")))
    .withColumn("inc_cont", when(length(col("inc_cont")) === 11, concat(lit("0"), col("inc_cont"))).otherwise(col("inc_cont")))
    .withColumn("lu_clear", when(length(col("lu_clear")) === 11, concat(lit("0"), col("lu_clear"))).otherwise(col("lu_clear")))

val combinedIncidentsOldFormattedDateDf = combinedIncidentsOldFormatFixedDatesDf
    .withColumn("inc_date", to_timestamp($"inc_date", "MMddyyyy"))
    .withColumn("alarm", to_timestamp($"alarm", "MMddyyyyhhmm"))
    .withColumn("arrival", to_timestamp($"arrival", "MMddyyyyhhmm"))
    .withColumn("inc_cont", to_timestamp($"inc_cont", "MMddyyyyhhmm"))
    .withColumn("lu_clear", to_timestamp($"lu_clear", "MMddyyyyhhmm"))

val generalIncidents2012 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2012.csv")
val generalIncidents2013 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2013.csv")
val generalIncidents2014 = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load("NFIRS/GeneralIncidentInformation2014.csv")

val combinedIncidentsNewFormatDf = generalIncidents2012.union(generalIncidents2013).union(generalIncidents2014)

combinedIncidentsNewFormatDf.select($"inc_date", $"alarm", $"arrival", $"inc_cont", $"lu_clear").show()
combinedIncidentsNewFormatDf.count

val oldFormatColumnNames =  combinedIncidentsOldFormatDf.schema.names
val newFormatColumnNames = combinedIncidentsNewFormatDf.schema.names

val columnsInOldButNotNewFormat = oldFormatColumnNames.diff(newFormatColumnNames)
val columnsInNewButNotOldFormat = newFormatColumnNames.diff(oldFormatColumnNames)

val combinedIncidentsOldFinalDf = combinedIncidentsOldFormattedDateDf.drop("serialid");
combinedIncidentsOldFinalDf.select($"inc_date", $"alarm", $"arrival", $"inc_cont", $"lu_clear").show()
combinedIncidentsOldFinalDf.count

val combinedIncidentsNewFinalDf = combinedIncidentsNewFormatDf
    .drop("state_definition")
    .drop("inc_date_unparsed")
    .drop("inc_type_definition")       
    .drop("aid_definition")
    .drop("alarm_unparsed")
    .drop("arrival_unparsed")      
    .drop("inc_cont_unparsed")
    .drop("lu_clear_unparsed")
    .drop("act_tak1_definition")      
    .drop("act_tak2_definition")
    .drop("act_tak3_definition")
    .drop("det_alert_definition")      
    .drop("haz_rel_definition")
    .drop("mixed_use_definition")
    .drop("prop_use_definition")      
combinedIncidentsNewFinalDf.select($"inc_date", $"alarm", $"arrival", $"inc_cont", $"lu_clear").show()
combinedIncidentsNewFinalDf.count

val combinedIncidentsAllYearsDf = combinedIncidentsOldFinalDf.union(combinedIncidentsNewFinalDf);
combinedIncidentsAllYearsDf.select($"inc_date", $"alarm", $"arrival", $"inc_cont", $"lu_clear").show()
combinedIncidentsAllYearsDf.count
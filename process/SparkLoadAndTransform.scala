import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark;
import org.apache.spark.sql.functions._
 
 object SparkLoadAndTransform extends Serializable {
    
    def loadGeneralIncidentDfFromCsv(sqlContext: SQLContext, generalIncidentsFilePath: String, fireDepartmentsFilePath: String) : DataFrame = {       
        val generalIncidents = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load(generalIncidentsFilePath)
        val fireDepartments = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load(fireDepartmentsFilePath)
        val fireDepartmentsReducedColumns = fireDepartments.select(col("state"), col("fdid"), col("fd_name")).distinct()
        val joinExpression = generalIncidents.col("fdid") === fireDepartmentsReducedColumns.col("fdid") && generalIncidents.col("state") === fireDepartmentsReducedColumns("state")
        val joinType = "left_outer"
        val generalIncidentsWithFD = generalIncidents.join(fireDepartmentsReducedColumns, joinExpression, joinType)
            .drop(fireDepartmentsReducedColumns.col("fdid")).drop(fireDepartmentsReducedColumns.col("state"))

        return generalIncidentsWithFD;
    }

     def main(args: Array[String]) = {

        val pathToDataFolder = args(0)

        // start up the SparkSession
        val spark = SparkSession.builder().appName("NFIRS Load and Transform").getOrCreate()
        import spark.implicits._

        val generalIncidents2009 = loadGeneralIncidentDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/GeneralIncidentInformation2009.csv", pathToDataFolder + "NFIRS/FireDepartments2009.csv")
        val generalIncidents2010 = loadGeneralIncidentDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/GeneralIncidentInformation2010.csv", pathToDataFolder + "NFIRS/FireDepartments2010.csv")
        val generalIncidents2011 = loadGeneralIncidentDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/GeneralIncidentInformation2011.csv", pathToDataFolder + "NFIRS/FireDepartments2011.csv")

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

        val generalIncidents2012 = loadGeneralIncidentDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/GeneralIncidentInformation2012.csv", pathToDataFolder + "NFIRS/FireDepartments2012.csv")
        val generalIncidents2013 = loadGeneralIncidentDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/GeneralIncidentInformation2013.csv", pathToDataFolder + "NFIRS/FireDepartments2013.csv")
        val generalIncidents2014 = loadGeneralIncidentDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/GeneralIncidentInformation2014.csv", pathToDataFolder + "NFIRS/FireDepartments2014.csv")
        
        val combinedIncidentsNewFormatDf = generalIncidents2012.union(generalIncidents2013).union(generalIncidents2014)

        val oldFormatColumnNames =  combinedIncidentsOldFormatDf.schema.names
        val newFormatColumnNames = combinedIncidentsNewFormatDf.schema.names

        val columnsInOldButNotNewFormat = oldFormatColumnNames.diff(newFormatColumnNames)
        val columnsInNewButNotOldFormat = newFormatColumnNames.diff(oldFormatColumnNames)

        val combinedIncidentsOldFinalDf = combinedIncidentsOldFormattedDateDf.drop("serialid");

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

        val combinedIncidentsAllYearsDf = combinedIncidentsOldFinalDf.union(combinedIncidentsNewFinalDf);

        combinedIncidentsAllYearsDf.write.option("header", "true").csv(pathToDataFolder + "sparkNfirs/combinedIncidentsAllYears")
    }
}
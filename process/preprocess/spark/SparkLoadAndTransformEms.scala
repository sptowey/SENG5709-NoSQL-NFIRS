import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark;
import org.apache.spark.sql.functions._
 
 object SparkLoadAndTransformEms extends Serializable {
    
    def loadEmsDfFromCsv(sqlContext: SQLContext, emsFilePath: String, generalIncidentsPath: String) : DataFrame = {       
        val ems = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load(emsFilePath)
        val generalIncidents = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load(generalIncidentsPath)
        val generalIncidentsReducedColumns = generalIncidents.select(col("state"), col("fdid"), col("inc_no"), col("exp_no"), col("inc_type")).distinct()
        val joinExpression = ems.col("fdid") === generalIncidentsReducedColumns.col("fdid") && 
            ems.col("state") === generalIncidentsReducedColumns("state") && 
            ems.col("inc_no") === generalIncidentsReducedColumns("inc_no") &&
            ems.col("exp_no") === generalIncidentsReducedColumns("exp_no")
        val joinType = "left_outer"
        val emsWithIncType = ems.join(generalIncidentsReducedColumns, joinExpression, joinType)
            .drop(generalIncidentsReducedColumns.col("fdid"))
            .drop(generalIncidentsReducedColumns.col("state"))
            .drop(generalIncidentsReducedColumns.col("inc_no"))
            .drop(generalIncidentsReducedColumns.col("exp_no"))

        return emsWithIncType;
    }

     def main(args: Array[String]) = {

        val pathToDataFolder = args(0)

        // start up the SparkSession
        val spark = SparkSession.builder().appName("NFIRS Load and Transform").getOrCreate()
        import spark.implicits._

        val ems2009 = loadEmsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/EmergencyMedicalServices2009.csv", pathToDataFolder + "NFIRS/GeneralIncidentInformation2009.csv")
        val ems2010 = loadEmsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/EmergencyMedicalServices2010.csv", pathToDataFolder + "NFIRS/GeneralIncidentInformation2010.csv")
        val ems2011 = loadEmsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/EmergencyMedicalServices2011.csv", pathToDataFolder + "NFIRS/GeneralIncidentInformation2011.csv")

        val emsOldFormatDf = ems2009.union(ems2010).union(ems2011)
        val emsOldFormatFixedDatesDf = emsOldFormatDf
            .withColumn("inc_date", when(length(col("inc_date")) === 7, concat(lit("0"), col("inc_date"))).otherwise(col("inc_date")))
            .withColumn("arrival", when(length(col("arrival")) === 7, concat(lit("0"), col("arrival"))).otherwise(col("arrival")))
            .withColumn("transport", when(length(col("transport")) === 7, concat(lit("0"), col("transport"))).otherwise(col("transport")))
        val combinedEmsOldFormattedDateDf = emsOldFormatFixedDatesDf.withColumn("inc_date", to_timestamp($"inc_date", "MMddyyyy"))


        val ems2012 = loadEmsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/EmergencyMedicalServices2012.csv", pathToDataFolder + "NFIRS/GeneralIncidentInformation2012.csv")
        val ems2013 = loadEmsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/EmergencyMedicalServices2013.csv", pathToDataFolder + "NFIRS/GeneralIncidentInformation2013.csv")
        val ems2014 = loadEmsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/EmergencyMedicalServices2014.csv", pathToDataFolder + "NFIRS/GeneralIncidentInformation2014.csv")
        
        val combinedEmsNewFormatDf = ems2012.union(ems2013).union(ems2014)

        val oldFormatColumnNames =  emsOldFormatDf.schema.names
        val newFormatColumnNames = combinedEmsNewFormatDf.schema.names

        val columnsInOldButNotNewFormat = oldFormatColumnNames.diff(newFormatColumnNames)
        val columnsInNewButNotOldFormat = newFormatColumnNames.diff(oldFormatColumnNames)

        val combinedEmsOldFinalDf = combinedEmsOldFormattedDateDf.drop("serialid");

        val combinedEmsNewFinalDf = combinedEmsNewFormatDf
            .drop("state_definition")
            .drop("inc_date_unparsed")
            .drop("arrival_unparsed")
            .drop("transport_unparsed")
            .drop("provider_a_definition")
            .drop("gender_definition")
            .drop("race_definition")
            .drop("eth_ems_definition")
            .drop("hum_fact1_definition")
            .drop("hum_fact2_definition")
            .drop("hum_fact3_definition")
            .drop("hum_fact4_definition")
            .drop("hum_fact5_definition")
            .drop("hum_fact6_definition")
            .drop("hum_fact7_definition")
            .drop("hum_fact8_definition")
            .drop("other_fact_definition")
            .drop("site_inj1_definition")
            .drop("site_inj2_definition")
            .drop("site_inj3_definition")
            .drop("site_inj4_definition")
            .drop("site_inj5_definition")
            .drop("inj_type1_definition")
            .drop("inj_type2_definition")
            .drop("inj_type3_definition")
            .drop("inj_type4_definition")
            .drop("inj_type5_definition")
            .drop("cause_ill_definition")
            .drop("proc_use1_definition")
            .drop("proc_use2_definition")
            .drop("proc_use3_definition")
            .drop("proc_use4_definition")
            .drop("proc_use5_definition")
            .drop("proc_use6_definition")
            .drop("proc_use7_definition")
            .drop("proc_use8_definition")
            .drop("proc_use9_definition")
            .drop("proc_use10_definition")
            .drop("proc_use11_definition")
            .drop("proc_use12_definition")
            .drop("proc_use13_definition")
            .drop("proc_use14_definition")
            .drop("proc_use15_definition")
            .drop("proc_use16_definition")
            .drop("proc_use17_definition")
            .drop("proc_use18_definition")
            .drop("proc_use19_definition")
            .drop("proc_use20_definition")
            .drop("proc_use21_definition")
            .drop("proc_use22_definition")
            .drop("proc_use23_definition")
            .drop("proc_use24_definition")
            .drop("proc_use25_definition")
            .drop("safe_eqp1_definition")
            .drop("safe_eqp2_definition")
            .drop("safe_eqp3_definition")
            .drop("safe_eqp4_definition")
            .drop("safe_eqp5_definition")
            .drop("safe_eqp6_definition")
            .drop("safe_eqp7_definition")
            .drop("safe_eqp8_definition")
            .drop("arrest_definition")
            .drop("arr_des1_definition")
            .drop("arr_des2_definition")
            .drop("ar_rhythm_definition")
            .drop("il_care_definition")
            .drop("high_care_definition")
            .drop("pat_status_definition")
            .drop("pulse_definition")
            .drop("ems_dispo_definition")         

        val combinedEmsAllYearsDf = combinedEmsOldFinalDf.union(combinedEmsNewFinalDf);

        combinedEmsAllYearsDf.write.option("header", "true").csv(pathToDataFolder + "sparkNfirs/combinedEmsAllYears")
    }
}
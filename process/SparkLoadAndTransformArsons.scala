import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark;
import org.apache.spark.sql.functions._
 
 object SparkLoadAndTransformArsons extends Serializable {
    
    def loadArsonsDfFromCsv(sqlContext: SQLContext, arsonsFilePath: String, fireDepartmentsFilePath: String) : DataFrame = {       
        val arsons = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load(arsonsFilePath)
        val fireDepartments = sqlContext.read.format("csv").option("header","true").option("inferSchema", "true").load(fireDepartmentsFilePath)
        val fireDepartmentsReducedColumns = fireDepartments.select(col("state"), col("fdid"), col("fd_name")).distinct()
        val joinExpression = arsons.col("fdid") === fireDepartmentsReducedColumns.col("fdid") && arsons.col("state") === fireDepartmentsReducedColumns("state")
        val joinType = "left_outer"
        val arsonsWithFD = arsons.join(fireDepartmentsReducedColumns, joinExpression, joinType)
            .drop(fireDepartmentsReducedColumns.col("fdid")).drop(fireDepartmentsReducedColumns.col("state"))

        return arsonsWithFD;
    }

     def main(args: Array[String]) = {

        val pathToDataFolder = args(0)

        // start up the SparkSession
        val spark = SparkSession.builder().appName("NFIRS Load and Transform").getOrCreate()
        import spark.implicits._

        val arsons2009 = loadArsonsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/Arson2009.csv", pathToDataFolder + "NFIRS/FireDepartments2009.csv")
        val arsons2010 = loadArsonsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/Arson2010.csv", pathToDataFolder + "NFIRS/FireDepartments2010.csv")
        val arsons2011 = loadArsonsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/Arson2011.csv", pathToDataFolder + "NFIRS/FireDepartments2011.csv")

        val arsonsOldFormatDf = arsons2009.union(arsons2010).union(arsons2011)
        val arsonsOldFormatFixedDatesDf = arsonsOldFormatDf.withColumn("inc_date", when(length(col("inc_date")) === 7, concat(lit("0"), col("inc_date"))).otherwise(col("inc_date")))
        val combinedArsonsOldFormattedDateDf = arsonsOldFormatFixedDatesDf.withColumn("inc_date", to_timestamp($"inc_date", "MMddyyyy"))


        val arsons2012 = loadArsonsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/Arson2012.csv", pathToDataFolder + "NFIRS/FireDepartments2012.csv")
        val arsons2013 = loadArsonsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/Arson2013.csv", pathToDataFolder + "NFIRS/FireDepartments2013.csv")
        val arsons2014 = loadArsonsDfFromCsv(spark.sqlContext, pathToDataFolder + "NFIRS/Arson2014.csv", pathToDataFolder + "NFIRS/FireDepartments2014.csv")
        
        val combinedArsonsNewFormatDf = arsons2012.union(arsons2013).union(arsons2014)

        val oldFormatColumnNames =  arsonsOldFormatDf.schema.names
        val newFormatColumnNames = combinedArsonsNewFormatDf.schema.names

        val columnsInOldButNotNewFormat = oldFormatColumnNames.diff(newFormatColumnNames)
        val columnsInNewButNotOldFormat = newFormatColumnNames.diff(oldFormatColumnNames)

        val combinedArsonsOldFinalDf = combinedArsonsOldFormattedDateDf.drop("serialid");

        val combinedArsonsNewFinalDf = combinedArsonsNewFormatDf
            .drop("state_definition")
            .drop("inc_date_unparsed")
            .drop("case_stat_definition")
            .drop("avail_mfi_definition")
            .drop("mot_facts1_definition")
            .drop("mot_facts2_definition")
            .drop("mot_facts3_definition")
            .drop("grp_invol1_definition")
            .drop("grp_invol2_definition")
            .drop("grp_invol3_definition")
            .drop("entry_meth_definition")
            .drop("ext_fire_definition")
            .drop("devi_cont_definition")
            .drop("devi_ignit_definition")
            .drop("devi_fuel_definition")
            .drop("inv_info1_definition")
            .drop("inv_info2_definition")
            .drop("inv_info3_definition")
            .drop("inv_info4_definition")
            .drop("inv_info5_definition")
            .drop("inv_info6_definition")
            .drop("inv_info7_definition")
            .drop("inv_info8_definition")
            .drop("prop_owner_definition")
            .drop("init_ob1_definition")
            .drop("init_ob2_definition")
            .drop("init_ob3_definition")
            .drop("init_ob4_definition")
            .drop("init_ob5_definition")
            .drop("init_ob6_definition")
            .drop("init_ob7_definition")
            .drop("init_ob8_definition")
            .drop("lab_used1_definition")
            .drop("lab_used2_definition")
            .drop("lab_used3_definition")
            .drop("lab_used4_definition")
            .drop("lab_used5_definition")
            .drop("lab_used6_definition")            

        val combinedArsonsAllYearsDf = combinedArsonsOldFinalDf.union(combinedArsonsNewFinalDf);

        combinedArsonsAllYearsDf.write.option("header", "true").csv(pathToDataFolder + "sparkNfirs/combinedArsonsAllYears")
    }
}
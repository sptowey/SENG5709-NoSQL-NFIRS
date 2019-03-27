# SENG5709-NoSQL-NFIRS
A collection of data analysis tools for the NFIRS data set

The POC we wanted to use Druid, Spark and Kafka.  Thus far we have successfully installed and used Druid and Spark. We had to use a few other tools along the way as we got the system more stable.  For example we used python to fix our date/timestamps.  That is because the data changed halfway through the collection.  This was a small thing to overcome and we expanded to use Spark fix the date/timestamps and to unify them. That wasn't the only change, columns were added and removed.  Again we used Spark to help clean up that data.

The biggest frustration has been getting conistent data into Druid.  Depending on when or who does the ingestion the total number of rows is not constant. This cause a bit of angst as we unable to determine where the data went.  We have been as close as 20,000 record to as much as 5,000,000.  

Druid is not for the faint of heart as the SQL interface, DSQL is very finiky. Then even more strange is the way to run queries using json files. The formatting and execution is very different. First the formatting is strange, not because it is json, but the way the statement needs to be constructed is not overly intuitive. Then using curl to execute the query makes understand where an error occured very difficult. 
Once the data was ingested we started asking our questions: https://github.com/sptowey/SENG5709-NoSQL-NFIRS/blob/master/data/analysis-questions.md
The SQL and the results are captured there. We also started porting all of the SQL queries to a json query, which are kept here: https://github.com/sptowey/SENG5709-NoSQL-NFIRS/tree/master/data/query

The documentation for Druid even adds more angst as the examples are incomplete and often not very helpful. 

# SENG5709-NoSQL-NFIRS
A collection of data analysis tools for the NFIRS data set

For this proof of concept, we intended to use Druid, Spark and Kafka. Thus far we have successfully installed and used Druid and Spark. We had to use a few other tools along the way as we got the system more stable. For example, we initially used python to fix our date/timestamps due to the difference in timestamp formatting between data. This was a small thing to overcome, and we eventually expanded to use Spark fix the date/timestamps and to unify them. Spark was also helpful to clean up the data, including joins between tables and dropping unnecessary columns.

The biggest frustration has been getting conistent data into Druid. Depending on when or who does the ingestion the total number of rows is not consistent. This cause a bit of angst as we unable to determine where the data went. Out of approximately 12,000,000 records, we have been as close as 20,000 records to the source of truth, and as far away as 5,000,000 records.  

Druid is not for the faint of heart as the SQL interface, DSQL, is very finiky. Then, even more strange, is the way to run queries using JSON files. The formatting and execution is very different. First the formatting is strange, not because it is JSON, but because of the way the statement needs to be constructed, which is not overly intuitive. Then using curl to execute the query makes understanding where an error occured very difficult. 
Once the data was ingested we started asking our [questions](https://github.com/sptowey/SENG5709-NoSQL-NFIRS/blob/master/analysis-questions.md)
The SQL and the results are captured there. We also started porting all of the SQL queries to JSON queries, which are kept [here](https://github.com/sptowey/SENG5709-NoSQL-NFIRS/tree/master/data/query).

The documentation for Druid even adds more angst as the examples are incomplete and often not very helpful. That is why we created a [learnings document](https://github.com/sptowey/SENG5709-NoSQL-NFIRS/blob/master/learnings.md) to capture all of the woes that we encountered. 

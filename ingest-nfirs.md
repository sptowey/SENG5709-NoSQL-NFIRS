# 1. Upload data
   From local data source
   ```
   scp NFIRS.zip <user>@<vm-ip>:~
   ```
# 2. Unzip data  
   Install unzip with `sudo apt-get install -y unzip`
   ```
   cd ~
   mkdir NFIRS
   unzip -d NFIRS/ NFIRS.zip
   ```
# 3. Transform data files
   These transformations allow the ingestion specs to work separately, and on the correct files
   - 2009-2011  
     - Pad the dates with leading 0's following using [python scripting](https://github.com/sptowey/SENG5709-NoSQL-NFIRS/blob/master/pad_dates.md)
     - python script renames as `<File>-Pad.csv`  
   - 2012-2014  
     - Rename the files as `<File>-PadNew.csv`  
# 4. Ingest python date-padded data with json ingestion scripts
   - Ingest 2009-2011  
     - `<druid bin>/post-index-task -f ~/SENG5709-NoSQL-NFIRS/process/ingest/ingest_padded_9-11.json`  
   - Ingest 2012-2014  
     - `<druid bin>/post-index-task -f ~/SENG5709-NoSQL-NFIRS/process/ingest/ingest_padded_12-14.json`  

# 5. Ingest Spark-processed data with json ingestion scripts  
   - Create CSV files by following [Spark job instructions](https://github.com/sptowey/SENG5709-NoSQL-NFIRS/blob/master/submit-spark-job.md)
   - Ingest 3 different data sets  
     - `<druid bin>/post-index-task -f ~/SENG5709-NoSQL-NFIRS/process/ingest/ingest_spark_joined_headers_included.json` for NFIRS General Incident Information  
     - `<druid bin>/post-index-task -f ~/SENG5709-NoSQL-NFIRS/process/ingest/ingest_arson.json` for NFIRS Arson  
     - `<druid bin>/post-index-task -f ~/SENG5709-NoSQL-NFIRS/process/ingest/ingest_ems.json` for NFIRS EMS  
     
# 6. Track ingestion jobs
   - See the running tasks at http://40.122.132.135:8090/console.html  
   - Watch the indexing service at http://40.122.132.135:8081/#/indexing-service  
   - See the imported datasources at http://40.122.132.135:8081/#/datasources

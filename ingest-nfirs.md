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
# 4. Ingest data with json ingestion script
   - Ingest 2009-2011  
     - `<druid bin>/post-index-task -f ~/ingest_padded_9-11.json`  
   - Ingest 2012-2014  
     - `<druid bin>/post-index-task -f ~/ingest_padded_12-14.json`  

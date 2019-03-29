# 3.1.3 Analytical Questions
The queries should be run from the `~/SENG5709-NoSQL-NFIRS/data/query/ directory.` The queries can be run on the host machine using the shown `localhost` or can be run against the public IP `40.122.132.135`.  
This document may be messy or incomplete because it is a living document showing the thought process behind the queries. Additionally, many of the JSON query results are not shown, because they mirror the DSQL results.
## 3.1.3.1 Easy Questions
- What are the min, max, and average Fire Service Deaths  
   - `select min(ff_death), max(ff_death), avg(ff_death) from "NFIRS_General_Incident_Information";`
   - `-1, 5, 0`  
   - `select min(ff_death), max(ff_death), avg(cast(ff_death as double)) from "NFIRS_General_Incident_Information";`
   - `??? query error` 
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_ff_death.json http://localhost:8082/druid/v2?pretty`
- How many different incident types are there  
   - Aggregate over indident types (`inc_type`)  
   - `select count(distinct inc_type) from "NFIRS_General_Incident_Information";`  
   - `74`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_count_incident_types.json http://localhost:8082/druid/v2?pretty`
- Were fire incidents reported in all 50 states each year?  
   - Count distinct states (`state`)
   - `select count(distinct state) from "NFIRS_General_Incident_Information";`  
   - `55`
   - `select distinct state from "NFIRS_General_Incident_Information";`  
   - 54 rows... Also includes `DC`, `NA` (N/A?), `PR`, `null`. Doesn't really add up
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_all50_2009.json http://localhost:8082/druid/v2?pretty` 54  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_all50_2010.json http://localhost:8082/druid/v2?pretty` 53  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_all50_2011.json http://localhost:8082/druid/v2?pretty` 53  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_all50_2012.json http://localhost:8082/druid/v2?pretty` 53  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_all50_2013.json http://localhost:8082/druid/v2?pretty` 52  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_all50_2014.json http://localhost:8082/druid/v2?pretty` 53  
- What is the min, max and average time between the time the alarm was sounded and the fire was controlled.  
   - Aggregate over difference between columns (`alarm`, maybe `alarm_unparsed`, `inc_cont`)  
   - `select` 
`min(TIMESTAMP_TO_MILLIS(TIME_PARSE(inc_cont))-TIMESTAMP_TO_MILLIS(TIME_PARSE(alarm)))/(1000*60),`  
`max(TIMESTAMP_TO_MILLIS(TIME_PARSE(inc_cont))-TIMESTAMP_TO_MILLIS(TIME_PARSE(alarm)))/(1000*60),`  
`avg(TIMESTAMP_TO_MILLIS(TIME_PARSE(inc_cont))-TIMESTAMP_TO_MILLIS(TIME_PARSE(alarm)))/(1000*60)`  
`from "NFIRS_General_Incident_Information_Spark" where alarm is not null and inc_cont is not null`  
`and TIME_PARSE(inc_cont) < TIME_PARSE('2015-01-01T00:00:00') and TIME_PARSE(inc_cont) >= TIME_PARSE('2009-01-01T00:00:00')`  
`and TIMESTAMP_TO_MILLIS(TIME_PARSE(inc_cont))-TIMESTAMP_TO_MILLIS(TIME_PARSE(alarm)) > 0`  
`limit 10;`  
   - This query was hard because the data was so messy. The query required many filters to make sense. Also converted from milliseconds to minutes  
   - `curl -X 'POST' -H 'Content-Type:application/jso-d @query_time_to_control.json http://localhost:8082/druid/v2?pretty`

|min|max|avg|
|---|---|---|
|1|527128|59|
  

## 3.1.3.2 Moderately Difficult Questions
- How many fires were there in each state per year?  
   - Group by State, count (`state`)  
   - `select state, TIME_EXTRACT(__time, 'YEAR'), count(*) from "NFIRS_General_Incident_Information" group by state, TIME_EXTRACT(__time, 'YEAR') order by state, TIME_EXTRACT(__time, 'YEAR');`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_fires_per_state_per_year_old.json http://localhost:8082/druid/v2?pretty` - TODO - years are formatted as longs  

|state|year|count|
|-----|----|-----|
|  |2009|359|
|  |2010|445|
|  |2011|460|
|AK|2009|5429|
|AK|2010|4943|
|AK|2011|5358|
|AK|2012|5606|
|AK|2013|5722|
|AK|2014|5459|
|AL|2009|19514|
|AL|2010|25062|
|AL|2011|25618|
|AL|2012|16938|
|AL|2013|16458|
|AL|2014|22296|
|AR|2009|28026|
|AR|2010|33415|
|AR|2011|32683|
|AR|2012|34555|
|AR|2013|26906|
|AR|2014|32431|
|AZ|2009|22377|
|AZ|2010|21451|
|AZ|2011|22580|
|AZ|2012|17671|
|AZ|2013|23271|
|AZ|2014|23497|
|CA|2009|110883|
|CA|2010|115400|
|CA|2011|117781|
|CA|2012|81074|
|CA|2013|123785|
|CA|2014|121761|
|CO|2009|33369|
|CO|2010|35057|
|CO|2011|34896|
|CO|2012|30864|
|CO|2013|30006|
|CO|2014|29124|
|CT|2009|36006|
|CT|2010|41385|
|CT|2011|45610|
|CT|2012|35256|
|CT|2013|34731|
|CT|2014|31333|
|DC|2009|293|
|DC|2010|1574|
|DC|2011|3814|
|DC|2012|3736|
|DC|2013|2448|
|DC|2014|2738|
|DE|2009|7724|
|DE|2010|8357|
|DE|2011|8051|
|DE|2012|7495|
|DE|2013|6765|
|DE|2014|7759|
|FL|2009|126328|
|FL|2010|127772|
|FL|2011|129234|
|FL|2012|120584|
|FL|2013|110859|
|FL|2014|111866|
|GA|2009|63132|
|GA|2010|76744|
|GA|2011|86100|
|GA|2012|77391|
|GA|2013|72995|
|GA|2014|78152|
|HI|2009|6277|
|HI|2010|6591|
|HI|2011|6206|
|HI|2012|6081|
|HI|2013|5826|
|HI|2014|5272|
|IA|2009|14530|
|IA|2010|13515|
|IA|2011|17125|
|IA|2012|21544|
|IA|2013|18472|
|IA|2014|20399|
|ID|2009|12572|
|ID|2010|12540|
|ID|2011|12258|
|ID|2012|13280|
|ID|2013|12516|
|ID|2014|12336|
|IL|2009|108613|
|IL|2010|116923|
|IL|2011|116289|
|IL|2012|111879|
|IL|2013|104419|
|IL|2014|110421|
|IN|2009|52407|
|IN|2010|55291|
|IN|2011|45947|
|IN|2012|47215|
|IN|2013|33482|
|IN|2014|8945|
|KS|2009|25445|
|KS|2010|24210|
|KS|2011|32013|
|KS|2012|30713|
|KS|2013|25630|
|KS|2014|33136|
|KY|2009|33427|
|KY|2010|34688|
|KY|2011|31966|
|KY|2012|32561|
|KY|2013|30504|
|KY|2014|34257|
|LA|2009|28286|
|LA|2010|36076|
|LA|2011|45408|
|LA|2012|34733|
|LA|2013|34020|
|LA|2014|37378|
|MA|2009|76376|
|MA|2010|83489|
|MA|2011|91726|
|MA|2012|80215|
|MA|2013|77538|
|MA|2014|71777|
|MD|2009|42843|
|MD|2010|48892|
|MD|2011|50489|
|MD|2012|44081|
|MD|2013|41162|
|MD|2014|42501|
|ME|2009|12076|
|ME|2010|11246|
|ME|2011|11566|
|ME|2012|11698|
|ME|2013|11703|
|ME|2014|13430|
|MI|2009|67373|
|MI|2010|70415|
|MI|2011|74299|
|MI|2012|69887|
|MI|2013|68245|
|MI|2014|69220|
|MN|2009|31329|
|MN|2010|33084|
|MN|2011|32243|
|MN|2012|34209|
|MN|2013|30817|
|MN|2014|29380|
|MO|2009|46735|
|MO|2010|48970|
|MO|2011|46400|
|MO|2012|47449|
|MO|2013|39430|
|MO|2014|50239|
|MS|2009|29296|
|MS|2010|37209|
|MS|2011|35800|
|MS|2012|28231|
|MS|2013|22980|
|MS|2014|26318|
|MT|2009|6177|
|MT|2010|5940|
|MT|2011|5700|
|MT|2012|7795|
|MT|2013|6762|
|MT|2014|6565|
|NA|2012|386|
|NA|2013|325|
|NA|2014|329|
|NC|2009|75127|
|NC|2010|91090|
|NC|2011|94200|
|NC|2012|80347|
|NC|2013|85476|
|NC|2014|93787|
|ND|2009|3104|
|ND|2010|3163|
|ND|2011|3461|
|ND|2012|4739|
|ND|2013|3369|
|ND|2014|3358|
|NE|2009|7768|
|NE|2010|7536|
|NE|2011|7732|
|NE|2012|9658|
|NE|2013|7581|
|NE|2014|8462|
|NH|2009|15216|
|NH|2010|18448|
|NH|2011|18312|
|NH|2012|16765|
|NH|2013|15560|
|NH|2014|17165|
|NJ|2009|78692|
|NJ|2010|92190|
|NJ|2011|92616|
|NJ|2012|89764|
|NJ|2013|68373|
|NJ|2014|80834|
|NM|2009|14151|
|NM|2010|14739|
|NM|2011|18047|
|NM|2012|15711|
|NM|2013|15473|
|NM|2014|15710|
|NV|2009|12883|
|NV|2010|13062|
|NV|2011|14149|
|NV|2012|12471|
|NV|2013|12518|
|NV|2014|13381|
|NY|2009|150852|
|NY|2010|153217|
|NY|2011|147064|
|NY|2012|165082|
|NY|2013|155517|
|NY|2014|169906|
|OH|2009|114835|
|OH|2010|117291|
|OH|2011|111067|
|OH|2012|114623|
|OH|2013|98045|
|OH|2014|105868|
|OK|2009|35620|
|OK|2010|31345|
|OK|2011|45159|
|OK|2012|35499|
|OK|2013|31604|
|OK|2014|33611|
|OR|2009|22894|
|OR|2010|17945|
|OR|2011|21141|
|OR|2012|22790|
|OR|2013|22555|
|OR|2014|23050|
|PA|2009|47757|
|PA|2010|67678|
|PA|2011|69196|
|PA|2012|58117|
|PA|2013|46816|
|PA|2014|65694|
|PR|2009|636|
|RI|2009|4169|
|RI|2010|6011|
|RI|2011|6809|
|RI|2012|6387|
|RI|2013|4942|
|RI|2014|5715|
|SC|2009|26423|
|SC|2010|42821|
|SC|2011|38368|
|SC|2012|31996|
|SC|2013|37862|
|SC|2014|44635|
|SD|2009|4410|
|SD|2010|4913|
|SD|2011|4565|
|SD|2012|5813|
|SD|2013|4255|
|SD|2014|4075|
|TN|2009|47192|
|TN|2010|52719|
|TN|2011|50573|
|TN|2012|44984|
|TN|2013|39732|
|TN|2014|46161|
|TX|2009|171739|
|TX|2010|167142|
|TX|2011|211668|
|TX|2012|166736|
|TX|2013|167415|
|TX|2014|166348|
|UT|2009|10045|
|UT|2010|8513|
|UT|2011|10945|
|UT|2012|11190|
|UT|2013|10501|
|UT|2014|12183|
|VA|2009|67963|
|VA|2010|70888|
|VA|2011|71680|
|VA|2012|66658|
|VA|2013|63557|
|VA|2014|61128|
|VT|2009|7470|
|VT|2010|7246|
|VT|2011|7965|
|VT|2012|7230|
|VT|2013|7234|
|VT|2014|7083|
|WA|2009|49665|
|WA|2010|43259|
|WA|2011|39185|
|WA|2012|38938|
|WA|2013|33493|
|WA|2014|38145|
|WI|2009|22485|
|WI|2010|25463|
|WI|2011|27364|
|WI|2012|29124|
|WI|2013|29042|
|WI|2014|32756|
|WV|2009|26911|
|WV|2010|26658|
|WV|2011|24800|
|WV|2012|25498|
|WV|2013|22382|
|WV|2014|22894|
|WY|2009|4362|
|WY|2010|4260|
|WY|2011|4260|
|WY|2012|3914|
|WY|2014|2649|

- Which fire department responded to the most incidents? Least?  
   - Group by fire department id, get min and max count (`fdid`)  - **need to rerun after re-import fdid as string**  
   - Get fire department name from Fire Departments table (different file)  - **redo and rerun after denormalizing**
   - `select fdid, count(fdid) from "NFIRS_General_Incident_Information" group by fdid order by count(fdid) limit 1;`  
   - `select fdid, count(fdid) from "NFIRS_General_Incident_Information" where fdid <> 0 group by fdid order by count(fdid) desc limit 1;`  
   - Turns out `fdid` is not unique. Might be unique per state
   - `select state, fdid, count(fdid) from "NFIRS_General_Incident_Information" group by (state, fdid) order by count(*) limit 1;`  
   - `OR, 00031, 1` - many more with 1
   - `select state, fdid, count(*) from "NFIRS_General_Incident_Information" where fdid <> 0 group by (state, fdid) order by count(*) desc limit 1;`  
   - `NY, 24001, 424430`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_busiest_dept.json http://localhost:8082/druid/v2?pretty`
- Which state(s) had the most Fire Service Deaths each year?  
   - Group by state, count fire service deaths (`state`,`ff_death`)  
   - `select state, TIME_EXTRACT(__time, 'YEAR'), sum(ff_death) from "NFIRS_General_Incident_Information_Spark" group by state, TIME_EXTRACT(__time, 'YEAR') order by sum(ff_death) desc limit 6;` **not quite right, gives top 6 deadliest, not 1 for each year - solve with timespans**  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_deadliest_state_per_year_2009.json http://localhost:8082/druid/v2?pretty`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_deadliest_state_per_year_2010.json http://localhost:8082/druid/v2?pretty`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_deadliest_state_per_year_2011.json http://localhost:8082/druid/v2?pretty`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_deadliest_state_per_year_2012.json http://localhost:8082/druid/v2?pretty`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_deadliest_state_per_year_2013.json http://localhost:8082/druid/v2?pretty`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_deadliest_state_per_year_2014.json http://localhost:8082/druid/v2?pretty`  
- Which incident type is most common in each state? Least common?  
   - Group by state and incident type, get min and max count (`state`,`inc_type`)  
   - `select state, inc_type, count(*) from "NFIRS_General_Incident_Information" group by state, inc_type order by count(*) desc limit 5;`  **similar problem as last query, but can't solve with timespans**  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_most_common_incident_per_state.json http://localhost:8082/druid/v2?pretty`
- How many civilians were killed in fires each year? Total over all years?  
   - Aggregate civilian deaths by year, and count (`oth_death`)  
   - `select floor(__time to year), sum(oth_death) from "NFIRS_General_Incident_Information_Spark" group by floor(__time to year) order by floor(__time to year);`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_count_civilian_deaths_per_year.json http://localhost:8082/druid/v2?pretty` 
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_total_civilian_deaths.json http://localhost:8082/druid/v2?pretty`   
   
|year|count|
|----|-----|
|2009|1929|
|2010|2063|
|2011|2082|
|2012|1963|
|2013|1943|
|2014|2150|

   - Count total  
   - `select sum(oth_death) from "NFIRS_General_Incident_Information";`  
   - `12130`  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_total_civilian_deaths.json http://localhost:8082/druid/v2?pretty`   
   
- How many arson cases are still open for each fire department?  
   - 1 Investigation Open  
   - 2 Investigation Closed  
   - 3 Investigation Inactive  
   - 4 Investigation closed with arrest  
   - 5 Closed with exceptional clearance  
   - Check for arson factor and closed status and count (Arson file - `fdid`,`case_stat`; Fire Depts `fd_name`, `state`)
   - `select state, fdid, fd_name count(*) from "NFIRS_General_Incident_Information_Spark" where case_stat = 1 group by state, fdid, fd_name limit 10;  
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_open_arson_by_fdid_and_state.json http://localhost:8082/druid/v2?pretty`  
   - `select state, fdid, count(*) from "NFIRS_Arson" where case_stat = 1 group by state, fdid order by count(*) desc limit 10;`
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_open_arson_by_fdid_and_state_arson_only.json http://localhost:8082/druid/v2?pretty`
- Which are the top 5 fire departments that didn’t report property loss (had the most null values in that column)?  
   - Group by fire departments, count nulls (`fdid`,`prop_loss`)
   - `select fdid from "NFIRS_General_Incident_Information_Spark" where prop_loss is null;` returns 0 rows - probably because that's how column family databases work  

## 3.1.3.3 Challenging Questions
- What is average property damage, fire service deaths, and civilian deaths per minute between when the fire alarm was sounded and when the fire was contained for each state?  
   - Group by state (`state`)
   - Get number of minutes between sounded and contained (`alarm`, maybe `alarm_unparsed`, `inc_cont`)  
   - Get sums of relevent columns and divide by sum of minutes (`prop_loss`,`ff_death`,`oth_death`,`alarm`, maybe `alarm_unparsed`, `inc_cont`)  
   - `select state, [post aggregations?]  from "NFIRS_General_Incident_Information_Spark" group by state;`  
   - `select`  
`state,`  
`sum(prop_loss)/(sum(TIMESTAMP_TO_MILLIS(TIME_PARSE(inc_cont))-TIMESTAMP_TO_MILLIS(TIME_PARSE(alarm)))/(1000*60)),`  
`sum(ff_death)/(sum(TIMESTAMP_TO_MILLIS(TIME_PARSE(inc_cont))-TIMESTAMP_TO_MILLIS(TIME_PARSE(alarm)))/(1000*60)),`  
`sum(oth_death)/(sum(TIMESTAMP_TO_MILLIS(TIME_PARSE(inc_cont))-TIMESTAMP_TO_MILLIS(TIME_PARSE(alarm)))/(1000*60))`  
`from "NFIRS_General_Incident_Information_Spark"`  
`where alarm is not null and inc_cont is not null`  
`and TIME_PARSE(inc_cont) < TIME_PARSE('2015-01-01T00:00:00')`  
`and TIME_PARSE(inc_cont) >= TIME_PARSE('2009-01-01T00:00:00')`  
`and TIMESTAMP_TO_MILLIS(TIME_PARSE(inc_cont))-TIMESTAMP_TO_MILLIS(TIME_PARSE(alarm)) > 0`  
`and prop_loss is not null and prop_loss > 0`  
`group by state;`  
   - Deaths are too low to show on a per minute scale (and that's a good thing!)
   - `curl -X 'POST' -H 'Content-Type:application/json' -d @query_stats_per_minute_by_state.json http://localhost:8082/druid/v2?pretty`
   
- What is the most common Emergency Medical Services treatment for each type of incident?  
   - Group by EMS treatment (additional file) and incident type. Get max count (EMS file; `inc_type` by join to general by `inc_no`; `proc_use1`,`proc_use2`,`proc_use3`...`proc_use25` - these are like checkboxes, either filled with their number or nothing)  
   - `select * from "NFIRS_EMS" group by inc_type;  
   - TBD might not be possible

## 3.1.3.4 Challenging Questions with Cross-Referenced Data
These questions are unable to be answered with the dataset available. However, they could be answered by analyzing results through the lens of our intuition, or additional datasets could be located to find the supporting data required. These advanced scenarios will be addressed only if time allows.  
- Do the fires happen more readily in arid areas versus more precipitous areas?  
- Were there increases in the number of fires where a drought occurred?  
- Did the number of fires and the location, especially affluent areas, have the highest property damage recorded?  

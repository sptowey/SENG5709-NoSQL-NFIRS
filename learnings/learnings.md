# 1. Learnings
## Attemps
Tried to use metrics on acres_burn as longSum, but didn't create a metric as a sum.  Essentially a dimension was created.

## Date Formatting
Dates are either in the format Mddyyyy or MMddyyyy, interspersed. The inconsistency doesn't allow joda datetime parsing to work correctly. Need to find a way to work with both versions.

## Transformations
Able to get some transformatino expressions working, but couldn't use for the timestamp column. May be able to come up with some other useful use cases (converting DateTimes which aren't used for the timestamp?)

## Querying 
This is a pain in the butt!! First to do SQL everything must be exact and if you mess up you have to retype everything! No command memory.  Next the examples on using curl to execute the JSON query is even more frustrating.  You don't necessarily get an error back or if you do it won't lead you to the answer.  Finally found some help, http://druid.io/blog/2013/11/04/querying-your-data.html gives the hints on what port is need to run the query on. 

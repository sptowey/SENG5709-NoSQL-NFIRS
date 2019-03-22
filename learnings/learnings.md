# 1. Learnings
## Attemps
- tried to use metrics on acres_burn as longSum, but didn't create a metric as a sum.  Essentially a dimension was created.

## Date Formatting
Dates are either in the format Mddyyyy or MMddyyyy, interspersed. The inconsistency doesn't allow joda datetime parsing to work correctly. Need to find a way to work with both versions.

## Transformations
Able to get some transformatino expressions working, but couldn't use for the timestamp column. May be able to come up with some other useful use cases (converting DateTimes which aren't used for the timestamp?)

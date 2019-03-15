# 3.1.3 Analytical Questions
## 3.1.3.1 Easy Questions
- What are the min, max, and average Fire Service Deaths  
   - Query Fire Service Deaths columnn  
- How many different incident types are there  
   - Aggregate over indident types  
- Were fire incidents reported in all 50 states each year?  
   - Count distinct states
- What is the min, max and average time between the time the alarm was sounded and the fire was controlled.  
   - Aggregate over difference between columns

## 3.1.3.2 Moderately Difficult Questions
- How many fires were there in each state per year?  
   - Group by State, count
- Which fire department responded to the most incidents? Least?  
   - Group by fire department id, get min and max count
   - Get fire department name from Fire Departments table
- Which state(s) had the most Fire Service Deaths each year?  
   - Group by state, count fire service deaths
- Which incident type is most common in each state? Least common?  
   - Group by state and incident type, get min and max count
- How many civilians were killed in fires each year? Total over all years?  
   - Aggregate civilian deaths by year, and count
   - Count total
- How many arson cases are still open for each fire department?  
   - Check for arson factor and closed status and count
- Which are the top 5 fire departments that didn’t report property loss (had the most null values in that column)?  
   - Group by fire departments, count nulls

## 3.1.3.3 Challenging Questions
- What is average property damage, fire service deaths, and civilian deaths per minute between when the fire alarm was sounded and when the fire was contained for each state?  
   - Group by state
   - Get number of minutes between sounded and contained  
   - Get sums of relevent columns and divide by sum of minutes
- What is the most common Emergency Medical Services treatment for each type of incident?  
   - Group by EMS treatment (additional file) and incident type. Get max count

## 3.1.3.4 Challenging Questions with Cross-Referenced Data
These questions are unable to be answered with the dataset available. However, they could be answered by analyzing results through the lens of our intuition, or additional datasets could be located to find the supporting data required. These advanced scenarios will be addressed only if time allows.  
- Do the fires happen more readily in arid areas versus more precipitous areas?  
- Were there increases in the number of fires where a drought occurred?  
- Did the number of fires and the location, especially affluent areas, have the highest property damage recorded?  

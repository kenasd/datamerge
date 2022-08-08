# Data sorting and filtering

Read the 3 input files reports.json, reports.csv, reports.xml and output a combined CSV file with the following characteristics:

- The same column order and formatting as reports.csv
- All report records with packets-serviced equal to zero should be excluded
- records should be sorted by request-time in ascending order

Additionally, the application should print a summary showing the number of records in the output file associated with each service-guid.

Please provide source, documentation on how to run the program and an explanation on why you chose the tools/libraries used.

## Submission

You may fork this repo, commit your work and let us know of your project's location, or you may email us your project files in a zip file.

## Run project
1. Go to project folder
or:
2. Run: mvn compile exec:java -Dexec.mainClass="com.olek.datamerge.DataMerge" -Dexec.arguments="full-reports.csv,reports.csv,reports.json,reports.xml"
or:
3. Run: mvn clean compile assembly:single
4. Run: java -jar target/datamerge-1.0-SNAPSHOT-jar-with-dependencies.jar full-reports.csv reports.csv reports.json reports.xml




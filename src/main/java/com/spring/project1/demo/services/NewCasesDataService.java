package com.spring.project1.demo.services;

import com.spring.project1.demo.data.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewCasesDataService {

    private static final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }


/*
https://docs.spring.io/spring-framework/docs/3.0.x/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html

second, minute, hour, day of month, month, day(s) of week

second (0 - 59)
minute (0 - 59)
hour (0 - 23)
day of month (1 - 31)
month (1 - 12)
day of week (0 - 6) (Sunday=0 or 7)

       "0 0 * * * *" = the top of every hour of every day.
       "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
       "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
       "0 0 0 25 12 ?" = every Christmas Day at midnight
*/

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());


        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {

/*          String state = record.get("Province/State");
            System.out.println(state);
            String reg = record.get("Country/Region");
            String lat = record.get("Lat");
            String lon = record.get("Long");

            Province/State
            Country/Region
            Lat
            Long
            These are headers.
            */


            LocationStats locationStats = new LocationStats();
            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));

            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));

            locationStats.setLatestTotalCases(latestCases);
            locationStats.setDiffFromPrevDay(latestCases - prevDayCases);

            newStats.add(locationStats);

        }
        this.allStats = newStats;
    }
}

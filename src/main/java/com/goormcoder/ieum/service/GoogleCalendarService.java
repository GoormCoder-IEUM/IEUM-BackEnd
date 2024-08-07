package com.goormcoder.ieum.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.goormcoder.ieum.domain.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "IEUM";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final File CREDENTIALS_FOLDER = new File("credentials");
    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCalendarService.class);

    private static HttpTransport httpTransport;

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            LOGGER.error("Failed to initialize HTTP transport", e);
        }
    }

    private static Credential getCredentials() throws IOException, GeneralSecurityException {
        File tokensDir = new File(TOKENS_DIRECTORY_PATH);
        if (!tokensDir.exists()) {
            if (tokensDir.mkdirs()) {
                LOGGER.info("Created tokens directory.");
            } else {
                LOGGER.error("Failed to create tokens directory.");
            }
        }

        try (FileInputStream in = new FileInputStream(new File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME))) {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(CalendarScopes.CALENDAR))
                    .setDataStoreFactory(new FileDataStoreFactory(tokensDir))
                    .setAccessType("offline")
                    .build();

            //return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
            return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().setPort(52143).build()).authorize("user");
        } catch (IOException e) {
            LOGGER.error("Failed to get credentials", e);
            throw e;
        }
    }


    public static Calendar getCalendarService() throws IOException, GeneralSecurityException {
        Credential credential = getCredentials();
        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void createGoogleCalendarEvent(Plan plan) throws GeneralSecurityException, IOException {
        Calendar calendarService = new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary("Trip to " + plan.getDestination().getDestinationName())
                .setLocation(String.valueOf(plan.getDestination().getDestinationName()))
                .setDescription("Travel plan from " + plan.getStartedAt() + " to " + plan.getEndedAt());

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(plan.getStartedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
                .setTimeZone("Asia/Seoul");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(plan.getEndedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
                .setTimeZone("Asia/Seoul");
        event.setEnd(end);

//        List<EventAttendee> attendees = plan.getPlanMembers().stream()
//                .map(planMember -> new EventAttendee().setEmail(planMember.getMember().getEmail()))
//                .collect(Collectors.toList());
//        event.setAttendees(attendees);

        String calendarId = "primary";
        try {
            event = calendarService.events().insert(calendarId, event).execute();
            LOGGER.info("Event created: {}", event.getHtmlLink());
        } catch (GoogleJsonResponseException e) {
            LOGGER.error("Request failed: {} {}", e.getStatusCode(), e.getStatusMessage());
            if (e.getDetails() != null) {
                LOGGER.error("Error details: {}", e.getDetails());
            }
        }
    }


    private static HttpRequestInitializer setHttpTimeout() {
        return httpRequest -> {
            httpRequest.setConnectTimeout(3 * 60000); // 3 minutes connect timeout
            httpRequest.setReadTimeout(3 * 60000); // 3 minutes read timeout
        };
    }
}
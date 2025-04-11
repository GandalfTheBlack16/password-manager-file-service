package com.gandalftheblack.pm.fileservice.client;

import static com.gandalftheblack.pm.fileservice.client.constants.GoogleDriveConstants.APPLICATION_NAME;
import static com.gandalftheblack.pm.fileservice.client.constants.GoogleDriveConstants.AUTH_URI;
import static com.gandalftheblack.pm.fileservice.client.constants.GoogleDriveConstants.CERT_URL;
import static com.gandalftheblack.pm.fileservice.client.constants.GoogleDriveConstants.CREDENTIALS_TYPE;
import static com.gandalftheblack.pm.fileservice.client.constants.GoogleDriveConstants.SCOPES;
import static com.gandalftheblack.pm.fileservice.client.constants.GoogleDriveConstants.TOKEN_URI;
import static com.gandalftheblack.pm.fileservice.client.constants.GoogleDriveConstants.UNIVERSE_DOMAIN;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleDriveClient {
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private final Drive driveInstance;

  public GoogleDriveClient(
      @Value("${google.drive.credentials.private_key_id}") String privateKeyId,
      @Value("${google.drive.credentials.private_key}") String privateKey,
      @Value("${google.drive.credentials.client_email}") String clientEmail,
      @Value("${google.drive.credentials.client_id}") String clientId,
      @Value("${google.drive.credentials.cert_url}") String clientX509CertUrl)
      throws IOException, GeneralSecurityException {
    GoogleCredential credential =
        GoogleCredential.fromStream(
                new BufferedInputStream(
                    buildCredentialsFile(
                        privateKeyId, privateKey, clientEmail, clientId, clientX509CertUrl)))
            .createScoped(SCOPES);
    driveInstance =
        new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
            .build();
  }

  public Drive getInstance() {
    return driveInstance;
  }

  private InputStream buildCredentialsFile(
      String privateKeyId,
      String privateKey,
      String clientEmail,
      String clientId,
      String clientX509CertUrl) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("type", new JsonPrimitive(CREDENTIALS_TYPE));
    jsonObject.add("project_id", new JsonPrimitive(APPLICATION_NAME));
    jsonObject.add("private_key_id", new JsonPrimitive(privateKeyId));
    jsonObject.add("private_key", new JsonPrimitive(privateKey.replaceAll("\\\\n", "\n")));
    jsonObject.add("client_email", new JsonPrimitive(clientEmail));
    jsonObject.add("client_id", new JsonPrimitive(clientId));
    jsonObject.add("auth_uri", new JsonPrimitive(AUTH_URI));
    jsonObject.add("token_uri", new JsonPrimitive(TOKEN_URI));
    jsonObject.add("auth_provider_x509_cert_url", new JsonPrimitive(CERT_URL));
    jsonObject.add("client_x509_cert_url", new JsonPrimitive(clientX509CertUrl));
    jsonObject.add("universe_domain", new JsonPrimitive(UNIVERSE_DOMAIN));
    return new ByteArrayInputStream(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
  }
}

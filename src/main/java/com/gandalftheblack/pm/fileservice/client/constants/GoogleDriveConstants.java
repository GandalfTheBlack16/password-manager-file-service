package com.gandalftheblack.pm.fileservice.client.constants;

import com.google.api.services.drive.DriveScopes;
import java.util.Collections;
import java.util.List;

public class GoogleDriveConstants {
  private GoogleDriveConstants() {
    throw new IllegalStateException("Utility class");
  }

  public static final String CREDENTIALS_TYPE = "service_account";
  public static final String APPLICATION_NAME = "password-manager-backup";
  public static final String AUTH_URI = "https://accounts.google.com/o/oauth2/auth";
  public static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
  public static final String CERT_URL = "https://www.googleapis.com/oauth2/v1/certs";
  public static final String UNIVERSE_DOMAIN = "googleapis.com";
  public static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
}

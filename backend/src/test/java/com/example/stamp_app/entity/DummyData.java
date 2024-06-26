package com.example.stamp_app.entity;

public class DummyData {
    public static final String REGISTER_POST_PATH = "/ems/account/register";
    public static final String LOGIN_POST_PATH = "/ems/account/login";
    public static final String MEASURED_DATA_POST_PATH = "/ems/measured-data";
    public static final String MEASURED_DATA_GET_PATH = "/ems/measured-data";
    public static final String MICROCONTROLLER_PATCH_PATH = "/ems/micro-controller/detail";
    public static final String MICROCONTROLLER_POST_PATH = "/ems/micro-controller";
    public static final String MICROCONTROLLER_GET_PATH = "/ems/micro-controller/detail";

    public static final String MICROCONTROLLER_NO_SESSION_GET_PATH = "/ems/micro-controller/detail/no-session";

    public static final String INVALID_7_LENGTH_PASSWORD = "A123456";

    public static final String VALID_8_LENGTH_PASSWORD = "A1234567";

    public static final String INVALID_8_LENGTH_PASSWORD = "a1234567";

    public static final String VALID_9_LENGTH_PASSWORD = "A12345678";

    public static final String VALID_23_LENGTH_PASSWORD = "A1234567890123456789012";

    public static final String VALID_24_LENGTH_PASSWORD = "A12345678901234567890123";

    public static final String INVALID_24_LENGTH_PASSWORD = "a12345678901234567890123";

    public static final String INVALID_25_LENGTH_PASSWORD = "A123456789012345678901234";

    public static final String VALID_EMAIL_ADDRESS = "aaa@example.com";

    public static final String INVALID_EMAIL_ADDRESS = "aaaexample.com";
    public static final String INVALID_UUID = "a";
    public static final String VALID_UUID = "4e540b38-31f8-40b4-9602-44dea41e5c5a";
    public static final String INVALID_INTERVAL = "100";
    public static final String INVALID_SDI_ADDRESS = "11";
    public static final String INVALID_MAC_ADDRESS = "a";
    public static final String VALID_MAC_ADDRESS = "AA:AA:AA:AA:AA:AA";
}

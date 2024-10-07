package org.example;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MainTask1 {

    public static final String URL = "http://localhost:8801/api/v1/robot-python/";
    public static final String TOKEN = "dcf21b4c-def9-4c91-917e-6eee22fc95e42acf6b8a-dfda-4664-bf8b-6622f0b70238";
    public static final String FORWARD = "forward";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String BACK = "backward";
    public static final String DATA = "sensor-data";
    public static final String FRONT_DISTANCE = "front_distance";
    public static final String LEFT_DISTANCE = "left_side_distance";
    public static final String RIGHT_DISTANCE = "right_side_distance";
    public static final int N = 1; // North |^
    public static final int S = 3; //South |
    public static final int W = 4; //West <-
    public static final int E = 2; //East ->

    private static final int[][] result = new int[16][16];
    private static int x = 0;
    private static int y = 15;
    private static int orientation;

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        orientation = N;
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(URL + "restart" + "?token=" + TOKEN))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        for (int i = 0; i < 1000; i++) {
            String jsonData = getData();
            JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            JSONObject json = (JSONObject) parser.parse(jsonData);
            check(json);
            rotation(json);
            httpClient = HttpClient.newHttpClient();
            request = HttpRequest.newBuilder(URI.create(URL + FORWARD + "?token=" + TOKEN))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            switch (orientation) {
                case N -> y--;
                case E -> x++;
                case S -> y++;
                case W -> x--;
            }
        }
        System.out.println("end");
    }

    public static String getData() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(URL + DATA + "?token=" + TOKEN))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static void rotation(JSONObject json) throws IOException, InterruptedException {
        int frontDistance = json.getAsNumber(FRONT_DISTANCE).intValue();
        int leftDistance = json.getAsNumber(LEFT_DISTANCE).intValue();
        int rightDistance = json.getAsNumber(RIGHT_DISTANCE).intValue();
        if (frontDistance > 5) {
            return;
        }
        if (rightDistance > 5) {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(URL + RIGHT + "?token=" + TOKEN))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            orientation = orientation == 4 ? N : ++orientation;
        } else if (leftDistance > 5) {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(URL + LEFT + "?token=" + TOKEN))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            orientation = orientation == 1 ? W : --orientation;
        } else {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(URL + LEFT + "?token=" + TOKEN))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            orientation = orientation == 1 ? W : --orientation;
            httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            orientation = orientation == 1 ? W : --orientation;
        }
    }

    private static void check(JSONObject json) throws ParseException {
        int frontDistance = json.getAsNumber(FRONT_DISTANCE).intValue();
        int leftDistance = json.getAsNumber(LEFT_DISTANCE).intValue();
        int rightDistance = json.getAsNumber(RIGHT_DISTANCE).intValue();
        int backDistance = json.getAsNumber("back_distance").intValue();
        switch (orientation){
            case N -> {
                if (backDistance == 5 && rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 14;
                } else if (frontDistance == 5 && backDistance == 5 && leftDistance == 5) {
                    result[y][x] = 13;
                } else if (frontDistance == 5 && rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 12;
                } else if (frontDistance == 5 && rightDistance == 5) {
                    result[y][x] = 7;
                } else if (frontDistance == 5 && leftDistance == 5) {
                    result[y][x] = 8;
                } else if (rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 9;
                } else if (rightDistance == 5) {
                    result[y][x] = 3;
                } else if (leftDistance == 5) {
                    result[y][x] = 1;
                } else if (frontDistance == 5) {
                    result[y][x] = 2;
                }
            }
            case S -> {
                if (backDistance == 5 && rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 12;
                } else if (frontDistance == 5 && backDistance == 5 && leftDistance == 5) {
                    result[y][x] = 11;
                }  else if (frontDistance == 5 && rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 14;
                } else if (frontDistance == 5 && rightDistance == 5) {
                    result[y][x] = 5;
                } else if (frontDistance == 5 && leftDistance == 5) {
                    result[y][x] = 6;
                } else if (rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 9;
                } else if (rightDistance == 5) {
                    result[y][x] = 1;
                } else if (leftDistance == 5) {
                    result[y][x] = 3;
                } else if (frontDistance == 5) {
                    result[y][x] = 4;
                }
            }
            case W -> {
                if (backDistance == 5 && rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 11;
                } else if (frontDistance == 5 && backDistance == 5 && leftDistance == 5) {
                    result[y][x] = 14;
                }  else if (frontDistance == 5 && rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 13;
                } else if (frontDistance == 5 && rightDistance == 5) {
                    result[y][x] = 8;
                } else if (frontDistance == 5 && leftDistance == 5) {
                    result[y][x] = 5;
                } else if (rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 10;
                } else if (rightDistance == 5) {
                    result[y][x] = 2;
                } else if (leftDistance == 5) {
                    result[y][x] = 4;
                } else if (frontDistance == 5) {
                    result[y][x] = 1;
                }
            }
            case E -> {
                if (backDistance == 5 && rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 13;
                } else if (frontDistance == 5 && backDistance == 5 && leftDistance == 5) {
                    result[y][x] = 12;
                }  else if (frontDistance == 5 && rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 11;
                } else if (frontDistance == 5 && rightDistance == 5) {
                    result[y][x] = 6;
                } else if (frontDistance == 5 && leftDistance == 5) {
                    result[y][x] = 7;
                } else if (rightDistance == 5 && leftDistance == 5) {
                    result[y][x] = 10;
                } else if (rightDistance == 5) {
                    result[y][x] = 4;
                } else if (leftDistance == 5) {
                    result[y][x] = 2;
                } else if (frontDistance == 5) {
                    result[y][x] = 3;
                }
            }
        }
    }
}
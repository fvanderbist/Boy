package com.boy.springbootexample.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String sayHello(@RequestParam(value = "name") String name) {
        return "Hello " + name + "!";
    }

    @RequestMapping("/uppercase")
    public String uppercase(@RequestParam(value = "name") String name) { return name.toUpperCase(); }

    @GetMapping("/random")
    public int uppercase(@RequestParam(value = "min") Integer min, @RequestParam(value = "max") Integer max) {
        final Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    @GetMapping("/url")
    public String fromURL(@RequestParam(value = "number") String number) throws IOException {

        String address = "http://numbersapi.com/" + number +"/math";
        URL url = new URL (address);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("text/plain", "charset=utf-8");
        con.setRequestProperty("Accept", "text/html");

        int code = con.getResponseCode();
        System.out.println(code);

        StringBuilder response = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
        return response.toString();
    }

    private static String getText(HttpURLConnection connection) throws IOException {

        // handle error response code it occurs
        int responseCode = connection.getResponseCode();
        InputStream inputStream;
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null)
            response.append(currentLine);

        in.close();

        return response.toString();
    }
}
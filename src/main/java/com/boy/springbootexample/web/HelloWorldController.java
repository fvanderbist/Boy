package com.boy.springbootexample.web;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class HelloWorldController {
    Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    @Bean
    WebClient getWebClient() {
        return WebClient.create("http://localhost:8082");
    }

/*    @Bean
    CommandLineRunner demo(WebClient client) {
        return args -> {
            client.get()
                    .uri("/randomInteger")
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .retrieve()
                    .bodyToFlux(Integer.class)
                    .map(s -> String.valueOf(s))
                    .subscribe(msg -> {
                        logger.info(msg);
                    });
        };
    }*/

    @RequestMapping("/tellme")
    public Flux<String> tellme() {
        WebClient webClient = WebClient.create("http://localhost:8082");

        return webClient.get()
                .uri("/randomInteger")
                .retrieve()
                .bodyToFlux(Integer.class)
                .map(s -> String.valueOf(s))
                .map(i -> {
                    try {
                        return getStringBuilder(getHttpURLConnection(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "null";
                });
    }

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
        return getStringBuilder(getHttpURLConnection(number));
    }

    private String getStringBuilder(HttpURLConnection con) throws IOException {
        StringBuilder response = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim()).append(System.getProperty("line.separator"));
            }
            System.out.println(response.toString());
        }
        return response.toString();
    }

    private HttpURLConnection getHttpURLConnection(@RequestParam("number") String number) throws IOException {
        String address = "http://numbersapi.com/" + number +"/math";
        URL url = new URL (address);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("text/plain", "charset=utf-8");
        con.setRequestProperty("Accept", "text/html");
        return con;
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

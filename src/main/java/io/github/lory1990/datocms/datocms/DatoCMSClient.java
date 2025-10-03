package io.github.lory1990.datocms.datocms;

import io.github.lory1990.datocms.datocms.dto.GetDataRequestDTO;
import io.github.lory1990.datocms.datocms.dto.GetDataResponseDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DatoCMSClient {

    private final String apikey;
    private final String environment;
    private final HttpClient httpClient;
    private final Gson gson;

    public DatoCMSClient(String apikey, String environment) {
        this.apikey = apikey;
        this.environment = environment;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public DatoCMSClient(String apikey){
        this(apikey, "main");
    }

    public <A> A getData(GetDataRequestDTO labelsDTO, Class<A> responseType) throws IOException, InterruptedException {
        String requestBody = gson.toJson(labelsDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://graphql.datocms.com/"))
                .header("Content-Type", "application/json")
                .header("x-environment", environment)
                .header("Authorization", "Bearer " + apikey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Type type = TypeToken.getParameterized(GetDataResponseDTO.class, responseType).getType();
        GetDataResponseDTO<A> data =  gson.fromJson(response.body(), type);
        return data.getData();
    }

}

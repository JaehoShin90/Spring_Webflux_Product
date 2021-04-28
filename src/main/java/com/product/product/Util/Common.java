package com.product.product.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.product.Dto.ProductDtlDto;
import com.product.product.Request.CartGroupReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.lang.reflect.Field;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class Common {

    private final WebClient webClient;

    public <T> T MapToObject (Map<String, Object> data, T t){
        Field[] filds = t.getClass().getDeclaredFields();
        for(Field fild : filds){
            fild.setAccessible(true);
            if(data.get(fild.getName()) != null){
                try {
                    fild.set(t, data.get(fild.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }

    public <T> T MapToObjectWithoutValue (Map<String, Object> data, T t){
        Field[] filds = t.getClass().getDeclaredFields();
        for(Field fild : filds){
            fild.setAccessible(true);
            try {
                if(data.get(fild.getName()) != null && (fild.get(t) == null || (fild.getType().getTypeName().equals("int") && (Integer)fild.get(t) == 0))){
                    fild.set(t, data.get(fild.getName()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public String ObjectToJson(Object data){
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        return json;
    }

    public <T> T setCopyData (T org, T target){
        Field[] orgFilds = org.getClass().getDeclaredFields();
        for(Field fild : orgFilds){
            fild.setAccessible(true);
            try{
                if(fild.get(target) != null && (fild.get(org) == null || fild.get(org).toString().equals("") || fild.get(org).toString().equals("0"))){
                    fild.set(org, fild.get(target));
                }
            }catch (Exception e){
                log.error(e.toString());
            }
        }
        return org;
    }

    public WebClient.ResponseSpec callAPIPublisher(String uri, Publisher param, Class cls){
//        log.info("param : {}", param.toString());
        return webClient.mutate()
                .build()
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(param, cls)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError()
                        , clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(RuntimeException::new)
                );
    }

    public WebClient.ResponseSpec callAPIObject(String uri, Object param){
        return webClient.mutate()
                .build()
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(param)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError()
                        , clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(RuntimeException::new)
                );
    }
}

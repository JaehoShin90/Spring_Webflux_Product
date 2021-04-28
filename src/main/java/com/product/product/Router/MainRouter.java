package com.product.product.Router;

import com.product.product.Handler.MainHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;



@Configuration
public class MainRouter {

    @Bean
    public RouterFunction<ServerResponse> route(MainHandler mainHandler){
        return RouterFunctions.route()
                .POST("/api/setCartList", RequestPredicates.all(), mainHandler::setOmCartList)
                .GET("/api/getOmcartCount", RequestPredicates.all(), mainHandler::getOmcartCount)
                .POST("/api/delCartInfo", RequestPredicates.all(), mainHandler::delCartInfo)
                .POST("/api/delAllCartInfo", RequestPredicates.all(), mainHandler::delAllCartInfo)
                .POST("/api/updateOmCartOdQty", RequestPredicates.all(), mainHandler::updateOmCartOdQty)
                .GET("/api/getCartListAfterGroupby", RequestPredicates.all(), mainHandler::getCartListAfterGroupby)
                .GET("/api/getCartList", RequestPredicates.all(), mainHandler::getCartList)
                .GET("/api/getCartListParallel", RequestPredicates.all(), mainHandler::getCartListParallel)
                .POST("/test", RequestPredicates.all(), mainHandler::test)
                .build();
    }
}

package com.product.product.security;

public class MultipartCsrfFilter {//implements WebFilter {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        if(!HttpMethod.GET.equals(exchange.getRequest().getMethod())){
//            return exchange.getMultipartData().flatMap(v -> {
//                Map<String, Part> map = v.toSingleValueMap();
//                if(map.get("_csrf") != null){
//                    exchange.getRequest()
//                            .mutate()
//                            .header("X-CSRF-TOKEN", ((FormFieldPart) map.get("_csrf")).value())
//                            .build();
//                }
//                return chain.filter(exchange);
//            });
//        }else{
//            return chain.filter(exchange);
//        }
//    }
//    @Override
//    public Mono<Void> filter(ServerWebExchange serverWebExchange,
//                             WebFilterChain webFilterChain) {
//        if(!HttpMethod.GET.equals(serverWebExchange.getRequest().getMethod())){
//            return serverWebExchange.getMultipartData().flatMap(v -> {
//                Map<String, Part> map = v.toSingleValueMap();
//                if(map.get("_csrf") != null){
//                    serverWebExchange.getRequest()
//                            .mutate()
//                            .header("X-CSRF-TOKEN", ((FormFieldPart) map.get("_csrf")).value())
//                            .build();
//                }
//                return webFilterChain.filter(serverWebExchange);
//            });
//        }else{
//            return webFilterChain.filter(serverWebExchange);
//        }
//    }
}

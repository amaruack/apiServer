# api 구현 시스템

## 시스템 spec
Spring Boot 2.7.7  
Spring JPA + QueryDSL 5.0.0   
apache commons-csv 1.9.0 : CSV file Read 라이브러리    
bucket4j 7.5.0 : rate limit 라이브러리     
jupiter 5.8.2 : junit 5 스프링 라이브러리  
swagger3, OAS3 : spring doc   
H2 database  
maven 

## 시스템 실행 방법
<span style="color:blue">***mvn compile***</span>  실행하여 Qclass 생성   
<span style="color:blue">***target/generated-sources/java***</span> 를 source folder 로 설정   
application.yml 의 daou.root-path 를 알맞게 지정  
Application 클래스 실행 

## 시스템 swagger ui 
[GET] http://127.0.0.1:8080/swagger-ui

## 시스템 api 목록
검색: [GET] http://127.0.0.1:8080/shop-history  
등록: [POST] http://127.0.0.1:8080/shop-history   
조회: [GET] http://127.0.0.1:8080/shop-history/{id}   
수정: [PUT] http://127.0.0.1:8080/shop-history/{id}  
삭제: [DELETE] http://127.0.0.1:8080/shop-history/{id}   



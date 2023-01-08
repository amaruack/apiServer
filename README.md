# api 구현 시스템

## 시스템 spec
Spring Boot 2.7.7  
Spring JPA + QueryDSL 5.0.0   
apache commons-csv 1.9.0 : CSV file Read 라이브러리    
bucket4j 7.5.0 : rate limit 라이브러리     
jupiter 5.8.2 : junit 5 스프링 라이브러리    
H2 database  
maven 

## 시스템 실행 방법
<span style="color:blue">***mvn compile***</span>  실행하여 Qclass 생성   
<span style="color:blue">***target/generated-sources/java***</span> 를 source folder 로 설정   
application.yml 의 daou.root-path 를 알맞게 지정  
DaouApplication 클래스 실행 

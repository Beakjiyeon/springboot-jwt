## springboot-jwt
* JWT 를 이용한 인증 서버 만들기
<br/>
<br/>

## Stack
### Backend
* Spring Boot
* Spring Security
* MySQL
<br/>
<br/>

## 기능 별 로직
### 기본 동작은 간단하다.
1. 클라이언트에서 ID/PW 로 로그인 요청
2. 서버에서 DB에 해당 ID/PW 를 가진 유저가 있다면 액세스 토큰, 리프레시 토큰을 발급한다.
3. 클라이언트는 발급 받은 액세스 토큰을 헤더에 담아 인증이 필요한 API 를 이용할 수 있다.
<br/>
<br/>

### 로그인
[서버]
* 요청 정보 (id, pw) 검증 
* 액세스 토큰, 리프레시 토큰 발행 
* 헤더에 토큰을 담아 응답
<br/>
<br/>

### 인증이 필요한 API 요청
[프론트]
* 액세스 토큰을 헤더(Authorization)에 담아 요청

[서버]
* 헤더에서 액세스 토큰을 조회, 유효성 체크
  * 액세스 토큰이 유효한 경우
    * 시큐리티 컨텍스트에 Authentication 객체 반영
  * 액세스 토큰이 만료된 경우
    * 필터 단위에서 자동으로 403 리턴
<br/>
<br/>

## 실행 가이드
* application.yml 
  * 맨 처음 프로젝트를 실행할 때는 create 로 수정하기
```yaml
  jpa:
    hibernate:
      ddl-auto: validate # 
```

* db
  * 회원가입 로직 구현 전이므로 미리 db에 유저 정보와 유저 권한 정보를 세팅해야 한다.
```sql
insert into member values('member_A', '1234');
insert into member_roles values('member_A', 'USER');
```
<br/>
<br/>

## Todo
[서버]
* 회원 가입 로직 구현하기
* 리프레시 토큰 정책 구체화하기
* 이메일 인증 기능 구현하기
* API 문서 만들기

[프론트]
* 프론트 구현하기 이왕이면 리액트 써보기
<br/>
<br/>

## 참고 레퍼런스
* https://gksdudrb922.tistory.com/217
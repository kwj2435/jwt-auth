## Spring Security 6.x과 JWT를 이용한 인증, 인가 구현

- 6.x 버전이 Release 되며 사용법이 달라진 Spring Security에 대한 사용법을 숙지
- Spring Security 와 JWT를 이용한 토큰 기반 인증, 인가 기능 구현
- 복잡한 구현 사항을 미리 정리해두어 추후 프로젝트에 바로 적용할 수 있도록 정리

--- 
### Implemented Features.
1. 회원 가입
2. 로그인과 Access Token, Refresh Token 발급
3. RTR(Refresh Token Rotation) 기반 토큰 재발급
4. 토큰 기반 API 권한 인증
5. 기타
---
### How To Run.
해당 프로젝트는 Docker-Compose 와 Docker 기반으로 동작됩니다.  
로컬환경에서 해당 프로젝트를 실행하기 위해서는 Docker와 Docker-Compose가 설치되어 있어야 합니다.  
로컬 환경에 Docker 설치및 설정이 완료 되었다면 아래 명령어를 실행 시켜 주세요.  

~~~
docker-compose up
docker run
~~~
---
### Description By Feature.
### 1. 회원 가입
```
curl --location 'localhost:9091/api/v1/user/join' \
--header 'Content-Type: application/json' \
--data '{
    "userName":"test",
    "password":"test"
}'
```
회원가입 로직은 세션 방식과 JWT 방식의 차이가 없다.
  
### 2. 로그인과 Access Token, Refresh Token 발급
```
curl --location 
--request POST 'http://localhost:9091/login?username=test&password=test' \
```
세션 방식은 서버 세션이 유저 정보를 저장하지만, JWT 방식은 토큰을 생성하여 응답한다.  
Spring Security는 클라이언트의 요청이 Dispatcher Servlet에 도달하기전 필터단계에서 가로챈 후, 검증(인증/인가)를 진행한다.  

Spring Security의 경우 기본적으로 Form Login 방식을 동작되도록 설정되어 있다.  
Form Login 방식의 경우 클라이언트에서 username, password를 전달하면 Security Filter Chain중  
UsernamePasswordAuthentication 필터에서 회원 검증을 진행한다.  
(FormLogin의 경우 UsernamePasswordAuthentication 필터가 Security에 의해 자동으로 생성된다.)
  
일반적으로 Spring Security의 Form Login방식은 SSR(Server Side Rendering) 방식을 사용한다.  

JWT를 이용하는 경우 대부분의 클라이언트는 CSR(Client Side Rendering) 방식을 사용하기 때문에, Form Login 방식을  
Disable 한후 JWT 토큰의 인증, 인가가 수행될 수 있도록 Custom Filter를 구현 및 등록해줘야한다.  
Custom Filter는 기존 Form Login을 처리하는 UsernamePasswordAuthentication 필터를 대신하기 때문에 해당 필터의 위치에  
추가해주어야 한다.  UsernamePasswordAuthentication 필터를 상속하여 구현하면, 기본적으로 해당 필터의 정의에 따라 '/login'  
URL에 대한 'POST'요청을 처리하게 된다.

**Spring Security Login Process**
- '/login' EndPoint로 유입된 사용자의 로그인 요청은 UsernamePasswordAuthenticationFilter를 상속한 LoginFilter로 유입된다.  
- 유입된 요청은 LoginFilter의 AttemptAuthentication Method에서 username, password를 사용하여 UsernamePasswordAuthenticationToken
객체를 생성한다.
- 생성된 Token 객체는 AuthenticationManager의 authenticate 메서드의 파라미터로 담겨 전달된다.  
- AuthenticationManager에 담겨진 Token 객체는 내부 인증 과정에서 UserDetailsService를 상속한  
CustomUserDetailsService의 loadUserByUsername 메서드를 통해 유효한 로그인 요청인지 판단하게 된다.
- loadUserByUsername 메서드에서 생성된 UserDetails 객체로 사용자의 인증여부를 판단하게 되며, 인증이 성공했을 경우 LoginFilter의  
  successfulAuthentication 메서드를 호출하며 인증이 마무리 된다.  

### 3. RRT(Rotation Refresh Token) 기반 토큰 재발급
```
curl --location 
--request POST 'localhost:9091/api/v1/auth/refresh' \
--header 'Authorization: Bearer <AccessToken> \
--header 'Refresh-Token: <RefreshToken> \
```

Access Token의 특성상 짧은 유효기간으로 인해 클라이언트가 빈번한 재로그인을 해야한다는 단점이 존재한다.  
반대로 Access Token의 유효기간을 길게 설정할 경우, Access Token 탈취시 보안 측면의 위험성이 커지게 된다.  

이런 단점을 보완하고자 Refresh Token을 도입하게 됨.   
Refresh Token은 처음 사용자 로그인 시점에 Access Token과 함께 전달되며, 첫 로그인 이후부터는 Access Token만 헤더에 담아 전달하게 된다.  
만료된 Access Token을 사용하여 서버에 요청시, Access Token이 만료되었다는 서버 응답에 따라 클라이언트에서는 Refresh Token을 Access Token과 함께  
서버로 전달하고, 서버는 Refresh Token을 확인하여 사용자에게 새로운 Access Token을 전달하게 된다.

- Access Token, Refresh Token 서버 전달
- Access Token의 만료 여부 및 유효성 확인 (만료 되지 않았을 경우 Exception)
- Refresh Token의 유효성 확인
- 전달된 Refresh Token과 DB에 저장된 유저 ID의 Refresh Token의 동일 여부 확인
- Access Token, Refresh Token 재발급 및 Refresh Token DB 저장

이런 방식의 인증 구조는 Access Token의 유효기간을 짧게 가져감으로써 보안측면에 유리함을 가질 수 있고,  
Refresh Token의 긴 유효기간으로 Access Token만  존재 했을때의 빈번한 재로그인이 필요한 단점 또한 해결 할 수 있다.  

보안측면에서도 Refresh Token의 경우 Access Token의 재발급때만 사용되기 때문에 상대적으로 탈취 위험 노출이 적다.

'그러나' Refresh Token 또한 보안적인 위험성이 존재한다.  
Access Token과 마찬가지로 Refresh Token 또한 탈취 위험성이 존재한다.  

토큰이 탈취 될 수 있는 상황 예시  
- XSS(Cross-Site Scripting) - 해커에 의해 작성된 악성 스크립트로 인해 사용자의 토큰 탈취
- 악성 애플리케이션 - 사용자가 설치한 악성 모바일 앱 혹은 악성 브라우저 확장 프로그램으로 애플리케이션의 토큰 탈취
- 중간자 공격 - 공격자가 사용자의 네트워크 통신을 가로채어 토큰을 탈취함 (공공 Wi-Fi 통신을 가로채어 토큰 탈취) 

Refresh Token의 보안 위험성으로, RTR(Refresh Token Rotation) 기법을 사용한다.  
RTT는 Refresh Token이 사용될 때마다, 새로운 Refresh Token을 발급하고 이전 토큰을 무효화하는 방식으로, 탈취된 Refresh Token의 유효성을 최소화 한다.  

Access Token과 RTT가 적용된 Refresh Token의 구조가 보안상 완벽하다는 관점으로 바라보는 것이 아닌, 다양한 보안 정책, 인증 정책이 사용되어야 한다.  

### 4. API 권한 인증
API는 크게 '인증이 필요한' API와 '인증이 불필요한' API 두가지 종류로 구분된다.  
Security Config에 정의된 URL 정책에 따라 인증이 불필요한 API의 경우 별도의 토큰 인증이 되지 않더라도 접근이 가능하며,  
인증이 필요한 API의 경우 Header에 포함된 AccessToken의 값을 JwtFilter에서 검증하게 된다.  
검증이 성공했을 경우 JwtFilter에서 CustomUserDetails 객체를 생성하게 되고, 해당 객체는 Authentication 객체로 감싸진 후 최종적으로    
SpringSecurityContextHolder에 등록되게 된다.  
이렇게 저장된 인증 객체는 추후 로직에서 인증이 필요한 부분마다 사용되게 된다.  

### 5. 기타
- Filter Exception Handling
- Rest Controller Advice를 이용한 Api Exception Handling
- Custom Exception 작성 및 적용
- Spring Event Listener 이용 Refresh Token DB 저장

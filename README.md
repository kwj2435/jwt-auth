## Implementing Authentication and Authorization Using Spring Security 6.x and JWT

---
#### 24.06.17 Developed By uijin kim  
This Repository written that Implemented Authorization, Authentication Java Code Using Spring Security 6.x and JWT.  
also written explaining each feature.
--- 
### Implemented Features.
1. Registration
2. Login And Access Token, Refresh Token Generation
3. RRT(Rotation Refresh Token)
4. Authorization

---
### How To Run.
This project is based on Docker-Compose and Docker.
You must install Docker Desktop before launching this project.

If Docker Desktop is installed in your local environment, execute the following Docker command.

~~~
docker-compose up
docker run
~~~
---
Registration  
회원가입 로직은 세션 방식과 JWT 방식의 차이가 없다.

  
Login  
세션 방식은 서버 세션이 유저 정보를 저장하지만, JWT 방식은 토큰을 생성하여 응답한다.  
Spring Security는 클라이언트의 요청이 Dispatcher Servlet에 도달하기전 필터단계에서 가로챈 후, 검증(인증/인가)를 진행한다.  

Spring Security의 경우 기본적으로 Form Login 방식을 동작되도록 설정되어 있다.  
Form Login 방식의 경우 클라이언트에서 username, password를 전달하면 Security Filter Chain중 UsernamePasswordAuthentication 필터에서 
회원 검증을 진행한다.(FormLogin의 경우 UsernamePasswordAuthentication 필터가 Security에 의해 자동으로 생성된다.)
  
일반적으로 Spring Security의 Form Login방식은 SSR(ServerSide Rendering) 방식을 사용한다.  

JWT를 이용하는 경우 대부분의 클라이언트는 CSR(ClientSide Rendering) 방식을 사용하기 때문에, Form Login 방식을 Disable 한후
JWT 토큰의 인증, 인가가 수행될 수 있도록 Custom Filter를 구현 및 등록해줘야한다.  
Custom Filter는 기존 Form Login을 처리하는 UsernamePasswordAuthentication 필터를 대신하기 때문에 해당 필터의 위치에 추가해주어야 한다.  
UsernamePasswordAuthentication 필터를 상속하여 구현하면, 기본적으로 해당 필터의 정의에 따라 '/login' URL에 대한 'POST'요청을 처리하게 된다.


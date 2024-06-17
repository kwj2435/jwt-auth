# 사용할 JDK 이미지
FROM openjdk:17-jdk-slim
LABEL authors="kimuijin"
# 이미지 생성시 실행할 명령 (gradle bootjar 통해 jar 파일 생성하도록 명령어 입력)
CMD ["./gradlew", "clean", "bootjar"]
# spring 변수 추가 입력 (jar 생성시 local profile로 실행될 수 있도록 명시)
ENV SPRING_PROFILES_ACTIVE=local
# jar 파일 변수 생성
ARG JAR_FILE_PATH=build/libs/*.jar
# 생성된 jar 파일 경로에서 jar 파일을 복사해서 가져옴
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
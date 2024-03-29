FROM openjdk:11.0.12 AS build_binary

RUN mkdir -p /app
COPY ./ /app
WORKDIR /app

RUN ./gradlew --no-daemon linkReleaseExecutableNative

FROM debian:buster-slim
RUN apt-get update && apt-get install -y postgresql-client
WORKDIR /app
COPY --from=build_binary /app/build/bin/native/releaseExecutable/parse-congress-info.kexe ./
ENV PATH "/app:$PATH"

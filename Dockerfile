FROM eclipse-temurin:17-jdk

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl bash ca-certificates git \
  && rm -rf /var/lib/apt/lists/*

RUN curl -Ls https://raw.githubusercontent.com/dwijnand/sbt-extras/master/sbt \
    -o /usr/local/bin/sbt \
 && chmod 0755 /usr/local/bin/sbt

WORKDIR /app

COPY project ./project
COPY build.sbt ./

RUN sbt -batch update

COPY . .

CMD ["sbt", "run"]

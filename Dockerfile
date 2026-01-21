# Use the requested base image
FROM hseeberger/scala-sbt:11.0.16.1_1.8.2_2.13.10

WORKDIR /app

# Copy build definition first (better layer caching)
COPY build.sbt /app/
COPY project /app/project

# Pre-fetch dependencies (speeds up later rebuilds)
RUN sbt -batch update

# Copy the rest of the source
COPY . /app

# Run the application
# Option A: run the default main class (if configured in build.sbt)
CMD ["sbt", "-batch", "run"]

# Option B (recommended if multiple mains): uncomment and set your main class explicitly:
# CMD ["sbt", "-batch", "runMain", "de.htwg.se.Hearts.Hearts"]

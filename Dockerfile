# Start with a base image containing Java runtime
FROM gradle:7.6.1-jdk17 AS builder

# Build CLI tool
WORKDIR /build/CLI
COPY CLI /build/CLI
RUN ./gradlew clean distZip

# Final image
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /build/CLI/build/distributions/keppel-cli.zip .

RUN apt-get update && apt-get install -y unzip && \
    unzip keppel-cli.zip && rm keppel-cli.zip && \
    apt-get purge -y --auto-remove unzip && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/app/keppel-cli/bin/keppel-cli"]

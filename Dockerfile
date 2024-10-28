# ---------------------------------------------------------------------
# (1) build stage
# ---------------------------------------------------------------------
FROM observabilitystack/graalvm-maven-builder:22.0.1-ol9 AS builder

ADD . /build
WORKDIR /build

# Build application
RUN mvn -B native:compile -P native --no-transfer-progress -DskipTests=true && \
    chmod +x /build/target/prometheus-alertecho

# ---------------------------------------------------------------------
# (2) run stage
# ---------------------------------------------------------------------
FROM debian:bookworm-slim

COPY --from=builder "/build/target/prometheus-alertecho" /srv/prometheus-alertecho

HEALTHCHECK --interval=5s --timeout=1s CMD curl -f http://localhost:9095/health
EXPOSE 9095
CMD exec /srv/prometheus-alertecho

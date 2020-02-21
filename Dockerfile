# Repo: https://github.com/poshjosh/bcutil
# ---------------
# Pull base image
# ---------------
FROM maven:3-alpine
# ---------------
# Create and use non-root user
# ---------------
#RUN addgroup -S looseboxes && adduser -S poshjosh -G looseboxes
#USER looseboxes:poshjosh
#Above caused error: unable to find user looseboxes: no matching entries in passwd file
# ---------------
# Speed up Maven a bit
# ---------------
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
#ENTRYPOINT ["/usr/bin/mvn"]
# ---------------
# Install project dependencies and keep sources
# ---------------
# make source folder
# ---------------
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
# ---------------
# Install maven dependency packages (keep in image)
# ---------------
COPY pom.xml /usr/src/app
# Now handled by Jenkins
# ---------------
# RUN mvn -T 1C install && rm -rf target
# ---------------
# Copy other source files (keep in image)
# ---------------
COPY src /usr/src/app/src

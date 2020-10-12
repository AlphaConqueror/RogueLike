.PHONY: build

JAVA_HOME=/home/s9latimm/.jdks/openjdk-14.0.2
EMPTY=../empty
COMM_LIB=../comm-lib
SYSTEMTEST=../systemtest
PREMIUM=../premium

default: schema build

build: premium

schema:
	@ mkdir -p ${EMPTY}/src/main/resources/schema/
	@ cp -v src/main/resources/schema/*.json ${EMPTY}/src/main/resources/schema/
	@ mkdir -p ${SYSTEMTEST}/src/main/resources/schema/
	@ cp -v src/main/resources/schema/*.json ${SYSTEMTEST}/src/main/resources/schema/
	@ mkdir -p ${PREMIUM}/src/main/resources/schema/
	@ cp -v src/main/resources/schema/*.json ${PREMIUM}/src/main/resources/schema/
	@ cp -v -r config/* ${EMPTY}/config/
	@ cp -v -r config/* ${PREMIUM}/config/

comm-lib:
	cd ${COMM_LIB} && gradle wrapper && ./gradlew --refresh-dependencies --no-build-cache -x check -x test jar
	@ mkdir -p ${SYSTEMTEST}/libs/external
	@ mv -v ${COMM_LIB}/libs/comm-lib-0.5.jar ${SYSTEMTEST}/libs/external/

systemtest: comm-lib
	cd ${SYSTEMTEST} && gradle wrapper && ./gradlew --refresh-dependencies --no-build-cache -x check -x test  jar
	@ mkdir -p ${PREMIUM}/libs/external
	@ mv -v ${SYSTEMTEST}/libs/systemtest-api-1.2.jar ${PREMIUM}/libs/external/
	@ mv -v ${SYSTEMTEST}/libs/external/comm-lib-0.5.jar ${PREMIUM}/libs/external/

premium: systemtest
	cd ${PREMIUM} && gradle wrapper && ./gradlew --refresh-dependencies --no-build-cache -x check -x test  jar
	@ mkdir -p libs/external
	@ mv -v ${PREMIUM}/libs/sopra.jar libs/external/
	#	@ mv -v ${PREMIUM}/libs/external/systemtest-api-1.2.jar libs/external/
	#	@ mv -v ${PREMIUM}/libs/external/comm-lib-0.5.jar libs/external/

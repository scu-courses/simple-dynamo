MVNCMD=mvn
MVNCOMPILE=$(MVNCMD) compile
MVNPACKAGE=$(MVNCMD) package
MVNCLEAN=$(MVNCMD) clean
MVNTEST=$(MVNCMD) test

all: clean unit-test build deploy

build:
	echo "Starting to build [simple-dynamo]..."
	$(MVNPACKAGE)

deploy:
	echo "Copying all executables to bin/ ..."
	if [ ! -d "bin" ]; then mkdir bin; fi
	cp common/target/common-1.0.0.jar bin/common.jar
	cp server/target/server-1.0.0.jar bin/server.jar
	cp client/target/client-1.0.0.jar bin/client.jar
	cp admin/target/admin-1.0.0.jar bin/admin.jar

clean:
	echo "Cleaning legacy files..."
	$(MVNCLEAN)

unit-test:
	echo "Running unit tests for [simple-dynamo]..."
	$(MVNTEST)

build:
	./gradlew build

clean:
	./gradlew clean && rm -rf build

install:
	./gradlew publishToMavenLocal

doc:
	./gradlew javadoc
	
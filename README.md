# BradyBot

## Build

```console
./gradlew build
```

## Creating a JAR file

```console
./gradlew jar
```

then the jar file will be in `brady-bot/build/libs/`.

## Compile MicroRTS and build its jar file

```console
javac -cp "lib/*:src" -d bin $(find . -name "*.java") # compile source files
cd bin
find ../lib -name "*.jar" | xargs -n 1 jar xvf # extract the contents of the JAR dependencies
jar cvf microrts.jar $(find . -name '*.class' -type f) # create a single JAR file with sources and dependencies
```

or if you have `ant` installed.

```console
ant export_jar
```

in the MicroRTS project folder.
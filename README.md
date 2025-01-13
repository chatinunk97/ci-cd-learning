# What I Learn

There are two things that made my life hard:

## 1. Docker Building Context

Docker looks for files to include in an image within its **building context** (the directory where the Dockerfile is
located). It will **not look outside** this context.  
~~The problem I faced was setting `WORKDIR` to `/app` but then trying to find `/target/<myProgramName>.jar`, which was
built by Maven in the `/target` folder. The Docker build process was looking in the wrong place because of this
mismatch.~~

Went down the wrong road for days. So the building context for `build-push-action` is set to Git context which is the
repository `https://github.com/<owner>/<repo>.git#<ref>.`

Now the problem that actually happened here is explained in the [build-push-action repository](www.google.com)
> Git context
>
>By default, this action uses the Git context, so you don't need to use the actions/checkout action to check out the
> repository as this will be done directly by BuildKit.



> Be careful because any file mutation in the steps that precede the build step will be ignored, including processing of
> the .dockerignore file since the context is based on the Git reference. However, you can use the Path context using
> the
> context input alongside the actions/checkout action to remove this restriction.
> Example structure:

```
└─CICD_Demo (the Path where Maven looks and builds jar in /target)
    ├─.github
    │  └─workflows
    ├─src
    │  ├─main
    │  │  ├─java
    │  │  │  └─org
    │  │  │      └─example
    │  │  └─resources
    │  └─test
    │      └─java
    └─target
        |- [CICD_Demo-1.0-SNAPSHOT.jar](target%2FCICD_Demo-1.0-SNAPSHOT.jar)
```

**Solution**: ~~Remove the `WORKDIR` setting to avoid this issue.~~ Simply read the documentation and follow it

## 2. Docker Tag Name

The tag name for your Docker image must include the full path to your Docker Hub repository, for example:  
`chatinun/learningdevops:initial`

## 3. Java Compiler Version (`compiler.source`)

The `compiler.source` property in the Maven configuration determines the JDK version to use. For example:

```xml

<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

## 4. JAR File Manifest (`MANIFEST.MF`)

The manifest file in a JAR defines the entry point for the application. The `Main-Class` attribute tells the Java
runtime which class contains the `main()` method:

```
Manifest-Version: 1.0
Main-Class: org.example.Main
Class-Path: lib/dependency.jar
```

You can configure this in your `pom.xml`:

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <configuration>
                <archive>
                    <manifestEntries>
                        <Main-Class>org.example.Main</Main-Class>
                    </manifestEntries>
                </archive>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 5. Don't Leave Your `target` Folder Open

Leaving the `target` folder open when running `mvn clean` can prevent Maven from properly cleaning the directory and
manipulating the files as needed. Make sure to close it before cleaning the build.

---

### **Note on Java Programs in CI/CD:**

In **CI/CD pipelines** or **Docker environments**, Java programs that require interactive input (e.g., using `Scanner`)
are not ideal. This is because these environments are **non-interactive**. Instead, consider:

1. Accepting input via **command-line arguments**, **environment variables**, or **files**.
2. Web applications (e.g., **Spring Boot**) are designed to run as background services and do not need interactive
   input. They continuously listen for requests and are more suitable for Docker or CI/CD environments.

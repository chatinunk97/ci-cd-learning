What I learn is there are two things that made my life hard

1. Docker Building context
    This is where docker looks,when it's building an image it will not look outside the context
   (which is the same directory the dockerfile is placed) 
    The problem I faced was setting WORKDIR to /app first
    But then try to find /target/<myProgramName>.jar which is built by MVN
    Now the situation looks something like this
    ```
    └─CICD_Demo (the Path where MVN looks and built jar in /target)
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
    └─app (the WORKDIR I created in the dockerfile)
       (--------- THERE'S  NO DOCKERFILE HERE ?!!! ---------- )
    ```
    Now that's the problem docker building is looking in the wrong place because I forced it to
    The solution was just to remove the WORKDIR
2. The tag name has to be the full path to Docker hub 
    (chatinun/learningdevops:initial)
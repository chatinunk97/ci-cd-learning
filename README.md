---

# CI/CD Learning Project

This project demonstrates the implementation of a Continuous Integration and Continuous Deployment (CI/CD) pipeline using GitHub Actions, Maven, and Docker.  

## Table of Contents  

- [Project Overview](#project-overview)  
- [Prerequisites](#prerequisites)  
- [Setup Instructions](#setup-instructions)  
- [Understanding the GitHub Actions Workflow](#understanding-the-github-actions-workflow)  
- [Handling Docker Build Context in GitHub Actions](#handling-docker-build-context-in-github-actions)  
- [Mistakes and Lessons Learned](#mistakes-and-lessons-learned)  
- [Contributing](#contributing)  
- [License](#license)  

## Project Overview  

This repository showcases a CI/CD pipeline that automates the build, test, and deployment processes for a Java application. The pipeline is configured using GitHub Actions and includes steps for:  

- Checking out the code  
- Setting up the Java Development Kit (JDK)  
- Building the project with Maven  
- Verifying the existence of the JAR file  
- Logging into Docker Hub  
- Setting up QEMU and Docker Buildx  
- Building and pushing the Docker image  

## Prerequisites  

Before you begin, ensure you have the following installed:  

- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)  
- [Maven](https://maven.apache.org/install.html)  
- [Docker](https://docs.docker.com/get-docker/)  

## Setup Instructions  

1. **Clone the Repository:**  

   ```bash
   git clone https://github.com/chatinunk97/ci-cd-learning.git
   cd ci-cd-learning
   ```  

2. **Configure Environment Variables:**  

   - Set up the following GitHub repository secrets:  
     - `DOCKERHUB_USERNAME`: Your Docker Hub username  
     - `DOCKERHUB_TOKEN`: Your Docker Hub access token  

3. **Run the Workflow:**  

   - Push your changes to the `main` branch to trigger the GitHub Actions workflow.  

## Understanding the GitHub Actions Workflow  

The GitHub Actions workflow is defined in `.github/workflows/main.yml` and consists of the following jobs and steps:  

- **Build Job:**  
  - **Runs on:** `ubuntu-latest`  
  - **Permissions:** Grants read access to contents and write access to packages.  
  - **Steps:**  
    1. **Checkout Code:** Uses `actions/checkout@v4` to clone the repository.  
    2. **Set Up JDK 11:** Uses `actions/setup-java@v4` to install Java 11.  
    3. **Build with Maven:** Runs `mvn -B package --file pom.xml` to build the project.  
    4. **Verify JAR File:** Checks the `target/` directory to ensure the JAR file exists.  
    5. **Login to Docker Hub:** Uses `docker/login-action@v3` to authenticate with Docker Hub.  
    6. **Set Up QEMU:** Uses `docker/setup-qemu-action@v3` to enable multi-platform builds.  
    7. **Set Up Docker Buildx:** Uses `docker/setup-buildx-action@v3` to set up Docker Buildx.  
    8. **Build and Push Docker Image:** Uses `docker/build-push-action@v6` to build and push the Docker image to Docker Hub.  

## Handling Docker Build Context in GitHub Actions  

When configuring GitHub Actions for building Docker images, it's crucial to understand how the build context is managed. By default, the `docker/build-push-action` utilizes the **Git context**, referencing the state of your repository at the last commit. This behavior can lead to challenges, especially when your build process generates artifacts (e.g., JAR files) that are not present in the committed codebase.  

**Issue Encountered:**  

In our CI/CD pipeline, we faced a build failure with the following error message:  

```
ERROR: failed to solve: failed to compute cache key: failed to calculate checksum of ref 4s2ukczv1um5t7b393lgo2fvl::a0q0aj999192kal9c88sa3d83: "/target/CICD_Demo-1.0-SNAPSHOT.jar": not found
```  

This error occurred because the Docker build process couldn't locate the `CICD_Demo-1.0-SNAPSHOT.jar` file in the `target/` directory. The root cause was that the Docker action, using the default Git context, did not include the `target/` directory generated during the Maven build step.  

**Solution Implemented:**  

To resolve this issue, we explicitly set the `context` parameter to the current workspace in our GitHub Actions workflow. This ensures that Docker uses the actual working directory, including all files generated during the workflow, as its build context.  

Here's the relevant section of our workflow configuration:  

```yaml
- name: Build and push Docker image
  uses: docker/build-push-action@v6
  with:
    push: true
    tags: chatinun/learningdevops:latest
    context: .
```

By setting `context: .`, we instruct Docker to consider the entire working directory—encompassing all modifications and generated artifacts—as the build context. This approach ensures that all necessary files are available during the Docker build process, preventing errors related to missing files.  

## Mistakes and Lessons Learned  
### Just read the documentation
The problem and explanation about context are all stated in the [official documentation](https://github.com/docker/build-push-action)  
So, take time and carefuly read the doc.

>Be careful because any file mutation in the steps that precede the build step will be ignored, including processing of the .dockerignore file since the context is based on the Git reference. However, you can use the Path context using the context input alongside the actions/checkout action to remove this restriction.


### The Wrong Path I Took  

I spent **days** trying to debug the issue, making incorrect assumptions and following misleading ideas. Some of the incorrect solutions I tried included:  

❌ **Thinking that the issue was caused by missing Maven dependencies.**  
I attempted to add extra steps in the GitHub Actions workflow to explicitly download dependencies again before the Docker build. This didn’t help because the issue was not related to dependencies but rather the **build context** used by Docker.  

❌ **Trying to manually copy the JAR file before the build step.**  
At one point, I added an extra command:  

```yaml
run: cp target/CICD_Demo-1.0-SNAPSHOT.jar .
```

This didn’t work because the file still wasn’t included in the Docker build context.  

❌ **Modifying `.dockerignore` unnecessarily.**  
I thought that the `.dockerignore` file might be preventing the `target/` directory from being included, so I tried removing it entirely. This turned out to be **completely unrelated** to the problem.  

### The Realization and Fix  

After several failed attempts, I finally understood that the core issue was **how Docker Buildx determines its context when using GitHub Actions**. By default, it uses the **Git reference** instead of the workspace, which means that any files created during the workflow (such as the JAR file) **won’t be included in the Docker build unless explicitly stated**.  

Once I set `context: .` in the Docker build step, everything worked perfectly.  

### Key Takeaways  

- If you’re using `docker/build-push-action`, make sure to **explicitly set the build context** to `.` to include workflow-generated files.  
- Debugging CI/CD issues can take a lot of trial and error. Keeping logs and systematically eliminating false assumptions is crucial.  
- Just because something works locally **doesn’t mean it will work in CI/CD**, since the environment behaves differently.  


## License  

This project is licensed under the [MIT License](LICENSE).  

---

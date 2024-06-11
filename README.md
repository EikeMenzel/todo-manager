# ToDo Manager

## How to execute the project?
### Via Docker Compose
you have two build choices:

1. `deployment-local-build.yml` here only the frontend gets manually built.
2. `deployment-local-build-all.yml` here is everything build.

If you are using Option 1. you need to make sure, that you logged in into the docker registry of the THM before. Because it pulls the images from the registry.

**You can login into the registry via:**

`docker login git-registry.thm.de -u [YOUR_EMAIL] -p [YOUR_ACCESS_TOKEN]`

**Important**

As soon as the backend was fully started it could take roughly 1 minute to execute it, as long as it is not executed, the frontend won't be reachable properly!

### Via IDEs
**Backend**
1. First do  `cd ./todo-manager-backend`
2. Next do `docker-compose -f docker-compose.yml up -d`
3. Now start IntellIJ and start the `discovery-service` as soon as it is fully started, go to step 4.
4. Now start the `database-service`, if it is successful, you should see in the console an successful connection to the database & see a OK from the `discovery-service`
5. Now start the `authentication-service`, `task-service` and the `gateway-service` as soon as they all started successfully. Go to Frontend Section.

**Frontend**
1. First do npm install
2. Now do npm start and navigate to http://localhost:4200

<br>

# Pipeline
- **Build Stage**
  - Builds the backend and frontend.

- **Test Stage**
  - Executes unit tests in the backend and end-to-end (E2E) tests in the frontend.

- **Lint Stage**
  - Validates the code for syntax errors and common issues, such as unused imports.

- **Code Quality Stage**
  - Performs static code analysis using SonarQube.

- **PushToRegistry Stage**
  - Pushes the images to the GitLab Registry.

- **Release Stage**
  - Releases a new version of the application as soon as it's pushed to the master branch.

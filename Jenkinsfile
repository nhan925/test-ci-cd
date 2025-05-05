def CHANGED_SERVICES = ""

pipeline {
    agent any
    
    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }
    
    environment {
        MINIMUM_COVERAGE = 70
        DOCKER_REGISTRY = "nhan925"
        SERVICES = "spring-petclinic-admin-server,spring-petclinic-api-gateway,spring-petclinic-config-server,spring-petclinic-discovery-server,spring-petclinic-customers-service,spring-petclinic-vets-service,spring-petclinic-visits-service"
    }
    
    stages {
        stage('Get Latest Commit') {
            steps {
                script {
                    // Get the latest commit hash
                    LATEST_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    echo "Latest Commit Hash: ${LATEST_COMMIT}"
                }
            }
        }

        stage('Detect Changes') {
            steps {
                script {                  
                    def compareTarget = env.CHANGE_TARGET ? "origin/${env.CHANGE_TARGET}" : "HEAD~1"
                    def changedFiles = sh(script: "git diff --name-only ${compareTarget}", returnStdout: true).trim()
                    
                    def changedServicesList = []
                    env.SERVICES.split(",").each { service ->
                        if (changedFiles.split("\n").any { it.startsWith(service) }) {
                            changedServicesList.add(service)
                        }
                    }
                    
                    CHANGED_SERVICES = changedServicesList.join(",")
                    
                    if (CHANGED_SERVICES.isEmpty() && 
                        changedFiles.split("\n").any { it == "pom.xml" || it.startsWith("src/") }) {
                        CHANGED_SERVICES = env.SERVICES
                    }
                    
                    echo "Services to build: ${CHANGED_SERVICES ?: 'None'}"
                }
            }
        }
        
        stage('Build & Test') {
            when { expression { return !CHANGED_SERVICES.isEmpty() } }
            steps {
                script {                  
                    sh "./mvnw verify --projects ${CHANGED_SERVICES}"
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    
                    // Make the build unstable if coverage is below threshold
                    recordCoverage(
                        tools: [[parser: 'JACOCO']],
                        sourceDirectories: [[path: 'src/main/java']],
                        sourceCodeRetention: 'EVERY_BUILD',
                        qualityGates: [
                            [threshold: env.MINIMUM_COVERAGE.toInteger(), metric: 'LINE', baseline: 'PROJECT', unstable: true]
                        ]
                    )
                    
                    // Now check if build became unstable due to coverage, and fail it explicitly
                    // script {
                    //     if (currentBuild.result == 'UNSTABLE') {
                    //         error "Build failed: Line is below the required minimum ${env.MINIMUM_COVERAGE}%"
                    //     }
                    // }
                }
            }
        }

        stage('Build Docker Images') {
            when { expression { return !CHANGED_SERVICES.isEmpty() } }
            steps {
                script {
                    echo "Building Docker image for ${CHANGED_SERVICES}"
                    whoami
                    sh "./mvnw clean install -X --projects ${CHANGED_SERVICES} -Dmaven.test.skip=true -P buildDocker -Ddocker.image.prefix=${env.DOCKER_REGISTRY} -Ddocker.image.tag=${LATEST_COMMIT} -Dcontainer.build.extraarg=\"--push\""
                }
            }
        }

        stage('Clean Docker Images') {
            when { expression { return !CHANGED_SERVICES.isEmpty() } }
            steps {
                script {
                    echo "Cleaning up Docker images"
                    sh "docker image prune -a -f"
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline completed with result: ${currentBuild.currentResult}"
            echo "Pipeline run by: ${currentBuild.getBuildCauses()[0].userId ?: 'unknown'}"
            echo "Completed at: ${new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(new Date())}"
            cleanWs()
        }
    }
}

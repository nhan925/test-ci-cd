pipeline {
    agent any
    
    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }
    
    environment {
        CHANGED_SERVICES = ""
        MINIMUM_COVERAGE = 70
        SERVICES = "'spring-petclinic-admin-server','spring-petclinic-api-gateway','spring-petclinic-config-server','spring-petclinic-discovery-server','spring-petclinic-customers-service','spring-petclinic-vets-service','spring-petclinic-visits-service'"
    }
    
    stages {
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
                    
                    def CHANGED_SERVICES = changedServicesList.join(",")
                    
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
                    if (CHANGED_SERVICES == env.SERVICES) {
                        sh 'mvn verify'
                    } else {
                        CHANGED_SERVICES.split(",").each { service ->
                            dir(service) {
                                echo "Testing ${service}"
                                sh 'mvn verify'
                            }
                        }
                    }
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
                    script {
                        if (currentBuild.result == 'UNSTABLE') {
                            error "Build failed: Line coverage is below the required minimum ${env.MINIMUM_COVERAGE}%"
                        }
                    }

                    sh 'mvn clean'
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

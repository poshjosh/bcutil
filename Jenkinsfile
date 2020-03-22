#!/usr/bin/env groovy
/**
 * https://github.com/poshjosh/bcutil
 * At a minimum, provide the MAIN_CLASS and where applicable SONAR_PORT and
 */
pipeline {

    agent any

    /**
     * parameters directive provides a list of parameters which a user should provide when triggering the Pipeline
     * some of the valid parameter types are booleanParam, choice, file, text, password, run, or string
     */
    parameters {
        string(name: 'ORG_NAME', defaultValue: 'poshjosh',
                description: 'Name of the organization. (Docker Hub/GitHub)')
        string(name: 'APP_BASE_URL', defaultValue: 'http://localhost',
                description: 'Server protocol://host, without the port')
        string(name: 'APP_PORT', defaultValue: '', description: 'App server port')
        string(name: 'APP_CONTEXT', defaultValue: '/',
                description: 'App server context path. Must begin with a forward slash / ')
// @bug @issue #001
// Only one java option supported. (e.g '-XX:+TieredCompilation')
// When more than one specified, encountered error: unknown shorthand flag: 'X' in -XX:TieredStopAtLevel=1
// --build-arg 'JAVA_OPTS=-XX:+TieredCompilation' '-XX:TieredStopAtLevel=1' -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap
// From the above the multiplie java opts where separated by jenkins
// I tried using single, double and then tripple quotes, but the error still occurred
//        string(name: 'JAVA_OPTS',
//                defaultValue: '-XX:TieredStopAtLevel=1 -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap',
//                description: 'Java environment variables')
        string(name: 'JAVA_OPTS',
                defaultValue: '-XX:TieredStopAtLevel=1',
                description: 'Java environment variables')
        string(name: 'CMD_LINE_ARGS', defaultValue: '',
                description: 'Command line arguments')
        string(name: 'MAIN_CLASS', defaultValue: 'com.bc.util.Main',
                description: 'Java main class')
        string(name: 'SONAR_BASE_URL', defaultValue: 'http://localhost',
                description: 'Sonarqube base URL. Will be combined with port to build Sonarqube property sonar.host.url')
        string(name: 'SONAR_PORT', defaultValue: '9000',
                description: 'Port for Sonarqube server')
        string(name: 'TIMEOUT', defaultValue: '45',
                description: 'Max time that could be spent in MINUTES')
        choice(name: 'DEBUG', choices: ['Y', 'N'], description: 'Debug?')
    }
    environment {
        ARTIFACTID = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        APP_ID = "${ARTIFACTID}:${VERSION}"
        IMAGE_REF = "${ORG_NAME}/${APP_ID}"
        IMAGE_NAME = IMAGE_REF.toLowerCase()
        MAVEN_CONTAINER_NAME = "${ARTIFACTID}-container"
        MAVEN_WORKSPACE = ''
        APP_HAS_SERVER = "${params.APP_PORT != null && params.APP_PORT != ''}"
        SERVER_URL = "${APP_HAS_SERVER == true ? params.APP_BASE_URL + ':' + params.APP_PORT + params.APP_CONTEXT : null}"
        ADDITIONAL_MAVEN_ARGS = "${params.DEBUG == 'Y' ? '-X' : ''}"
        VOLUME_BINDINGS = '-v /home/.m2:/root/.m2'
    }
    options {
        timestamps()
        timeout(time: "${params.TIMEOUT}", unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '4'))
        skipStagesAfterUnstable()
        disableConcurrentBuilds()
    }
    triggers {
// @TODO use webhooks from GitHub
// Once in every 2 hours slot between 0900 and 1600 every Monday - Friday
        pollSCM('H H(8-16)/2 * * 1-5')
    }
    stages {
        stage('Maven') {
            agent {
                docker {
                    image 'maven:3-alpine'
                    args "--name ${MAVEN_CONTAINER_NAME} -u root ${VOLUME_BINDINGS}"
                }
            }
            stages {
                stage('Clean & Build') {
                    steps {
                        echo '- - - - - - - CLEAN & BUILD - - - - - - -'
                        script {
                            MAVEN_WORKSPACE = WORKSPACE
                            if(DEBUG == 'Y') {
                                echo '- - - - - - - Printing Environment - - - - - - -'
                                sh 'printenv'
                                echo '- - - - - - - Done Printing Environment - - - - - - -'
                            }
                        }
                        sh 'mvn -B ${ADDITIONAL_MAVEN_ARGS} clean:clean resources:resources compiler:compile'
                    }
                }
                stage('Unit Tests') {
                    steps {
                        echo '- - - - - - - UNIT TESTS - - - - - - -'
                        sh 'mvn -B ${ADDITIONAL_MAVEN_ARGS} resources:testResources compiler:testCompile surefire:test'
                        jacoco execPattern: 'target/jacoco.exec'
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                            sh "exit 1"
                        }
                    }
                    post {
                        always {
                            junit(
                                allowEmptyResults: true,
                                testResults: 'target/surefire-reports/*.xml'
                            )
                        }
                    }
                }
                stage('Package') {
                    steps {
                        echo '- - - - - - - PACKAGE - - - - - - -'
                        sh 'mvn -B ${ADDITIONAL_MAVEN_ARGS} jar:jar'
                    }
                    post {
                        always {
                            archiveArtifacts artifacts: 'target/*.jar', onlyIfSuccessful: true
                        }
                    }
                }
                stage('Quality Assurance') {
                    parallel {
                        stage('Integration Tests') {
                            steps {
                                echo '- - - - - - - INTEGRATION TESTS - - - - - - -'
                                sh 'mvn -B ${ADDITIONAL_MAVEN_ARGS} failsafe:integration-test failsafe:verify'
                                jacoco execPattern: 'target/jacoco-it.exec'
                                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                                    sh "exit 1"
                                }
                            }
                            post {
                                always {
                                    junit(
                                        allowEmptyResults: true,
                                        testResults: 'target/failsafe-reports/*.xml'
                                    )
                                }
                            }
                        }
                        stage('Sanity Check') {
                            steps {
                                echo '- - - - - - - SANITY CHECK - - - - - - -'
                                sh 'mvn -B ${ADDITIONAL_MAVEN_ARGS} checkstyle:checkstyle pmd:pmd pmd:cpd com.github.spotbugs:spotbugs-maven-plugin:spotbugs'
                                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                                    sh "exit 1"
                                }
                            }
                        }
                        stage('Sonar Scan') {
                            when {
                                expression {
                                    params.SONAR_PORT != null && params.SONAR_PORT != ''
                                }
                            }
                            environment {
                                SONAR = credentials('sonar-creds') // Must have been specified in Jenkins
                                SONAR_URL = "${params.SONAR_BASE_URL}:${params.SONAR_PORT}"
                            }
                            steps {
                                echo '- - - - - - - SONAR SCAN - - - - - - -'
                                sh "mvn -B ${ADDITIONAL_MAVEN_ARGS} sonar:sonar -Dsonar.login=$SONAR_USR -Dsonar.password=$SONAR_PSW -Dsonar.host.url=${SONAR_URL}"
                                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                                    sh "exit 1"
                                }
                            }
                        }
                        stage('Documentation') {
                            steps {
                                echo '- - - - - - - DOCUMENTATION - - - - - - -'
                                sh 'mvn -B ${ADDITIONAL_MAVEN_ARGS} site:site'
                                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                                    sh "exit 1"
                                }
                            }
                            post {
                                always {
                                    publishHTML(target: [reportName: 'Site', reportDir: 'target/site', reportFiles: 'index.html', keepAll: false])
                                }
                            }
                        }
                    }
                }
                stage('Install Local') {
                    steps {
                        echo '- - - - - - - INSTALL LOCAL - - - - - - -'
                        sh 'mvn -B ${ADDITIONAL_MAVEN_ARGS} source:jar install:install'
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                            sh "exit 1"
                        }
                    }
                }
            }
        }
        stage('Docker') {
            when {
                expression {
                    params.MAIN_CLASS != null && params.MAIN_CLASS != ''
                }
            }
            stages{
                stage('Build Image') {
                    steps {
                        echo '- - - - - - - BUILD IMAGE - - - - - - -'
                        script {

                            if(DEBUG == 'Y') {
                                echo '- - - - - - - Printing Environment - - - - - - -'
                                sh 'printenv'
                                echo '- - - - - - - Done Printing Environment - - - - - - -'
                            }

                            // a dir target should exist if we have packaged our app e.g via mvn package or mvn jar:jar'
                            sh "cp -r ${MAVEN_WORKSPACE}/target target"
                            sh "cd target && mkdir dependency && cd dependency && find ${WORKSPACE}/target -type f -name '*.jar' -exec jar -xf {} ';'"
                            def customArgs = "--build-arg APP_PORT=${params.APP_PORT} --build-arg MAIN_CLASS=${params.MAIN_CLASS} --build-arg JAVA_OPTS=${params.JAVA_OPTS}"
                            def additionalBuildArgs = "--pull ${customArgs}"
                            docker.build("${IMAGE_NAME}", "${additionalBuildArgs} .")
                        }
                    }
                }
                stage('Run Image') {
                    steps {
                        echo '- - - - - - - RUN IMAGE - - - - - - -'
                        script{

                            def RUN_ARGS = VOLUME_BINDINGS
                            if(params.APP_PORT != null && params.APP_PORT != '') {
                                RUN_ARGS = "${RUN_ARGS} -p ${params.APP_PORT}:${params.APP_PORT}"
                            }
                            if(params.JAVA_OPTS != null && params.JAVA_OPTS != '') {
                                RUN_ARGS = "${RUN_ARGS} -e JAVA_OPTS=${JAVA_OPTS}"
                            }

                            // Add server port to command line args
                            def CMD_LINE
                            if(APP_HAS_SERVER == true) {
                                CMD_LINE = params.CMD_LINE_ARGS + ' --server-port=' + params.APP_PORT
                            }else{
                                CMD_LINE = params.CMD_LINE_ARGS
                            }

                            docker.image("${IMAGE_NAME}")
                                .withRun("${RUN_ARGS}", "${CMD_LINE}") {
                                    // SERVER_URL is an environment variable not a pipeline parameter
                                    if(APP_HAS_SERVER == true) {
                                        sh "curl --retry 5 --retry-connrefused --connect-timeout 5 --max-time 5 ${SERVER_URL}"
                                    }else {
                                        echo "No Server URL"
                                    }
                            }
                        }
                    }
                }
                stage('Deploy Image') {
                    when {
                        beforeAgent true
                        branch '*/master'
                    }
                    steps {
                        echo '- - - - - - - DEPLOY IMAGE - - - - - - -'
                        script {
                            // Must have been specified in Jenkins
                            docker.withRegistry('', 'dockerhub-creds') {
                                sh "docker push ${IMAGE_NAME}"
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            script{
                retry(3) {
                    try {
                        timeout(time: 60, unit: 'SECONDS') {
                            deleteDir() // Clean up workspace
                        }
                    } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
                        // we re-throw as a different error, that would not
                        // cause retry() to fail (workaround for issue JENKINS-51454)
                        error 'Timeout!'
                    }
                } // retry ends
            }
            sh "docker system prune -f --volumes"
        }
        failure {
            mail(
                to: 'posh.bc@gmail.com',
                subject: "$IMAGE_NAME - Build # $BUILD_NUMBER - FAILED!",
                body: "$IMAGE_NAME - Build # $BUILD_NUMBER - FAILED:\n\nCheck console output at ${env.BUILD_URL} to view the results."
            )
        }
    }
}

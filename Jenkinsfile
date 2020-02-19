/**
 * github.com/poshjosh/bcutil
 * https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/
 * docker:build is a fabric8 command for building docker image
 */
pipeline {
    agent { 
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2' 
        }
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage ('Initialize') {
            steps {
                bat '''
                    echo "M2_HOME = %M2_HOME%"
                    echo "JAVA_HOME = %JAVA_HOME%"
                '''
            }
        }
        stage('Build') {
            steps {
                bat 'mvn -B -DskipTests clean package' 
            }
        }
        stage('Test') { 
            steps {
                bat 'mvn test' 
            }
            post {
                always {
                    archive '**/target/**/*.jar'
                    junit '**/target/**/*.xml'
                }
            }
        }
        stage ('Build docker image') {
            agent {
                docker {
                    image 'maven:3-alpine'
                    args '-v /root/.m2:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock' 
                }
            }
            steps {
                bat 'mvn -Pfabric8 -Ddocker.skip=false -Ddocker.host=unix:///var/run/docker.sock verify' 
            }
        }
    }
}

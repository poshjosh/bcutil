/**
 * github.com/poshjosh/bcutil
 * https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/
 * docker:build is a fabric8 command for building docker image
 */
pipeline {
    agent { 
        dockerfile {
            filename 'Dockerfile'
            args '-v /root/.m2:/root/.m2' 
        }
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
        stage('Test') { 
            steps {
                sh 'mvn test' 
            }
            post {
                always {
                    archive '**/target/**/*.jar'
                    junit '**/target/**/*.xml'
                }
            }
        }
        stage ('Build docker image') {
            steps {
                sh 'mvn -Pfabric8 verify' 
            }
        }
        stage('Install') {
            steps {
                sh 'mvn jar:jar install:install help:evaluate -Dexpression=project.name'
            }
        }
    }
}

/**
 * github.com/poshjosh/bcutil
 * https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/
 */
pipeline {
    agent { 
        dockerfile {
            filename 'Dockerfile'
            args '--name looseboxes-bcutil -v /root/.m2:/root/.m2' 
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
        stage('Build Docker Image') {
            steps {
                sh 'mvn docker:build' 
            }
        }
        stage('Install') {
            steps {
                sh 'mvn jar:jar install:install help:evaluate -Dexpression=project.name'
            }
        }
        stage('Deploy') {
            steps {
                sh 'echo IN THE FUTURE, THIS WILL PUSH THE BUILT IMAGE TO DOCKER HUB' 
            }
        }
    }
}

/**
 * github.com/poshjosh/bcutil
 * https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/
 */
pipeline {
    agent { 
        dockerfile {
            filename 'Dockerfile'
            args '--name -u 0 looseboxes-bcutil -v /root/.m2:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock' 
        }
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -X -e -B -DskipTests clean package' 
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
                sh '''
                    "chmod 666 /var/run/docker.sock"
                    "mvn -Ddocker.certPath=/certs/client -Ddocker.host=unix:///var/run/docker.sock -Pfabric8 docker:build"
                sh '''
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

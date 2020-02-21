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
        stage('Build and Verify') {
            steps {
                sh 'mvn -Ddocker.skip=false -Ddocker.certPath=/certs/client -Ddocker.host=unix:///var/run/docker.sock -Pfabric8 docker:build' 
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

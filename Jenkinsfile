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
                sh 'mvn -Ddocker.skip=false -Ddocker.host=unix:///var/run/docker.sock -Pfabric8 verify' 
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
        stage('Smoke Test') {
            steps {
                sh 'echo IN THE FUTURE, THIS RUN THE IMAGE AND CARRY OUT SMOKE TEST' 
            }
        }
    }
}

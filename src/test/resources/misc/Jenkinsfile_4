#!/usr/bin/env groovy
/**
 * https://github.com/poshjosh/parent
 * @see https://hub.docker.com/_/maven
 *
 * Only double quoted strings support the dollar-sign ($) based string interpolation.
 *
 * Do not use --rm in args as the container will be removed by Jenkins after being 
 * run, and jenkins will complain about not being able to remove the container if
 * already removed due to --rm option in args.
 */
pipeline {
    environment {
        IMAGE_NAME = 'poshjosh/parent:latest'
        dockerImage = ''
    }
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Clone Git') {
            steps {
                git '/home/Documents/NetBeansProjects/parent'
            }
        }
        stage('Clean') {
            steps {
                sh 'mvn -B clean'
            }
        }
        stage('Build Image') {
            steps{
                script {
                   dockerImage = sh(script: 'docker build . -t ${IMAGE_NAME} -f Dockerfile', returnStdout: true)
                }
            }
        }
        stage('Install') {
            steps {
                sh '''
                    "mvn install:install help:evaluate -Dexpression=project.name"
                '''    
            }
        }
        stage('Deploy Image') {
            environment {
                DOCKERHUB_CREDS = credentials('dockerhub')
            }
            steps{
                script {
                    sh '''
                        "docker login --username=${DOCKERHUB_CREDS_USR} --password=${DOCKERHUB_CREDS_PSW}"
                        "docker push ${IMAGE_NAME}"
                    '''    
                }
            }
        }
        stage('Clean Up') {
            steps {
                sh '''
                    "if rm -rf target; then echo 'target dir removed'; else echo 'failed to remove target dir'; fi"
                    "docker rmi ${IMAGE_NAME}"    
                '''
            } 
        }
    }
}

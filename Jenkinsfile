#!/usr/bin/env groovy
/**
 * https://github.com/poshjosh/bcutil
 */
library(
    identifier: 'jenkins-shared-library@master',
    retriever: modernSCM(
        [
            $class: 'GitSCMSource',
            remote: 'https://github.com/poshjosh/jenkins-shared-library.git'
        ]
    )
)

defaultPipeline(gitUrl : 'https://github.com/poshjosh/bcutil.git')

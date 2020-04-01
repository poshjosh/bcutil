#!/usr/bin/env groovy
/**
 * https://github.com/poshjosh/bcutil
 */
library(
    identifier: 'jenkins-shared-library',
    retriever: modernSCM(
        [
            $class: 'GitSCMSource',
            remote: 'https://github.com/poshjosh/jenkins-shared-library.git'
        ]
    )
)

defaultPipeline()

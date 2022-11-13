pipeline {

  agent none

  environment {
    DOCKER_IMAGE = "duynh/test-job"
  }

  stages {
    stage("build") {
      agent { node {label 'master'}}
      environment {
        DOCKER_TAG="${GIT_BRANCH.tokenize('/').pop()}-${GIT_COMMIT.substring(0,7)}"
      }
      steps {
        bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} . "
        bat "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
//         bat "docker image ls | grep ${DOCKER_IMAGE}"
        withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
            bat "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
            bat "docker push ${DOCKER_IMAGE}:latest"
        }

        //clean to save disk
        bat "docker image rm ${DOCKER_IMAGE}:${DOCKER_TAG}"
        bat "docker image rm ${DOCKER_IMAGE}:latest"
      }
    }
    stage("ssh-server") {
      agent any
      steps {
        sshagent(['ssh_remote']) {
            bat 'ssh -o StrictHostKeyChecking=no -l Admin 127.0.0.1 docker ps'
        }
      }
    }
  }

  post {
    success {
      echo "SUCCESSFUL"
    }
    failure {
      echo "FAILED"
    }
  }
}
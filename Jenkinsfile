node('docker') 
{
    stage "Container Prep"
        echo("The node is up")
        def mycontainer = docker.image('elastest/ci-docker-siblings:latest')
        mycontainer.pull()
        mycontainer.inside("-u jenkins -v /var/run/docker.sock:/var/run/docker.sock:rw") 
	{
            git 'https://github.com/elastest/elastest-cost-engine.git'

            stage "Tests"
                echo ("Starting tests")
                sh 'cd ece; mvn clean test'
                //step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])

            stage "Package"
                echo ("Packaging")
                sh 'cd ece; mvn package -DskipTests'

            stage "Archive artifacts"
                archiveArtifacts artifacts: 'ece/target/*.jar'

            stage "Build image - Package"
                echo ("Building ECE Image")
                def myimage = docker.build 'elastest/ece:latest'

            stage "Run image"
                myimage.run()

            stage "Publish"
                echo ("Publishing")

                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'elastestci-dockerhub',
                    usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) 

		{
                    sh 'docker login -u "$USERNAME" -p "$PASSWORD"'
                    myimage.push()
                }
        }
}

`java -jar git-runner.jar -c 4 -dir "../sample-program"  -exec "mvn dependency:analyze-duplicate" -ne "javax.el:javax.el-api:jar"`


-c - number of commits checked from current. Default = 100
-dir - working directory
-e - text exist
-ne - not exist
-exec - command to execute 

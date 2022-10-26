`java -jar git-runner.jar -c 4 -dir "../sample-program"  -exec mvn dependency:analyze-duplicate -f javax.el:javax.el-api:jar`


-c - number of commits checked from current. Default = 100  
-dir - working directory  
-exec - command to execute  
-f - find text in command result  

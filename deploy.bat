@echo off
set APPLICATION_NAME=learntechpuzz
set STAGE_NAME=dev
set S3_BUCKET=learntechpuzz-%STAGE_NAME%
set INPUT_FILE=template.yaml
set OUTPUT_FILE=template-output.yaml
set STACK_NAME=%APPLICATION_NAME%-%STAGE_NAME%

:: Build Lambda Functions using maven

cd lambda-functions
call mvn clean package
cd ..

:: create s3 bucket
call aws s3 mb s3://%S3_BUCKET%

:: copy swagger.yaml
call aws s3 cp swagger.yaml s3://%S3_BUCKET%/swagger.yaml

call aws cloudformation package --template-file %INPUT_FILE% --output-template-file %OUTPUT_FILE% --s3-bucket %S3_BUCKET%
call aws cloudformation deploy --template-file %OUTPUT_FILE% --stack-name %STACK_NAME% --parameter-overrides ApplicationName=%APPLICATION_NAME% StageName=%STAGE_NAME% S3BucketName=%S3_BUCKET% --capabilities CAPABILITY_IAM


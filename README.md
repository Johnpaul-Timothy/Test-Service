title: 'Test Service'
description: 'This demonstrates a simple java application that groups anagrams and provides words suggestion based on input. Built using AWS services like Lambda, API Gateway, S3 and Cloudfront'
layout: Doc
framework: v1
platform: AWS
language: Java
authorName: 'Johnpaul Mani'

# Test Service

This demonstrates a simple java application that groups anagrams and provides words suggestion based on input.

## Use Cases

- Group Anagrams
- Suggestions for every character input

## Service Description

### Group Anagrams

The service groups the anagram. ( Two words are anagram, if they have same 
characters but in different order e.g. ABC, BAC, CAB, CBA, BCA are anagrams)
E.g. Words “CAT, “DOG”, “TAC”, ”MAD”, “DAM”, “AMD”, “GOD”, “SET”
         
Service response will be : [CAT, TAC],  [DOG, GOD], [MAD, DAM, AMD], [SET]

### Word Suggestions

Thr service provide name suggestions for every character input.
Ex: J for John, Ja for Jane.

## Build

It is required to build prior to deploying. You can build the deployment artifact using Maven.

### Maven

In order to build using Maven simply run

```bash
mvn clean install
```

## Deploy

After having built the deployment artifact using  Maven as described above you can deploy by

- Creating an AWS Lambda function configured with Java Runtime. 
- Update the handler detail with 'com.test.service.TestService::handleRequest'
- Upload the deployment artifact.

## Other AWS Services Setup

### AWS API Gateway

Create an AWS API Gateway by downloading and importing this OpenAPI specification,

https://temp-dld-store.s3.amazonaws.com/test-service-api-dev-oas30-apigateway.yaml


### AWS S3

Create ans S3 bucket with default configurations. Edit the bucket settings to set 
it as a static website store and upload the test-service-ui/index.html file from the repo.

### AWS Cloudfront

Create a cloudfront distribution with default configurations and add the aws s3 bucket as the origin.

## Usage

### REST API

-Group Anagrams

End Point : https://r9po3h1tcg.execute-api.us-east-1.amazonaws.com/dev/anagrams
HTTP Method: POST

Request:
```bash
cat,dog,act
```
Respone:
```bash
{
    "source": "Test Service",
    "output": "[[cat,act], [dog]]",
    "contacts": null
}
```

-Auto Suggestion

End Point : https://r9po3h1tcg.execute-api.us-east-1.amazonaws.com/dev/suggest?cquery=joh
HTTP Method: GET

Respone:
```bash
{
    "source": "Test Service",
    "output": null,
    "contacts": {
        "joh": [
            "john"
        ]
    }
}
```

## Scaling

By default, AWS Lambda limits the total concurrent executions across all functions within a given region to 100. The default limit is a safety limit that protects you from costs due to potential runaway or recursive functions during initial development and testing. To increase this limit above the default, follow the steps in [To request a limit increase for concurrent executions](http://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html#increase-concurrent-executions-limit).

# Installation on AWS

## Preparation
* Create AWS User with `Programmatic access` and `Administrator Access` for Terraform
* Allocate an Elastic IP `EC2 -> ElasticIPs -> Allocate`
* Export ElasticIP for Frontend by:
 * Create an `.env` file in the `frontend/src` directory
 ```
 API_URL=http://<ElasticIP>:<Port>
 ```

 * Export the ElasticIP to the environment:
 ```
 export API_URL=http://<ElasticIP>:<Port>
 ```

## Build Application
* Execute `mvn clean install` in project base directory
* Execute `mvn clean install -P deployment-aws` for building AWS Deployment

## Configure Terraform
* Change to directory `deployment/target/<deployment_name>/backend/terraform`
* Download and Install Terraform (https://www.terraform.io/downloads.html)
* Configure all `*.tfvars` files

## Deploy the application
* Load Terraform Modules by executing `terraform init`
* Execute Terraform Plan
```
terraform plan --var-file= .... --var-file=....
```
* Create AWS Infrastructure
```
terraform apply --var-file= ... --var-file= ....
```

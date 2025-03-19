# Breakable-Toy-I-Inventory-manager
# Description

This project is part of the spark program assignments, the objective is to develop a functional store manager, with CRUD functionalities,
ths project will be developed with the next technologies:

**Back-End**
- [Springboot]([https://react.dev/](https://spring.io/projects/spring-boot)) *3.4.3*
- [Java](https://www.oracle.com/mx/java/technologies/downloads/) *21*
- Maven

# Installation

To the instalation of this project is necessary to have node instaled in a stable version, at the moment of the creation of this document the version used for this project is: ```v21```

You can download and install java **[here](https://www.oracle.com/mx/java/technologies/downloads/)**.

> It's important to inform that the project is setted to work on PORT:8080, if for any reason this port is busy or you whish to change it you can find and modify this in the archive named **application.properties** in the project.

Once you have node installed you'll need to go your terminal of preference, locate in the desired directory to download the project,clone the repository and then execute the next commands:
```
git clone ${url of the repository}
//in case you haven't clone the project repository
```

Next to run the project is necessary to run the next command line:

```
mvn spring-boot:run
```
___

If you wish to look a deeper and easier insight of the functionality you can run the project and then go to:
```

http://localhost:9090/swagger-ui/index.html#/
//This is the swagger documentation url

```
___

# Inventory manager
## Version: v1.0

### /api/v1/product/update

#### PUT
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | query |  | Yes | string (uuid) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/product/products/{id}/instock

#### PUT
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string (uuid) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/product/products/{id}/outofstock

#### POST
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string (uuid) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/product/addProduct

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/product

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| page | query |  | No | integer |
| size | query |  | No | integer |
| name | query |  | No | string |
| category | query |  | No | [ string ] |
| stock | query |  | No | integer |
| sort | query |  | No | [ string ] |
| order | query |  | No | [ boolean ] |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

#### DELETE
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | query |  | Yes | string (uuid) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/product/metrics

#### GET
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

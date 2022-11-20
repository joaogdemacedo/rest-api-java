# REST API in Java - João De Macedo
#### A simple REST API of a vending machine.

### What is this?

This is a `REST API`, made in `Java` using `Spring Boot` framework, that stores and retrieves information about a vending machine. `H2 Database` is used for storage of the data and both the request body and response data are in `JSON format`.
This API has `CRUD` implemented and other functionalities such as `search criteria`, `Basic authentication` and `integration tests`.

### Structure `Backend1/`

- `Controller`: Handle incoming requests and sends the response data back to the client.
- `DataProvider`: Objects that correspond to the tables in the database.
- `Repository`: Like a wrapper for the database. Read and write data to the database.
- `UseCases`: Layer capable of managing data from Controllers and comunicating to Repositories.
- `Tests`: Integration Tests to validate the API codebase.
- `PostmanCollection.json`: API collection.

<h3>Where to find me</h3>
<p><a href="https://github.com/joaogdemacedo" target="_blank"><img alt="Github" src="https://img.shields.io/badge/GitHub-%2312100E.svg?&style=for-the-badge&logo=Github&logoColor=white" /></a> <a href="https://twitter.com/joaodemacedo134" target="_blank"><img alt="Twitter" src="https://img.shields.io/badge/twitter-%231DA1F2.svg?&style=for-the-badge&logo=twitter&logoColor=white" /></a> <a href="https://www.linkedin.com/in/joaodemacedo134" target="_blank"><img alt="LinkedIn" src="https://img.shields.io/badge/linkedin-%230077B5.svg?&style=for-the-badge&logo=linkedin&logoColor=white" /></a></p>

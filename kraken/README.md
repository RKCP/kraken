# Kraken Test

Small program testing three kraken endpoints

## Installation

To run this program, you'll need to install the following dependencies:

- spring-boot-starter-web
- spring-boot-starter-test
- slf4j-api

As well as the following plugins:

- spring-boot-maven-plugin


You can install these dependencies by running:

```
$ mvn clean install
```

## Usage

To run the program, use the following command:

```
$ mvn spring-boot:run
```

Or load the pom.xml file into your IDE and run the following class:

```
KrakenApplication
```

## Tests

To run the tests, use the following command:

```
$ ./mvnw test
```

## API Calls

After running the program, please download and import the Postman collection called `krakenCollectionRaphael.postman_collection.json` in the root
folder of this project. This will give you access to two GET requests, and a POST request. The required API is already inside.

If you prefer to use curl or hit the APIs from your browser, below are the requests:
- `http://localhost:8080/krakenapi/outages`
- `http://localhost:8080/krakenapi/site-info/norwich-pear-tree`
- `http://localhost:8080/krakenapi/site-outages/norwich-pear-tree` (you can send empty braces {} in this request. The code will handle the body)

If the calls are successful, the GET requests should both return data in your Postman console. Your IDE console will also log the response.
The POST request will return a 200 OK, seen as a status code in Postman, and logged in your IDE console.



## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
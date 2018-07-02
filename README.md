# wikitolearn-course-midtier

## Synopsis
WikiToLearn Course Midtier is a mid-tier service of the WikiToLearn architecture.
Its aim is to serve coarse-grained API and hide complex business logic operations which involve multiple microservices.


## Development
We use Docker to speed-up development and setup the environment without any dependency issues.

### Minimum requirements
- Docker Engine 17.09.0+

### How to run
It is advisable to run using the `docker-compose.yml` file provided.
First, you have replace your Docker Host IP into the `.env` file.
Second, you need to run the following services on which WikiToLearn Course Midtier strongly depends on:
- Keycloak

### API documentation
The API documentation is generated thanks to [Springfox](https://springfox.github.io/springfox/).
A Swagger2 compliant JSON and Swagger UI is available at `/api-docs` and at `/swagger-ui.html` respectively.

> N.B.: Do not use the "Try it out" Swagger UI option, it will be removed as soon as possible.

## Versioning
We use [SemVer](http://semver.org/) for versioning.

## License
This project is licensed under the AGPLv3+. See the [LICENSE.md](LICENSE.md) file for details.

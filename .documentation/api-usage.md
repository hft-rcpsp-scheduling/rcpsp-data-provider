# API Usage

## 1. Scheduling Usage

This shows the main use-case of the api:

```
API                                   Scheduling Client
 | <----- request data for one project ----- | 
 | ------------ send project --------------> |   
 |                                           | execute scheudling algorithm
 |                                           | fill the start-days in the project
 | <- request evaluation for the solution -- |
 | ------------ send feedback -------------> |                          
 |                                           | check feasibility from the feedback
```

> Open the Swagger-UI to get specific information about the REST-calls and their related data.

## 2. Visualization Usage

```
API                                    Scheduling Client
 |                                           | execute scheudling algorithm
 | <-------- request visualization --------- |
 | ------- send visualization file --------> |
```

## 3. Potential UI Usage

```
                   UI                         API              Scheduling Client
                    |                          |                       | execute scheudling algorithm
                    |                          | <-- save solution --- |  
                    | -- request solutions --> |                       | 
                    | <--- send solutions ---- |                       |
visualize solutions |                          |                       |
```

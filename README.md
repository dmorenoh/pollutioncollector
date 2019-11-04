

## Overview

Implemented solution was faced by using concepts/tools as: hexagonal, message driven (eventbus), webflux

## Use cases
Use cases are depicted as commands/queries:

**1. Start API (Command)**
-- Method: *POST*
-- Endpoint ```http://localhost:8082/myapp/start```
-- Request body
```
{
    "robotName":"[robot name]",
    "stopInterval":[double value as kms],
    "speed":[double value expressed as meters per seconds],
    "polylineEncoded":"[polyline encoded]"
}
```
Example:
```
curl --request POST \
  --url http://localhost:8082/myapp/start \
  --header 'content-type: application/json' \
  --data '{
    "robotName":"test",
    "stopInterval":0.1,
    "speed":3.00,
    "polylineEncoded":"ivo~Fhe}uO`MoAlKi@"
}'
```
**3. Stop (Command)**
-- Method: *POST*
-- Endpoint ```http://localhost:8082/myapp/stop```
-- Request body: N/A
Example:
```
curl -X POST http://localhost:8082/myapp/stop
```
**4. Reroute (Command)**
-- Method: *POST*
-- Endpoint ```http://localhost:8082/myapp/reroute```
-- Request body
```
{
    "polylineEncoded":"[polyline encoded]"
}
```
Example:
```
curl --request POST \
  --url http://localhost:8082/myapp/reroute \
  --header 'content-type: application/json' \
  --data '{
    "polylineEncoded":"_po~Fpb}uOvHa@xHs@"
}'
```
**5. Get report (Query)**
-- Method: *GET*
-- Endpoint ```http://localhost:8082/myapp/reports``` 
-- Request body: N/A

Example (execute as streams):
```
curl -S http://localhost:8082/myapp/reports
```

## Considerations
Time interval considered in order to display reports can be set in `application.properties` file 
```
report.interval.seconds=10
```
As you can see, It's currently set as 10 seconds. This means new report result is added each 10 seconds.

Distance calculation formulas taken from: `https://www.movable-type.co.uk/scripts/latlong.html`
 
## Technical debts

Full test coverage over existing endpoints, just few of them were tested using RestAssured/Spock

 


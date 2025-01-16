# Load-Balancer
A versatile load balancer designed to support multiple dynamic routing strategies for optimized traffic management.

## Route API
#### Sample CURL
```CURL
curl --location 'http://localhost:8081/load-balancer/route/' \
--header 'Content-Type: application/json' \
--data '{
    "discriminator" : "service1",
    "endPointPath": "/index",
    "payload": null,
    "resourceType": "HTML",
    "httpMethod": "GET"
}'
```
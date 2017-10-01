### Performance-test service
---

The module serves users with multiple statistics of a remote api's performance.

#### Deployment Guide
* Build and get the jar
```sh
cd {project_root_dir} && gradle clean build -x test
```
* Run jar from libs directory
```sh
cd {project_root_dir}/build/libs
java -jar performance-test-0.0.1-SNAPSHOT.jar
```

#### User Manual
* Hit the Below URL.
```sh
Request URL: http://localhost:4545/metrics-service/metrics/get
Type: GET
```
* Sample Response - Response time in ms
```json
{
    "Mean": 347,
    "99th percentile": 1571,
    "10th percentile": 243,
    "Standard Deviation": 268.3360206904768,
    "90th percentile": 411,
    "95th percentile": 1065,
    "50th percentile": 253
}
```

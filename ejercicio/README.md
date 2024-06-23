
# Evaluación: JAVA

Se utilizo la arquitectura Multitier

### Instrucciones de ejecución

./gradlew clean build

./gradlew bootRun

### base de datos
http://localhost:8080/h2-console

### curls de ejemplo

curl --location 'localhost:8080/sign-up' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "aa",
"email": "bc7@bb.com",
"password": "a2asfGfdfdf4",
"phones": [
{
"number": 6,
"citycode": 66,
"contrycode": "AR"
},
{
"number": 7,
"citycode": 77,
"contrycode": "AR"
}
]
}
'

curl --location 'localhost:8080/login' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkNTIxZmQ0Ni1jMjAxLTRiODMtOTczYi1kMDBhYjBiYWQ3MWUiLCJzdWIiOiJiYzdAYmIuY29tIiwiaXNzIjoiQXJha2FraSBMZW9uYXJkbyIsImF1ZCI6ImF1ZGllbmNlIiwiaWF0IjoxNzE5MDk1OTQwLCJleHAiOjE3MTkwOTk1NDAsIk5hbWUiOiJhYSIsIkVtYWlsIjoiYmM3QGJiLmNvbSJ9.PYK45Li4UNcVj9UAcYUyuxCifNzWIiqWI-EtnZL0QRL9xjZK0CO4skPy-pEIf_s4bQ_8H_xLzh19GYt-TpVKAg' \
--data ''

### 
Autor: Leonardo Arakaki
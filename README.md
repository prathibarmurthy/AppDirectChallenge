# Prathiba's Appdirect Challenge Integration Project

Implemented Functionalities :
* Subscription Notification Functionalities such as 
	* create
	* change
	* cancel
	* status
* Subscription
	* assign
	* unassign
* Performing OAuth signature verification on the AppDirect issued API requests using Single Sign on

## Prerequisites
* Java 8
* Maven
* Heroku(For Deployment)
* Postgresql

## Deployment
$ mvn clean package

- Create tables in Heroku-Postgresql using the commands </br>
`CREATE TABLE subscription(id SERIAL PRIMARY KEY,company_name VARCHAR(255),edition VARCHAR(255),status VARCHAR(255),market_place_base_url VARCHAR(255));`</br>
`CREATE TABLE user_account(id SERIAL PRIMARY KEY,openid VARCHAR(255),firstname VARCHAR(255),lastname VARCHAR(255),email VARCHAR(255),subscription_id INT);`

- Setup heroku for easy deployment

- Running the application locally is as simple as
`java -jar target/appdirectchallenge-2.0.0-RELEASE.war`

- Deployed Application is running on
`https://myappdirectchallenge.herokuapp.com/`

## Integration URLS

###Login URL
https://myappdirectchallenge.herokuapp.com/login/openid?openid_identifier={openid}

###Logout URL
https://myappdirectchallenge.herokuapp.com/logout

###Subscription Create Notification URL
https://myappdirectchallenge.herokuapp.com/subscription/notification/create?url={eventUrl}

###Subscription Change Notification URL
https://myappdirectchallenge.herokuapp.com/subscription/notification/change?url={eventUrl}

###Subscription Cancel Notification URL
https://myappdirectchallenge.herokuapp.com/subscription/notification/cancel?url={eventUrl}

###Subscription Status Notification URL
https://myappdirectchallenge.herokuapp.com/subscription/notification/status?url={eventUrl}

###User Assignment Notification URL
https://myappdirectchallenge.herokuapp.com/user/assignment/notification/assign?url={eventUrl}

###User Unassignment Notification URL
https://myappdirectchallenge.herokuapp.com/user/assignment/notification/unassign?url={eventUrl}

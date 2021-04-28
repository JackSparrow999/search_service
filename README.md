# ykn

This application allows a subscribed/unsubscribed user to get emails of good articles at regular intervals. We have 3 microservices and 3 AWS sqs to execute this:

Architecture: https://github.com/JackSparrow999/search_service/blob/master/Architecture.JPG

User Service (https://github.com/JackSparrow999/user_service):

- Authentication
- workflow trigger
-> Written in **Spring Boot**

Search Service (https://github.com/JackSparrow999/search_service):

- Creation of search event out of API call (Search SQS)
- Processes search event using Google Search APIs
- Creation of mail event (Mail SQS)
- Creation of search event out of feedback event (Feedback SQS)
-> Written in **Spring Webflux**

Mail Service (https://github.com/JackSparrow999/mail_service):

- Processed mail event using AWS SES
- Creation of feedback event (Feedback SQS)
-> Written in **Spring Boot**

Note: Although CPU cycles are wasted here if the mail's time to send has not been crossed but during heavy loads this problem goes away beacause of the longer wait time for each queue event.

Note: Code will be refactored soon to implement proper design patterns. Also will make it more readable when I get some time! :)

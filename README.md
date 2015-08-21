# jacksonweekes-assignment2015

This project will be viewable live on [Heroku](https://evening-meadow-6191.herokuapp.com/), once I have implemented a persistent datastore. For the moment the landing page is visible however you can not register/login.

Student:        Jackson Cleary

Student Number: 220135030

All the requirements of the assignment have been met. The landing page purposefully does not include links to all the features directly as many of the pages require user authentication, however once logging in all the features are readily available. Searching, posting and session management functionality are all shown on the user page. I have conciously implemented the system so that only logged in users can see other users posts or profiles.

I have included some seed data, which is loaded up in onStart in app/Global.java. Comment out the relevent code in this file to stop this data loading on application start.

Pre-seeded users: bob@example.com, jack@example.com, jill@example.com, jan@example.com, all with password 'password'

I have included some tests, which can be run using 'sbt test'. This is by no means exhaustive however much of the important functionality is tested.

#TODO

*provide more complete test coverage, refactor existing tests

*code cleanup

*use play.mvc.Controller.Form objects for input forms to improve validation checks

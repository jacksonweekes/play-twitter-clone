# jacksonweekes-assignment2015

Student:        Jackson Cleary

Student Number: 220135030

My port on turing: 50755

Folder on turing: ~/comp391/jacksonweekes-assignment2015

Javadocs for this project can be viewed online at https://uneadvancedweb.github.io/jacksonweekes-assignment2015/

All the requirements of the assignment have been met, with MongoDB now providing the datastore and ReactJS is used to create a single page app version of the application, at {host}/spa. The SPA requires a user to be authenticated to access it, and a user can only post when on their own post list. Websockets are used to maintain a connection between server and client, with new posts sent to subscribing clients.

As an additional feature, the app is set up to find the location of a user by their ip address using the ip-api.com web service. However, this does not work on Turing as the request cannot go through the proxy, so this feature has been disabled. However if testing on a computer outside of Turing, uncomment the bottom line of the Session.java constructor and this feature should work properly.

Appointment Scheduler
Application that allows the user to edit,update, and add customers as well as appointments to a database.
Application switches from English to French based on user and alters time to user local time.
It also allows the user to generate reports on data.

@author Kaitlynn Abshire
kabshi2@wgu.edu
App Version 1
2/14/2021

Intellij IDEA Community 2020.3.2
JDK 11.0.10
JavaFX 11.0.2

Use configuration Main to run. Enter username and password on application login screen to enter.

There are 3 report options in the application. The first arranges appointments by type and month.
The second lists contacts and their appointment schedule.
The third lists the amount of appointments per year.

The application uses 2 lambda expressions.
One is used in MainScreen controller to change UTC times in database to local time in table views.
The second is in add appointments and modify to cause the date picker to be unable to select weekends.
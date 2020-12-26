# Mobile Application Development Semester Project
As part of a semester project in the lecture Mobile Application Development at Technische Hochschule Lübeck in 2018, every student had to build a small todo app with Android. Jump to the [project requirements](#requirements-to-pass-the-lecture), to get a feeling for what had to be done in the project. Please note that all aspects regarding the UI were left to the students.

## Getting Started

### Prerequisites
In order for the app to be tested, the following prerequisites must be created/installed beforehand:
* Android Studio
* An Android virtual device to start the apk
* The AVD must have at least one fictual contact
* Run the JAR sample-webapi-1.0-SNAPSHOT-war-exec.jar at the repository [org.dieschnittstelle.mobile.android.samples.dataaccess](https://github.com/dieschnittstelle/org.dieschnittstelle.mobile.android.samples.dataaccess). This Java application provides a simple REST API for organizing all todos. The API is needed to use the login UI.

### Installing
* Clone this Android project and open it in Android Studio.
* Build the project with Gradle and start the app with your AVD.

## Requirements to pass the lecture

### Data model
The data model for Todos should allow to represent the following information:
- [x] The name of the Todo
- [x] A description of the todo
- [x] Information about whether the todo was done or not
- [x] Information about whether it is a particularly important /'favourite' todo or not
- [x] The due date of the todo and a time
- [x] All information components, including the name, should be changeable after creating a todo

### Saving todos
Todos should be saved using both an external web application and a local data store. The Web application is provided as a Java EE Web Application.
- [x] Todos are to be stored in an SQLite database on the mobile device
- [x] If a write operation has been successfully executed on the local SQLite database, the relevant operation should be called on the web application. The IDs assigned by the SQLite database can be taken over by the web application
- [x] If access to the Web application is not possible when the application is started, a warning message should be issued. In this case, only the local database will be used until the end of application usage, and the cases where an initial connection terminates during app usage or an initially unavailable web application becomes available during usage need not be taken into account.
- [x] If the web application is available at the start of the Android application, the following "synchronization" should be implemented:
- [x] If there are local todos, all todos on the Web application pages are deleted and the local todos are transferred to the Web application.
- [x] If there are no local todos, all todos are transferred from the web application to the local database.

### Login
The registration should be done by entering an email and a password and by pressing a login button.
- [x] Only email addresses should be entered in the input field for email.
- [x] If no email address is entered, a permanently visible error message is displayed.
- [x] If the entry of the email field is changed, the error message disappears immediately when a character is entered/deleted.
- [x] Passwords should be numeric and exactly 6 digits long.
- [x] The input should be hidden.
- [x] It should only be possible to use the login button if values for email and password have been entered.
- [x] After pressing the login button, the entered values are to be transmitted to a server and checked there.
- [x] The check should be asynchronous.
- [x] As long as the check is running, a Progress dialog is to be displayed.
- [x] If the check fails, a permanently visible error message is output.
- [x] If one of the two fields is changed, the error message disappears immediately when a character is entered/deleted.
- [x] If the entered values are checked successfully, the todos should be displayed.
- [x] If there is no connection to the web application when starting the Android application, the todo list is displayed immediately. A local login is not required. (Note: points for this requirement will only be awarded if the registration using the web application has been implemented in principle.

### Todo list
The todo list should display an overview of all todos and enable the creation of new todos. It should display the following information for each todo:
- [x] the name
- [x] the due
- [x] the information if it is done
- [x] the information if it is a favorite
- [x] It should also allow the user to view the details of each Todo.
- [x] Changes to the todo list that can be made in the detailed view of a todo should be displayed in the overview on return.
- [x] It should be possible to modify the information on completion/non-completion or importance without requesting the detailed display.
- [x] Todos should always be sorted by Completed/Not Complete and then either by importance+date or by date+importance.
- [x] The user should be able to select the display by date+importance vs. importance+date via an options menu or action bar options.
- [x] Overdue todos - i.e. todos with an expired due date - should be highlighted visually.

### Detail view
The detail view should show all data represented by a todo. Sie soll außerdem die Änderung zumindest der folgenden
Daten eines Todo ermöglichen:
- [x] the name
- [x] the description
- [x] the due
- [x] the information if it is done
- [x] The UI controls provided by Android for this purpose are to be used for setting the date and time.
- [x] The deletion of a todo should also be possible via the detailed view and should be confirmed before execution.

### Link with contacts
Allow the association of a todo with a set of contacts at the data model level.
- [x] Allow the user to optionally link todos to a list of contacts at the level of the detail view.
- [x] The contacts are to be selected on the basis of a display of all available contacts.
- [x] View the linked contacts in the detail view for Todos.
- [x] Allow you to remove contacts from a todo's linked contacts list.
- [x] Also allow the user to be contacted by e-mail or SMS for each contact if an e-mail address or mobile phone number is available.
- [x] When contacting, the email address/mobile phone number, the title and the description of the todo of the Android app used in each case should be transmitted.

## Built with
* Android
* Gradle
* Retrofit
* SQLite
* Gson

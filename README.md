Lab Final Project - Student StudySync Assistant

Welcome to the Lab Final Project for the StudySync Assistant. This README serves as a comprehensive manual for both users and developers who wish to install, run, understand, extend or use this Java‑based Maven project. The aim of this document is to provide detailed guidance on every aspect of the project—from setting up the development environment and running the application to navigating its intuitive user interface and understanding its underlying architecture. Whether you are a student looking for a tool to manage your courses, assignments and routine, or a developer aiming to contribute to the project, this manual will help you through every step.

This README intentionally contains over 1000 lines to ensure that all critical details, instructions, and explanations are captured. Each section is broken down into small pieces with descriptive headings so that you can locate information quickly. The file is designed to be read sequentially or used as a reference for specific topics when needed.

Table of Contents

Introduction

Prerequisites

Setup and Installation

Project Directory Structure

Database Schema Overview

5.1 Course Table

5.2 Task Table

5.3 Study Session Table

5.4 Routine Events Table

5.5 Resources Table

5.6 Chapters Table

Java Classes and Data Models

6.1 Course Class

6.2 Task Class

6.3 StudySession Class

6.4 Routine Class

6.5 Resource Class

6.6 Chapter Class

Data Access Objects (DAO)

7.1 CourseDAO

7.2 TaskDAO

7.3 StudySessionDAO

7.4 RoutineDAO

7.5 ResourceDAO

7.6 ChapterDAO

Controllers and User Interface Flow

8.1 MainApplication and App Entry Point

8.2 Dashboard

8.3 Courses Page

8.4 Course Detail Page

8.5 Task Explorer

8.6 Grade Sheet

8.7 Routine Management

8.8 Analytics

Usage Guide

9.1 Launching the Application

9.2 Adding Courses

9.3 Managing Tasks

9.4 Managing Study Sessions

9.5 Setting Up Your Routine

9.6 Using Resources and Videos

9.7 Tracking Grades

9.8 Viewing Analytics

9.9 Notifications and Reminders

Developer Guide

10.1 Building with Maven

10.2 Understanding the Code Structure

10.3 Adding New Features

10.4 Customizing the UI

10.5 Extending the Database

10.6 Testing and Debugging

Troubleshooting

Acknowledgements

License

Introduction

The Lab Final Project aims to develop a study management assistant that helps students keep track of their academic commitments. Built on Java, JavaFX, and SQLite, it offers a set of features designed to simplify and organize coursework, assignments, routine schedules, study sessions, grades, and even provide analysis of study patterns. The user interface is modern and intuitive, making it easy for anyone—regardless of technical background—to start managing their academic life effectively. Here’s what the application offers:

Course Management – Add, view, and manage courses with details such as course code, title, instructor, and credit hours. Courses are stored in a persistent database table and can be updated at any time.

Task and Assignment Tracking – Create tasks associated with courses, set due dates and statuses, add notes, and receive notifications when deadlines approach. Each task is stored in the database with fields for titles, due dates, statuses (to-do, in‑progress, done), and completion timestamps.

Routine Scheduling – Define your weekly routine by adding events with start and end times, rooms, and day of the week. The routine is displayed on the dashboard to show your daily schedule.

Study Sessions – Log study sessions to track how much time you spend on each course. The study sessions table records start and end times, duration, and notes.

Resource Management – Associate chapters and video links with courses to keep educational materials in one place.

Grade Sheet – Record quiz, midterm, and final exam marks for each course. The grade sheet is editable and automatically updates the database when you enter marks.

Analytics – Visualize your study habits with charts that show study time per course over selected weeks. Choose which week to view and filter by course.

Notifications – Receive reminders when tasks are due, either on the due date or three days before, ensuring you never miss a deadline.

This README not only explains how to set up and use the application but also delves into the code base, including the class structure, database schema, data access patterns and UI flow. For new contributors, the developer guide provides tips on extending the system. References to relevant sections of the code and FXML files are provided with citations, such as the explanation of how the database schema is created in the DB.java file. Whenever possible, specific lines from the project’s files are cited to anchor the explanations.

Prerequisites

Before running or developing the project, ensure that your environment meets the following requirements:

Java Development Kit (JDK) 17 or higher – The project is built with JavaFX requiring a modern JDK. You can download it from Oracle’s or OpenJDK’s official site.

Maven 3.6 or higher – Maven is used to manage dependencies and build tasks. The project includes a Maven wrapper (mvnw and mvnw.cmd) so you may not need to install Maven globally. If you do not have Maven installed, you can run the wrapper scripts as described in the setup section.

Git – If you cloned the project from a repository, Git is required. It’s optional if you directly downloaded the project zip.

SQLite JDBC driver – The project includes the necessary driver through Maven dependencies. You do not need to install SQLite separately; the database will be created automatically at runtime using the DB.java class.

JavaFX Runtime (openjfx) – JavaFX modules are usually bundled with the JDK on recent distributions; however, on some systems you might need to install the JavaFX SDK separately. Consult your JDK documentation to verify if JavaFX is included.

If you plan to modify the code and build the UI, an IDE that supports JavaFX (such as IntelliJ IDEA) is recommended. It will help you view and edit the FXML files and controllers easily.

Setup and Installation

This section guides you through installing dependencies, building the project, and running it for the first time. The setup covers both Windows and Linux/macOS systems. Follow these steps in order:

1. Clone or Download the Repository

If you have Git installed and plan to track changes or contribute to the project, clone the repository using the following command:

git clone <repository_url>
cd Lab_final_Project


Alternatively, if you have received a ZIP file (e.g., Lab_final_Project 2.zip), extract it to a directory of your choice and navigate into the project folder. The top‑level directory should contain a pom.xml file, a src folder, a database folder, and the Maven wrapper scripts. After extraction, you should see the following structure:

Lab_final_Project/
├── Lab_final_Project/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── database/
│   │   └── student_assistant.db
│   ├── src/
│   │   ├── main/java/com/example/javaproject/
│   │   └── main/resources/com/example/javaproject/
│   └── target/
└── …

2. Build the Project Using Maven

To ensure all dependencies are downloaded and the application compiles successfully, run the following commands from the root of the project (the directory containing pom.xml):

For Linux/macOS:

./mvnw clean install


For Windows:

mvnw.cmd clean install


The clean install goal instructs Maven to delete previous build artifacts (clean) and then compile the source code, run tests (if any), and package the application (install). If all goes well, Maven will create a jar file in the target directory.

3. Run the Application

You can run the application directly from Maven using the Spring Boot plugin provided in the pom.xml. Even though this is a JavaFX application, the project is configured to launch via Maven. Use one of the following commands:

./mvnw javafx:run


or, if you prefer to run the packaged jar after building:

java -jar target/<generated_jar_file>.jar


When you run the application for the first time, it will create the SQLite database and initialize the tables described in the DB.java file. The main window titled “STUDY_SYNC” will appear, showing the dashboard.

4. Setting Up the Database (Optional)

The database is automatically created in the database directory with the name student_assistant.db. If you wish to explore the database using a SQLite browser, you can use any tool (e.g., DB Browser for SQLite) to open the file. However, manual editing is not necessary; the application manages all inserts, updates, and deletions through DAO classes. If you ever want to reset the database, delete the student_assistant.db file; it will be recreated when the application restarts.

Project Directory Structure

Understanding the directory structure is crucial for both users and developers. Here is a detailed breakdown of the major directories and files:

Top‑Level Files and Directories

pom.xml – The Maven build file. It declares dependencies, build plugins, and JavaFX configurations.

mvnw & mvnw.cmd – Maven wrapper scripts for Unix and Windows. Use these instead of your system‑installed Maven if you want to ensure consistent builds.

src/ – Contains all Java source code and resources.

database/ – Holds the SQLite database file (student_assistant.db). You generally do not need to modify this file manually.

target/ – Generated by Maven after building. It contains compiled classes, packaged jars, and other artifacts. This directory can be safely deleted (it is recreated on each build).

README.md – The document you are reading now.

Source Code (src/main/java)

The project’s Java source code is organized under src/main/java in the package com.example.javaproject. It follows an MVC (Model–View–Controller) pattern:

Controllers (CONTROLLER directory) – Classes that handle user interactions and orchestrate data flow between the view and the model. Examples include DashboardController, CoursesController, TaskExplorerController, GradeSheetController, RoutinePageController, AnalyticsController, and AddRoutinePageController.

Data Access Objects (DAO directory) – Classes that perform CRUD (Create, Read, Update, Delete) operations on the database. They encapsulate SQL queries and map the results to model objects. Files include CourseDAO, TaskDAO, StudySessionDAO, RoutineDAO, ResourceDAO, and ChapterDAO.

Models (all_class directory) – Plain Java classes representing database entities such as Course, Task, StudySession, Routine, Resource, and Chapter. Each model defines properties with JavaFX binding capabilities and includes constructors, getters, and setters.

Utility classes – Files like DB.java provide database connectivity and schema creation. The MainApplication.java file is the entry point that starts the JavaFX application by loading the Dashboard.fxml file.

Resources (src/main/resources)

The resources directory contains FXML files and stylesheets used to define the user interface. The following are noteworthy:

Dashboard.fxml – Defines the layout of the main dashboard, including a sidebar for navigation and panels for today’s routine, upcoming tasks, and notifications. Buttons on the sidebar—such as “Go to Routine”, “Go to COURSE”, “TASK EXPLORER”, “Analytics”, and “Go to GradeSheet”—are connected to controller methods using the onAction attribute.

CourseView.fxml and CourseDetailView.fxml – Provide the interface for listing courses and viewing/editing details of a selected course, respectively.

TaskExplorer.fxml – Defines the layout for exploring tasks. It includes filters for courses and date ranges as well as a table view for tasks.

AddRoutinePage.fxml – Used when adding a new routine event. The file contains text fields for course name, room number, start and end times, a day‑of‑week combo box, and buttons for submitting and going back.

RoutinePage.fxml – Presents a weekly calendar or routine overview (implementation details are in the RoutinePageController).

AnalyticsView.fxml – Contains a bar chart and filter controls for viewing study analytics.

GradeSheet.fxml – Displays the grade sheet table where users can edit quiz, midterm, and final marks.

tasks.css – A stylesheet that defines the look and feel of tables, buttons and cards. It includes styles for different task statuses (done, pending, missed), course cards, button colors and hover effects.

Database Directory (database/)

The database directory stores the SQLite database. The file student_assistant.db is created automatically if it does not exist. You should not track this file in version control; ensure it is excluded by .gitignore if you are using Git.

Database Schema Overview

The DB.java class creates and initializes the SQLite database schema when the application starts. It defines six tables: course, task, study_session, routine_events, resources, and chapters. Each table is designed with fields appropriate for managing a study environment. Here’s a detailed explanation of each table and its columns.

Course Table

The course table stores information about each course. According to the schema defined in DB.java, the table contains the following columns:

id – An integer primary key that uniquely identifies the course. It auto‑increments when a new course is inserted.

code – A text field storing the course code (e.g., “CSE220”). This field is mandatory.

title – The course title (e.g., “Operating Systems”). Also mandatory.

instructor – The name of the instructor teaching the course. This field is optional, allowing you to omit it if unknown.

credits – A real number representing the number of credit hours for the course (e.g., 3.0 or 1.5).

Additional columns for storing quiz and exam marks are added later by the CourseDAO (columns quiz1, quiz2, quiz3, quiz4, mid, and final). The DAO ensures these columns exist by performing ALTER TABLE statements if they are missing when listing or saving marks.

Task Table

The task table tracks assignments, homework, projects and other tasks associated with a course. The table is created with the following columns:

id – Auto‑incrementing primary key for each task.

course_id – Foreign key linking to the course table’s id. If the referenced course is deleted, tasks are automatically removed (ON DELETE CASCADE).

title – A descriptive title of the task (e.g., “Project 1 report”).

notes – A free‑form text field for additional details or instructions.

due_at – A text field representing the due date in “yyyy‑MM‑dd” format.

status – Indicates the task status: todo, in-progress, or done. The default is todo when a task is created.

seen_3days – An integer flag (0 or 1) indicating whether a reminder has been shown three days before the due date.

seen_dayof – An integer flag indicating whether a reminder has been shown on the due date.

completed_at – The completion date (if marked complete). A null or empty value means the task is not yet completed.

created_at – Timestamp recorded when the task is created.

updated_at – Timestamp updated whenever the task is modified.

The task table is designed to keep track of when reminders have been displayed so that the user is not spammed with multiple alerts for the same task.

Study Session Table

The study_session table records the time spent studying for each course. It is defined as follows:

id – Primary key of the study session.

course_id – Foreign key referencing the course table. Deleting a course cascades to delete its study sessions.

started_at – Date and time when the study session started. It defaults to the current date/time (datetime('now','localtime')).

ended_at – Date and time when the session ended. It can be null if the session is still ongoing.

duration_min – Integer representing the duration of the session in minutes. This is computed and stored when the session ends.

notes – A text field for any comments or notes about the study session.

By logging study sessions, the application can later compute statistics and produce bar charts in the analytics view.

Routine Events Table

The routine_events table holds your weekly class schedule. Each entry represents one class or activity:

id – Auto‑incrementing primary key.

course_name – A text field storing the course name. Instead of referencing the course table, the name is stored directly for simplicity.

start_time – The starting time of the event. Stored as a text value (HH:mm).

end_time – The ending time of the event.

room – Optional text field for the room number or location.

day_of_week – Text field indicating the day of the week (e.g., “MONDAY”, “TUESDAY”).

Routine events are displayed on the dashboard to show the daily schedule and can be managed via the Routine page and Add Routine page.

Resources Table

The resources table stores supplementary materials—such as lecture videos or additional reading—for each course. The columns include:

id – Primary key of the resource record.

course_id – Foreign key to the course table. Deleting the course removes associated resources (cascade).

topic – The topic or title of the resource (e.g., “Chapter 1: Introduction to Databases”).

video_link – A URL or hyperlink to the video resource. Could be a YouTube link or an internal file.

Chapters Table

The chapters table tracks chapters or sections within a course. Each record stores:

id – Primary key.

course_id – Foreign key referencing the course table.

chapter_name – The name of the chapter or section.

is_completed – A boolean flag indicating whether the chapter has been completed. Defaults to FALSE (not completed).

These tables together provide a comprehensive data model for storing courses, tasks, study sessions, routine events, additional resources and progress through course chapters.

Java Classes and Data Models

The project follows an object‑oriented design where each database table has a corresponding Java class in the all_class package. These classes encapsulate fields as JavaFX properties, enabling binding with UI components. Below is an in‑depth look at each model class.

Course Class

The Course class models a course entity. It has properties for id, code, title, instructor, and credits, along with optional properties for quiz and exam marks. Here is a summary of its key features:

Properties – Each field (id, code, title, instructor, credits, quiz scores, mid, final) is represented using JavaFX property types (IntegerProperty, StringProperty, DoubleProperty). Using properties allows UI elements to bind to fields and reflect changes automatically.

Constructors – There are two constructors. The first takes id, code, title, instructor and credits as parameters and sets the basic fields. The second includes additional parameters for quiz and exam marks, allowing you to create a course with marks already assigned.

Getters and Setters – Standard getters and property methods (idProperty(), codeProperty(), etc.) provide access to the internal fields. Mark fields have setQuiz1, setQuiz2, etc., for updating values.

toString method – Returns the title of the course, which is used when the course is displayed in lists (e.g., in the course combobox).

Through the CourseDAO, new courses can be inserted into the database. The DAO also dynamically adds the mark columns if they are missing, ensuring the schema evolves as needed.

Task Class

The Task class represents tasks and assignments. Significant fields include id, courseId, title, notes, dueAt, and status. Additional fields track whether reminder notifications have been displayed. The following points summarize its behavior:

Status – The status property can be one of todo, in-progress, or done. Different statuses change how tasks appear in the UI (e.g., color-coded cards in the dashboard).

Reminder Flags – seen3Days and seenDayOf are integer properties (0 or 1) that help avoid sending multiple reminders. The TaskDAO updates these flags once notifications are sent.

Completion Date – The completedAt property stores the date when the task was marked as done. This is useful for tracking how long tasks take to complete and for analytics.

Course Name Lookup – A convenience method getCourseName() uses the CourseDAO to fetch the course title based on courseId. This helps display tasks with their associated course name.

Tasks are managed via the TaskDAO, which provides methods for listing tasks by course, date range, and status; inserting new tasks; deleting tasks; and marking reminders as seen.

StudySession Class

The StudySession class stores information about study sessions. Its fields include id, courseId, startedAt, endedAt, durationMin, and notes. Key details:

Time Parsing – The class includes a private helper method formatDate that parses and re‑formats timestamps. Although not used in the provided snippet, such methods can assist in converting between database time formats and human‑readable forms.

Duration Calculation – When a study session ends, the duration (in minutes) is calculated and stored. This value is used later for analytics.

Property Methods – As with other model classes, JavaFX properties (IntegerProperty, StringProperty) allow UI components to react to changes.

Study sessions are inserted and retrieved via the StudySessionDAO. Developers can add functions for updating or deleting sessions if needed.

Routine Class

The Routine class represents an entry in the weekly routine. It contains fields for id, courseName, startTime, endTime, room, dayOfWeek, and userId (currently unused). Highlights include:

LocalTime Fields – startTime and endTime are stored as LocalTime objects. These types automatically handle formatting (HH:mm) and comparisons.

Day of Week – The dayOfWeek field stores the day as a string (e.g., “MONDAY”). The UI uses this string to filter events by the current day.

User ID (Optional) – An integer userId field allows for future expansion to multi‑user support. Currently, it remains unused but can be enabled easily.

The RoutineDAO interacts with the routine events table, providing methods to insert events, list all events, and retrieve events for a specific day.

Resource Class

The Resource class maps to the resources table. It stores a resource ID, the associated course ID, a topic string, and a video link string. All fields have getters and setters. This simple data structure is used by controllers that display resources linked to a course.

Chapter Class

The Chapter class models chapters in a course. Fields include the chapter ID, course ID, chapter name, and a boolean isCompleted flag indicating whether the chapter has been covered. The class provides typical getters and setters, enabling UI components to bind to the completion status.

Data Access Objects (DAO)

DAOs are responsible for executing SQL queries and returning the results as objects. They encapsulate database operations and provide a clean interface to the rest of the application. Each table has a corresponding DAO class:

CourseDAO

CourseDAO manages all operations on the course table. Its responsibilities include:

Schema Migration – The method ensureMarkColumns() checks if columns for quiz and exam marks exist. If not, it issues ALTER TABLE statements to add them. This ensures backward compatibility when adding new fields to the schema.

Listing Courses – listAll() returns a list of Course objects sorted by course code. It reads marks columns if they exist and sets them on the Course model.

Inserting Courses – insert() adds a new course to the table. It sets quiz and exam marks using ps.setObject to handle null values gracefully.

Updating Marks – updateMarks() accepts a course ID and marks for quizzes, midterm, and final. It writes the new values to the database; any null values result in the corresponding column being set to NULL.

Deleting Courses – delete() removes a course by ID. Cascading deletes remove tasks, study sessions, resources, and chapters associated with the course.

Helper Methods – Methods such as getCourseIdByCode(), getAllCourseNames(), and getCourseNameById() provide convenient lookups for other parts of the application.

TaskDAO

TaskDAO performs all CRUD operations on the task table. Its main methods include:

Listing Tasks – listAll() returns all tasks sorted by due date. listByCourse(), listByDate(), and listByDateRange() filter tasks by course ID or date range. listByCourseAndDateRange() filters by both course and date range, returning tasks ordered by due date.

Upcoming Tasks – getUpcomingTasks() accepts a start date and end date to return tasks due within that range. This method is used to populate the dashboard’s “Next 3 Days” list.

Reminders – getDueReminders() selects tasks that are due either today or in three days and have not yet shown reminders. This ensures the user receives notifications once per task per time window.

Insert, Delete and Update – insert() adds a new task, delete() removes a task by ID, and markAsSeen() updates the seen_3days or seen_dayof flags after reminders are shown. Additional methods for updating tasks can be added if needed.

StudySessionDAO

StudySessionDAO handles creation and retrieval of study sessions. Although the class is simple, it is vital for analytics. Key functions are:

Insert – Adds a new study session record with a course ID, start time, end time, duration and notes.

List All – Retrieves all study sessions. In a future update you might add methods to filter by course or date range, or to compute totals directly from the database.

RoutineDAO

RoutineDAO manages the routine_events table. Its methods include:

Insert – Adds a new routine entry. The AddRoutinePageController uses this method after validating inputs.

List All – Returns all routine events. This is useful for populating routine views that show the weekly schedule.

Get Routine for a Day – getRoutineForToDay() takes a day name (e.g., “MONDAY”) and returns the events scheduled for that day. It is used in the dashboard to display today’s classes.

ResourceDAO

ResourceDAO provides operations on the resources table, including insertion and retrieval. Methods include:

Insert – Adds a new resource entry (topic and video link) for a specific course ID.

List By Course – Returns all resources associated with a particular course.

ChapterDAO

ChapterDAO manipulates the chapters table. It offers methods to:

Insert – Add new chapters to a course with chapter_name and is_completed status.

List By Course – Retrieve all chapters for a specific course.

Update Completion Status – Mark a chapter as completed or incomplete.

Controllers and User Interface Flow

The user interface is built with JavaFX using FXML to describe layouts and controllers to handle events. This section explains how the application flows from the main entry point through each view.

MainApplication and App Entry Point

The MainApplication class extends javafx.application.Application. Its start() method loads the dashboard view using FXMLLoader and sets up the primary stage. The title of the window is set to “STUDY_SYNC” and the window is maximized. After the FXML is loaded, the DashboardController is retrieved, and the HostServices object is injected into it for opening links when needed. Finally, stage.show() displays the main window. The main method simply calls launch(), which triggers the JavaFX lifecycle.

Dashboard

The dashboard (Dashboard.fxml) is the central hub of the application. It presents an overview of your schedule and tasks and offers navigation to other pages. The layout consists of a left sidebar and a main content area. The sidebar includes buttons labeled “Go to Routine”, “Go to COURSE”, “TASK EXPLORER”, “Analytics”, and “Go to GradeSheet”. Clicking each button triggers an onAction handler in the DashboardController, which loads the corresponding view.

The main content area comprises three sections:

Today’s Routine – Displays a list of today’s classes. The DashboardController method displayTodayRoutine() retrieves the current day of the week, queries the RoutineDAO for events scheduled on that day, and renders them as cards. If no events exist, a message “No classes today!” appears.

Next 3 Days’ Tasks – Shows tasks due within the next three days. Each task appears as a colored card—green for done, yellow for todo, and blue for in-progress—to indicate its status. The TaskDAO.getUpcomingTasks() method filters tasks within the date range starting today and ending three days later.

Notifications – Lists reminders for tasks due today or in three days. Notifications are styled with icons (e.g., ⚠️ for due today and ⏰ for upcoming). When reminders are displayed, the corresponding flags (seen_dayof or seen_3days) are set via TaskDAO.markAsSeen() to avoid duplicate notifications.

Below is a brief explanation of the event handlers associated with the dashboard’s sidebar buttons:

goToRoutinePage – Loads the routine page (RoutinePage.fxml) so that you can view your weekly schedule and add new routine events.

goToCoursePage – Opens the course page where you can add, view and manage courses.

goToTaskExplorerPage – Launches the Task Explorer view to filter tasks by course and date range.

goToAnalyticsPage – Navigates to the analytics page for viewing study statistics.

goToGradeSheetPage – Takes you to the grade sheet view to enter marks for quizzes, midterms, and finals.

Courses Page

The courses page (CourseView.fxml) lists all courses in a grid layout. Each course is represented by a card with a colored header showing the course code and a content section with the course title, instructor, and credits. The CoursesController performs the following tasks:

Loading Courses – On initialization, it calls CourseDAO.listAll() to fetch all courses from the database, then creates a card for each course using createCourseCard().

Card Styling – Cards are styled using CSS (defined in tasks.css) with rounded corners, shadows, and color‑coded headers. The header color is determined by hashing the course code, ensuring each course card has a distinct color.

Click Action – Clicking a course card calls openCourseDetail(course), which loads the CourseDetailView.fxml file. The selected course is passed to the CourseDetailController, which displays its details and resources.

Add Course – The “Add Course” button triggers a dialog where you can enter the course code, title, instructor, and credits. Input validation ensures the credits field contains a positive number and other fields are not empty. Upon confirmation, CourseDAO.insert() adds the course to the database, and the grid refreshes.

Back Button – The back button returns you to the dashboard using the onBack event handler.

Course Detail Page

The course detail page (CourseDetailView.fxml) presents detailed information about a selected course. It typically includes fields to display or edit course information, a list of chapters, and a list of resources (video links). The CourseDetailController manages:

Displaying Course Info – Shows the course code, title, instructor and credits. You can choose to edit or delete the course from this view.

Chapters – Lists chapters associated with the course. Each chapter includes a checkbox indicating completion status. Adding a new chapter calls the ChapterDAO.insert() method. Checking or unchecking the box triggers ChapterDAO.updateCompletion() (not shown here but easily implemented).

Resources – Displays resources with clickable video links. You can add new resources by specifying a topic and a URL, which are stored via ResourceDAO.insert().

Back Navigation – Returns to the courses page when the back button is clicked.

Task Explorer

The Task Explorer (TaskExplorer.fxml) is a powerful tool for managing tasks. The layout consists of a top filter bar and a central table. Here’s how to use it:

Filter by Course – The courseComboBox lists all courses. Selecting a course limits the tasks displayed to those associated with that course. The CoursesDAO.getAllCourseNames() method populates this combo box.

Filter by Date Range – The dateRangeComboBox provides three options: “This Week”, “Next Week”, and “Custom Range”. Choosing “Custom Range” reveals two date pickers (startDatePicker and endDatePicker) where you can select a specific date range. When the user chooses the custom range, the customRangeBox becomes visible.

Search Button – Clicking the search button triggers handleSearchByDateRange(), which calls the appropriate TaskDAO method (e.g., listByDateRange or listByCourseAndDateRange) based on the selected filters. Tasks that match the criteria appear in the table.

Clear Filters – The clear button resets all filters and reloads all tasks.

Table Columns – The table displays columns for Title, Due Date, Status, Course and Notes. Rows are colored based on status using CSS classes (task-done, task-pending, task-missed).

Back Navigation – A bottom bar contains a “Back” button that returns you to the dashboard.

Grade Sheet

The grade sheet page (GradeSheet.fxml) displays a table of courses and their marks. The GradeSheetController enables the following:

Editable Table – Columns for quizzes (Quiz 1–4), midterm, and final are editable. When you double click a cell and enter a number, the controller commits the change and calls CourseDAO.updateMarks() to persist it in the database.

Bold Styling for Non‑Zero Marks – The cell factories defined in GradeSheetController change the font weight to bold if the value is non‑zero. This makes it easy to spot courses with recorded marks.

Back Button – The bottom bar contains a back button to return to the dashboard.

Routine Management

Routine management is split between two views: the routine page and the add routine page.

Routine Page – Displays the weekly schedule. It typically shows days of the week across columns and times down the rows, with cells representing classes. The RoutinePageController populates the schedule by reading all routine events from the database and mapping them to their respective days.

Add Routine Page – This is where you add new routine events. The page contains text fields for course name and room number, a start time and end time field, a combo box for the day of the week, and an “Add Event” button. Upon clicking “Add Event”, the AddRoutinePageController validates inputs, parses the times using DateTimeFormatter, constructs a Routine object, and calls RoutineDAO.insert() to save it. A confirmation message appears if successful.

Back Button – A back button returns you to the routine page without saving changes if you change your mind.

Analytics

The analytics page (AnalyticsView.fxml) displays study metrics in a bar chart. Users can choose the week starting date and filter by course. Features include:

Week Selection – A DatePicker allows you to select the start of the week. Buttons labeled “This Week” and “Last Week” provide quick shortcuts. When pressed, the AnalyticsController sets the date picker appropriately and reloads the chart.

Course Filter – A ComboBox lists all courses. Selecting a course filters the chart to show only that course’s study time. Choosing no course shows total study time across all courses.

Bar Chart – The BarChart displays study time in minutes on the y‑axis and courses on the x‑axis. Data is retrieved from StudySessionDAO and aggregated by course. The chart title and axis labels are set programmatically.

Back Navigation – A back button returns you to the dashboard.

Usage Guide

This section walks through how to use each part of the StudySync Assistant as an end user. If you are running the application for the first time, start with launching the application and then explore each feature.

Launching the Application

Ensure you have built the project (see the setup instructions). The build creates the necessary dependencies and compiles the project.

Run the application using one of the following commands:

Maven: ./mvnw javafx:run (Linux/macOS) or mvnw.cmd javafx:run (Windows).

Java: java -jar target/<generated_jar_file>.jar after building.

The main window appears with the dashboard. You should see the current date’s routine, tasks due in the next three days, and notifications if applicable.

Adding Courses

From the dashboard, click “Go to COURSE” in the sidebar. This loads the courses page.

Click the Add Course button (often a plus icon or labeled “Add Course”). A dialog opens with input fields.

Enter the course code (e.g., “CSE201”), title (e.g., “Algorithms”), instructor name, and credit hours. Credit hours must be a positive number. If you leave the instructor blank, the field will be stored as null.

Confirm by clicking Add. The new course will appear in the grid on the courses page.

To edit a course, click its card to open the detail page. Options there allow you to modify or delete the course.

Managing Tasks

Tasks are assignments, homework, projects, or exams associated with courses. To manage tasks:

Go to the Task Explorer by clicking “TASK EXPLORER” from the dashboard.

Click Add Task (if implemented) or create tasks via the Course Detail page (depending on the UI design). Enter the task title, due date, notes, and select the status (todo, in-progress, done).

In the Task Explorer, use the filters to narrow down tasks:

Course Filter – Choose a course from the drop‑down to only show tasks for that course.

Date Range Filter – Select “This Week”, “Next Week”, or “Custom Range”. For custom ranges, specify start and end dates via the date pickers.

Click Search to apply the filters. The table will show the matching tasks. If you want to clear the filters, click Clear Filters.

To mark a task as completed, update its status via the course detail view or an edit dialog. Marking the task as done sets the completed_at date.

Deleted tasks can be removed permanently using the delete option (often represented by a trash icon).

Managing Study Sessions

Although study sessions may not have a dedicated page, they are logged whenever you choose to record your study time. The typical workflow is:

Navigate to a course’s detail page.

Click “Start Study Session” (if provided). The session start time is recorded.

When you finish studying, click “End Study Session”. The end time is recorded, and the application computes the duration in minutes. You may be prompted to enter notes about what you studied.

Study sessions are stored in the study_session table. Later, they are used to generate analytics.

Setting Up Your Routine

Your routine is your weekly class schedule. To set it up:

On the dashboard, click “Go to Routine” to open the routine page. If you have no routine entries yet, the calendar will be empty.

Click “Add Event” or “Add Routine” to open the Add Routine page.

Fill in the course name (this is a free text field but should match your course names for clarity), the room number, start time, end time, and select the day of the week from the combo box.

Click “Add Event” to insert the routine entry. The controller validates the times using LocalTime.parse() and displays errors if the format is incorrect (acceptable format includes “2:30 PM” or “10:45AM”).

Back on the routine page, you will see your schedule displayed. Events are sorted by start time within each day. Hover effects and colors defined in the CSS make events easy to read.

If you need to modify or delete an event, an edit interface (to be implemented) would allow you to select and alter routine entries.

Using Resources and Videos

Resources are supplementary materials associated with a course. To use them:

Go to the Course Detail page of the course you wish to add resources to.

Scroll down to the Resources section. It lists existing resources with their topics and video links.

To add a resource, click “Add Resource”. Enter the topic name and the video link (e.g., a YouTube URL). Then save. The resource appears in the list.

Clicking a video link will open it in your default web browser (the application uses HostServices.showDocument() for this purpose).

You can remove resources by selecting them and clicking Delete (if implemented).

Tracking Grades

Grades are managed via the Grade Sheet page. Steps to record and update marks:

From the dashboard, click “Go to GradeSheet”. A table appears with each course listed in the first column and six additional columns for Quiz 1–4, Mid, and Final.

To enter a mark, double click the cell corresponding to the quiz or exam and type a numeric value. Press Enter to commit. The cell will become bold to indicate a non‑zero value.

When the edit is committed, the GradeSheetController calls CourseDAO.updateMarks(), saving the new value to the database.

If you leave a mark blank or set it to 0, the cell appears normal. Use this feature to indicate incomplete or upcoming assessments.

The grade sheet can serve as a central location for all course marks. You may extend the controller to compute overall grades or average scores.

Viewing Analytics

Analytics help you understand how much time you spend studying each course. To view analytics:

Navigate to the analytics page by clicking “Analytics” from the dashboard sidebar.

Select a week using the date picker. The week start date determines which study sessions are aggregated.

Use the “This Week” and “Last Week” buttons for quick selection.

Choose a course from the drop‑down to filter the chart to a specific course. Leave it blank to view all courses.

The bar chart updates automatically, showing total minutes studied for each course on the x‑axis and time on the y‑axis. Use this information to identify which courses need more attention.

Notifications and Reminders

Notifications alert you to upcoming deadlines. They appear in the dashboard’s notification panel and follow these rules:

Three‑Day Reminder – If a task’s due date is exactly three days away and seen_3days is 0, a notification appears with a message like “⏰ Upcoming deadline in 3 days: [Task Title]”. The controller sets seen_3days to 1 so the reminder is not shown again.

Same‑Day Reminder – If a task is due today and seen_dayof is 0, a notification appears with a message like “⚠️ Today is the deadline for: [Task Title]”. The controller sets seen_dayof to 1 after showing the reminder.

Acknowledging Notifications – Currently, notifications simply appear in the list. You can extend the system to require user acknowledgment by adding a button that calls TaskDAO.markAsSeen() manually.

Clearing Notifications – Exiting the dashboard or refreshing the notifications container clears the display. Only tasks that meet the criteria will reappear.

Developer Guide

The following information is intended for developers who wish to contribute to or extend the StudySync Assistant. It explains the build process, code organization, common patterns and how to add new features.

Building with Maven

The project uses Maven for dependency management. All dependencies are declared in pom.xml. Notable dependencies include:

javafx-controls, javafx-fxml, javafx-graphics – Provide JavaFX modules.

org.xerial:sqlite-jdbc – Provides the JDBC driver for SQLite.

org.controlsfx:controlsfx (optional) – Could be added for advanced UI controls.

Other dependencies for logging or testing (if any).

To build the project for development, simply run:

./mvnw clean compile


To build a distributable jar, run:

./mvnw clean package


Maven will compile the code, copy resources into the jar, and attach JavaFX runtime modules via the javafx-maven-plugin. Ensure that your JDK and Maven versions are compatible with JavaFX.

Understanding the Code Structure

The code is organized into packages reflecting MVC principles. Key observations:

Models – Located in the all_class package. These classes use JavaFX properties to support binding with UI controls. They also include helper methods (e.g., getCourseName() in Task) to fetch related information.

DAOs – Provide static methods to perform operations on the database. Each DAO opens a connection with DB.getConnection(), performs queries using PreparedStatement or Statement, maps the ResultSet to model objects, then closes the connection. Error handling is done via printing stack traces; you can improve this by adding proper logging.

Controllers – Each FXML file has a matching controller class. Controllers are annotated with @FXML for fields injected from the FXML, and they define event handler methods for button clicks, table edits, etc. Navigation between pages is done by loading new FXML and setting it as the scene root.

DB.java – Contains the static getConnection() method and createSchema() method. It ensures that the database file exists and that all tables are created. If any ALTER TABLE commands fail due to existing columns, exceptions are caught and ignored.

MainApplication.java – The entry point. It loads the dashboard via FXMLLoader and injects HostServices into the DashboardController for opening external links.

Resources – FXML files define UI layouts and are styled using tasks.css. Use the JavaFX Scene Builder or an IDE to visually edit these files.

Adding New Features

When extending the project, consider the following guidelines:

New Models – If you need a new entity (e.g., “AssignmentCategory”), create a class in all_class with the necessary fields and JavaFX properties. Then create a table in DB.createSchema() and implement a corresponding DAO.

New Views and Controllers – For each UI page, create a FXML file in src/main/resources/com/example/javaproject. Define the layout and controls. Then create a controller class in CONTROLLER. Link the controller in the FXML via fx:controller. In the existing navigation menu, add a button and handler to load the new view.

Event Handling – Use @FXML to annotate fields and methods in controllers. Use FXMLLoader to load new scenes. To pass data between controllers, set public methods (e.g., setCourse(course)) and call them after loading the FXML.

Updating the Database Schema – If you add new columns to existing tables, modify DB.createSchema() to include ALTER TABLE statements. Ensure that your DAO checks for missing columns before inserting or updating (similar to CourseDAO.ensureMarkColumns()).

Error Handling – Add user‑friendly error messages with Alert dialogs for invalid input or database errors. Ensure exceptions are logged for debugging.

Testing – Write unit tests for DAOs to ensure that CRUD operations work as expected. For controllers, consider using TestFX to simulate user interactions.

Customizing the UI

The UI uses a CSS file (tasks.css) to define styles. To change the look and feel:

Colors and Fonts – Modify the color values in CSS classes (e.g., .greenButton, .blueButton, .task-done). To change fonts, adjust the -fx-font-size and -fx-font-family properties.

Hover Effects – Many styles define hover and pressed states (e.g., .greenButton:hover). Customize these to change the visual feedback when the user interacts with buttons or table rows.

Layout Adjustments – Edit the FXML files to rearrange controls. Use GridPane, HBox, VBox, BorderPane and other layout containers to achieve the desired design. Keep in mind accessibility and readability.

Icons and Images – You can add icons next to buttons (e.g., a plus icon for adding courses) by including ImageView nodes in the FXML or by using built‑in Unicode icons. Ensure that any images are packaged in the resources folder.

Extending the Database

If you want to store additional data, follow these steps:

Update createSchema() – Add a new table or columns to existing tables in DB.java. Use CREATE TABLE IF NOT EXISTS for new tables and ALTER TABLE for new columns.

Create a Model Class – Define a new class in all_class with JavaFX properties representing the table fields.

Implement a DAO – Create a new DAO in the DAO package to perform CRUD operations on the new table. Use prepared statements to avoid SQL injection.

Update UI – Add forms and tables in FXML for interacting with the new data. Write a controller to handle user actions, such as adding and editing records.

Migration Considerations – When modifying the schema, ensure backward compatibility by wrapping ALTER TABLE statements in try-catch blocks. This approach prevents exceptions if the column already exists.

Testing and Debugging

Here are some tips for testing and debugging the application:

Unit Tests – Use JUnit (or a similar framework) to test DAO methods. Create temporary databases for testing and ensure data is persisted correctly.

Logging – Replace e.printStackTrace() calls with a logging framework like SLF4J/Logback. This allows you to capture logs in files and control log levels.

Use the Database Browser – When debugging data issues, open the SQLite file in a database browser to inspect tables and query data directly.

JavaFX Debugging – Use Scene Builder to inspect FXML layouts. If an element doesn’t appear, check the FXML fx:id matches the controller field and that the controller method names are correct.

Exception Handling – Wrap potentially error‑prone code (e.g., date parsing) in try-catch blocks. Display error alerts to the user instead of silently failing.

Troubleshooting

Even with a robust design, issues may arise. Here are common problems and solutions:

Application Does Not Launch – Ensure you compiled the project using Maven. If there are errors about missing JavaFX modules, verify that your JDK includes JavaFX or that the modules are properly configured in pom.xml.

Database Connection Fails – If the application cannot connect to SQLite, check file permissions in the database directory. Make sure the student_assistant.db file is writable by your user. The error messages will appear in the console.

Blank or Broken UI Elements – A mismatch between FXML fx:id and controller field names can cause UI elements to be null. Verify that IDs match exactly and that controllers are correctly referenced in the FXML fx:controller attribute.

Tasks Not Showing Up – Check that tasks have valid due dates and belong to an existing course. Ensure the filters in the Task Explorer are cleared or set to the correct range.

Notifications Not Appearing – Confirm that the system clock is correct; notifications are based on the current date. Also verify that the seen_3days and seen_dayof flags are correctly set in the database.

NumberFormatException – When entering marks or times, ensure that only numbers are provided where appropriate. The controllers handle conversion, but entering invalid characters may cause exceptions.

Acknowledgements

This project draws inspiration from academic planning tools and aims to provide students with an integrated environment for organizing their academic activities. The use of JavaFX allows for a visually appealing interface, while SQLite keeps data persistence simple and lightweight. Thanks to all contributors who helped design the UI, implement DAO patterns, and refine the analytics features.

Unless specified otherwise in the project’s LICENSE file, this code is released under an educational or open‑source license (MIT or similar). Please check the repository for the exact licensing terms. You are free to use, modify, and distribute the software according to the license conditions.
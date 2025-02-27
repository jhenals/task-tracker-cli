# Task Tracker Application
Task Tracker is a simple CLI application for managing tasks such as tracking what users need to do, what they're working on, and what they've completed.
https://roadmap.sh/projects/task-tracker

## Features 
* Add, Update, and Delete tasks
* Mark a task as in progress or done
* List all tasks
* List all tasks that are done
* List all tasks that are not done
* List all tasks that are in progress

## Installation
1. Clone the repository:
```
git clone https://github.com/jhenals/task-tracker-cli
cd task_tracker_cli
```
2. Compile source code:
* Generate a list of all .java files in the ```src``` directory:
```
javac -d out -cp src @sources.txt
```
* Compile using the  ```sources.txt ``` file
```
javac -d out -cp src @sources.txt
```

3. Run application:
```
java -cp out TaskCLI
```

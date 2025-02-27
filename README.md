# Task Tracker Application
Task Tracker CLI is a simple command-line application to help you manage your tasks. You can add, update, delete, and track your tasks directly from the terminal. Tasks are stored in a JSON file.
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
dir /s /b src\*.java > sources.txt
```
* Compile using the  ```sources.txt ``` file
```
javac -d out -cp src @sources.txt
```

3. Run application:
```
java -cp out TaskCLI
```

## Usage
* Add task <br/>
  ![image](https://github.com/user-attachments/assets/f1e03b4d-31fc-4097-8a12-178a228cd93a)

* List all tasks <br/>
  ![image](https://github.com/user-attachments/assets/680e98ce-cef1-4a4c-82e2-06d3b6c0b626)

* Update task <br/>
  ![image](https://github.com/user-attachments/assets/79d4d24b-f100-460a-a664-31536ae15a80)
 
* Mark task as in-progress <br/>
  ![image](https://github.com/user-attachments/assets/6b9a80da-db37-4316-8360-30801f129d58)

* List all to-do tasks <br/>
  ![image](https://github.com/user-attachments/assets/12fe1a55-c6ac-44e8-aaed-17afdb3bf6aa)

* List all in-progress tasks <br/>
    ![image](https://github.com/user-attachments/assets/8b9076da-5555-45fe-b578-15b016be4bfd)

* Mark task as done <br/>
    ![image](https://github.com/user-attachments/assets/fbf4f4b9-76ae-4019-b81d-69352613b467)

* List all done tasks <br/>
  ![image](https://github.com/user-attachments/assets/153209d6-4a79-45bf-a939-a0e6b607044a)

* Delete task <br/>
  ![image](https://github.com/user-attachments/assets/ef47a23c-d152-4aaf-931d-cb49e92c0e36)

# ExchangeRate Backend

This is the backend directory of the ExchangeRate project. It contains a Flask project that creates the HTTP routes that the front-end will use to interact with the server.
## Getting Started with the database
Before starting with the project, you should download mySQL WorkBench Community Server edition: https://dev.mysql.com/downloads/mysql/
Next you should setup the application and create a server called exchange. You should also set your password for root to RootyRooty123 or change the password used in the db_config file in the project.


## Getting Started with Flask
Before running the Flask server, make sure you have the necessary dependencies installed. You can install them using pip:

This is the backend directory of the ExchangeRate project. It contains a Flask project that creates the HTTP routes that the front-end will use to interact with the server.
To get started with the ExchangeRate backend, clone the repository:

```
git clone https://github.com/michael-ferzli/430Lproject.git
```

Then, navigate to the backend directory and install the necessary dependencies:

```
cd ExchangeRate/backend
pip install -r requirements.txt
```

After that, you should run:
```
flask shell
```
To create the database with mySQL workbench open in the background:
```
db.create_all()
exit()
```

Once you have installed the dependencies and now have the tables created, you can start the server by running the following command in the backend directory:

Unix:
```
FLASK_APP=app.py FLASK_ENV=development flask run
```
Windows:
```
set FLASK_APP=app.py
set FLASK_ENV=development
flask run
```
This will start the Flask server on http://localhost:5000.

Congrats! You can now try the different routes and use the API by using Postman.
## Technologies Used

The backend of the ExchangeRate project uses the following technologies:
Flask: a web framework used to create the HTTP routes.
SQLAlchemy: an Object-Relational Mapping (ORM) library used to interact with the database.

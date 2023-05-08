Currency Exchange Desktop Application - Technical Documentation

1. Overview

Michael Ferzli's original lab submission was used because he got the highest grade in the group on said lab.
This technical documentation provides an overview of the project structure, general architecture, and high-level architecture of the Currency Exchange Desktop application, a Java-based desktop application for currency exchange services.

2. Project Structure

The Currency Exchange Desktop application is organized into the following packages:

com.mdf00.exchange: Contains the main class and the Authentication class for user authentication management.
com.mdf00.exchange.api: Contains the Exchange, ExchangeService, and model classes for interacting with the remote API.
com.mdf00.exchange.login: Contains the FXML controller class for the Login
com.mdf00.exchange.register: Contains the FXML controller class for the registration
com.mdf00.exchange.rates: Contains the FXML controller class for the rates page of our applications, where current rates are displayed along with the caluclator.
com.mdf00.exchange.Statistics: Contains the FXML controller class for the Statistics feature where the data is handled to display on the frontend.
com.mdf00.exchange.transactions: Contains the FXML controller class for the page that displays the transactions of the user.
com.mdf00.exchange.Forum: Contains the FXML controller class for the Forum Feature of our application
com.mdf00.exchange.Currency_game: Contains the FXML controller class for the currency game feature
com.mdf00.exchange.ExchangeTransaction: Contains the FXML controller class for the offers features where user can Exchange transaction.
Ressources: Contains the FXML files for the user interface.

3. General Architecture

The application uses a Model-View-Controller (MVC) architectural pattern. The user interface is designed using JavaFX and FXML, and the controllers handle user interactions and application logic. The model classes define the data structures used by the application, and the ExchangeService class manages communication with the remote API.

4. High-Level Architecture

The high-level architecture of the application consists of the following software modules:

User Interface (UI): The UI module is responsible for rendering the application's views and capturing user input.
Controller: The controller module handles user interactions, processes input, and manages the application logic.
Model: The model module defines the data structures and objects used by the application.
ExchangeService: The ExchangeService module manages communication with the remote API and provides methods for performing API calls.

These modules interact with each other as follows:

The UI module sends user input to the controller module.
The controller module processes the input, updates the model if necessary, and makes calls to the ExchangeService module to interact with the remote API.
The ExchangeService module communicates with the remote API and returns the requested data to the controller module.
The controller module updates the UI module with the retrieved data or any changes made to the model.

5. Model

The model package contains classes that define the data structures used by the application. These classes include User, Message, Transaction, Offer, and Rate. Each class corresponds to a specific entity in the application's domain and includes properties, getters, and setters. These classes utilized the SerializedName package in jaba in order to directly map the api's body to an object on the frontend.

6. ExchangeService

The ExchangeService class is responsible for managing communication with the remote API. It uses Retrofit, a type-safe HTTP client for Java, to make API calls and handle HTTP requests and responses. The ExchangeService class defines an interface called Exchange that includes methods for performing various API calls, such as retrieving exchange rates, managing transactions, and interacting with the forum.

7. Resources Directory

The resources directory contains various assets used by the application, including FXML files, CSS stylesheets, and images. The FXML files define the layout and structure of the user interface, and the CSS stylesheets control the appearance and styling of the UI elements. Images, such as icons and logos, are also stored in the resources directory. Each page of the frontend in FXML is stored in a separate directory along with its respective StyleSheets


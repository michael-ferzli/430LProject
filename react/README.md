# Getting Started with Create React App

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.\
You may also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### New implementation
In additional to the previous implementation, this React application now allows users to perform exchange transactions, view exchange rates, statistics, and participate in a forum. The application consists of the following components:

### ExchangeRateGraph: 
Displays a line chart of the average exchange rate over time. Users can choose to view data for the last day, month, year, or all-time.

### Forum: 
Allows users to post messages and view the last 5 messages in the forum.

#### ExchangeRateStats: 
Displays exchange rate statistics such as maximum, minimum, and average exchange rates for different time periods (last day, week, month, and year).

#### UserTransactions: 
Allows users to perform exchange transactions by posting offers and accepting available offers.

#### Installation
Make sure you have Node.js and npm installed on your machine. You can download Node.js from https://nodejs.org/en/download/.

Clone the repository to your local machine.

Navigate to the project directory and install the required dependencies by running npm install.

Start the development server by running npm start. The application will open in your default browser at http://localhost:3000.

### Usage
Users must sign up and log in to access the application features.

On the main page, users can view the ExchangeRateGraph, ExchangeRateStats, and their personal transactions in UserTransactions.

Users can post and accept offers in the UserTransactions component.

Users can view and participate in the forum by clicking on the "Forum" link in the navigation menu.

#### Dependencies
The application uses the following dependencies:

React: A JavaScript library for building user interfaces.
recharts: A charting library built on top of React and D3.
@mui/material: Material-UI components for React.
react-router-dom: A collection of navigational components for React applications.
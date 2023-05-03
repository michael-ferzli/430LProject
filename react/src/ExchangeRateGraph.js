import React, { useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

function ExchangeRateGraph({ userToken }) {
  const [transactions, setTransactions] = useState([]);
  const [timeRange, setTimeRange] = useState('all');

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await fetch(`http://localhost:5000/graph`, {
          headers: {
            Authorization: `bearer ${userToken}`,
          },
        });
        const data = await response.json();
        setTransactions(data);
      } catch (error) {
        console.error('Error fetching transactions:', error);
      }
    };

    fetchTransactions();
  }, [userToken]);

  const calculateMean = (array) => {
    const sum = array.reduce((a, b) => a + b, 0);
    return sum / array.length;
  };

  const filterTransactionsByDate = (transactions, days) => {
    const currentDate = new Date();
    const filteredTransactions = transactions.filter(transaction => {
      const transactionDate = new Date(transaction.added_date);
      const timeDifference = Math.abs(currentDate - transactionDate);
      const dayDifference = timeDifference / (1000 * 60 * 60 * 24);
      return dayDifference <= days;
    });
    return filteredTransactions;
  };

  let filteredTransactions = transactions;
  if (timeRange === 'day') {
    filteredTransactions = filterTransactionsByDate(transactions, 1);
  } else if (timeRange === 'month') {
    filteredTransactions = filterTransactionsByDate(transactions, 30);
  } else if (timeRange === 'year') {
    filteredTransactions = filterTransactionsByDate(transactions, 365);
  }

  const data = filteredTransactions.map((transaction, index, array) => {
    const currentRates = array.slice(0, index + 1).map(t => t.lbp_amount / t.usd_amount);
    const meanRate = calculateMean(currentRates);
    return {
      date: new Date(transaction.added_date).toLocaleDateString(),
      rate: meanRate,
    };
  });

  return (
    <div>
      <LineChart width={800} height={300} data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="date" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Line type="monotone" dataKey="rate" stroke="#8884d8" />
      </LineChart>
      <div>
        <button onClick={() => setTimeRange('all')}>All Time</button>
        <button onClick={() => setTimeRange('day')}>Last Day</button>
        <button onClick={() => setTimeRange('month')}>Last Month</button>
        <button onClick={() => setTimeRange('year')}>Last Year</button>
      </div>
    </div>
  );
}

export default ExchangeRateGraph;

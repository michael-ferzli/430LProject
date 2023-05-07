import React, { useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import './ExchangeRateGraph.css';

function ExchangeRateGraph({ userToken }) {
  const [transactions, setTransactions] = useState([]);
  const [timeRange, setTimeRange] = useState('all');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

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

  const filterTransactionsByDate = (transactions, start, end) => {
    const isCustomRange = typeof start === 'string' && typeof end === 'string';
    const startDate = isCustomRange ? new Date(start) : new Date(Date.now() - start * 24 * 60 * 60 * 1000);
    const endDate = isCustomRange ? new Date(end) : new Date();
  
    const filteredTransactions = transactions.filter(transaction => {
      const transactionDate = new Date(transaction.added_date);
      return transactionDate >= startDate && transactionDate <= endDate;
    });
    return filteredTransactions;
  };

  const handleCustomRange = () => {
    setTimeRange('custom');
  };

  let filteredTransactions = transactions;
  if (timeRange === 'day') {
    filteredTransactions = filterTransactionsByDate(transactions, 1);
  } else if (timeRange === 'month') {
    filteredTransactions = filterTransactionsByDate(transactions, 30);
  } else if (timeRange === 'year') {
    filteredTransactions = filterTransactionsByDate(transactions, 365);
  } else if (timeRange === 'custom') {
    filteredTransactions = filterTransactionsByDate(transactions, startDate, endDate);
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
    <div className="ExchangeRateGraph">
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="date" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="rate" stroke="#8884d8" />
        </LineChart>
      </ResponsiveContainer>
      <div>
        <button onClick={() => setTimeRange('all')}>All Time</button>
        <button onClick={() => setTimeRange('day')}>Last Day</button>
        <button onClick={() => setTimeRange('month')}>Last Month</button>
        <button onClick={() => setTimeRange('year')}>Last Year</button>
        </div>
        <div>
          <label htmlFor="start-date">Start Date:</label>
          <input
type="date"
id="start-date"
value={startDate}
onChange={(e) => setStartDate(e.target.value)}
/>
<label htmlFor="end-date">End Date:</label>
<input
type="date"
id="end-date"
value={endDate}
onChange={(e) => setEndDate(e.target.value)}
/>
<button onClick={handleCustomRange}>Custom Range</button>
</div>
</div>
);
}

export default ExchangeRateGraph;

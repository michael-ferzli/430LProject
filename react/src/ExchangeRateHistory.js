import React, { useState, useEffect } from "react";
import { Line } from "react-chartjs-2";

const ExchangeRateHistory = () => {
  const [ratesHistory, setRatesHistory] = useState([]);

  useEffect(() => {
    fetch("http://127.0.0.1:5000/exchangeRateHistory")
      .then((response) => response.json())
      .then((data) => {
        setRatesHistory(data);
      });
  }, []);

  const data = {
    labels: ratesHistory.map((item) => item.date),
    datasets: [
      {
        label: "LBP to USD Exchange Rate",
        data: ratesHistory.map((item) => item.lbp_to_usd),
        fill: false,
        borderColor: "#f44336",
      },
      {
        label: "USD to LBP Exchange Rate",
        data: ratesHistory.map((item) => item.usd_to_lbp),
        fill: false,
        borderColor: "#3f51b5",
      },
    ],
  };

  // return (
  //   <div className="wrapper">
  //     <h2>Exchange Rate History</h2>
  //     <Line data={data} />
  //   </div>
  // );
};

export default ExchangeRateHistory;
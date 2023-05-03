import React, { useEffect, useState } from 'react';
import { Typography } from '@mui/material';

function ExchangeRateStats({ userToken }) {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    const fetchStatistics = async () => {
      try {
        const response = await fetch(`http://localhost:5000/statistics`, {
          headers: {
            Authorization: `bearer ${userToken}`,
          },
        });
        const data = await response.json();
        setStats(data);
      } catch (error) {
        console.error('Error fetching statistics:', error);
      }
    };

    fetchStatistics();
  }, [userToken]);

  if (!stats) {
    return <Typography>Loading statistics...</Typography>;
  }

  return (
    <div>
      {Object.entries(stats).map(([key, value]) => (
        <div key={key}>
          <Typography variant="h6">{key.replace('_', ' ').toUpperCase()}</Typography>
          {Object.entries(value).map(([timePeriod, rate]) => (
            <Typography key={timePeriod} variant="body1">
              {timePeriod.charAt(0).toUpperCase() + timePeriod.slice(1)}: {rate}
            </Typography>
          ))}
        </div>
      ))}
    </div>
  );
}

export default ExchangeRateStats;

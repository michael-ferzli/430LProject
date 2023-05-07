import React, { useEffect, useState } from 'react';
import { Typography, Select, MenuItem, FormControl, InputLabel } from '@mui/material';
import './ExchangeRateStats.css';

function ExchangeRateStats({ userToken }) {
  const [stats, setStats] = useState(null);
  const [selectedStat, setSelectedStat] = useState('');

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
        setSelectedStat(Object.keys(data)[0]);
      } catch (error) {
        console.error('Error fetching statistics:', error);
      }
    };

    fetchStatistics();
  }, [userToken]);

  const handleChange = (event) => {
    setSelectedStat(event.target.value);
  };

  if (!stats) {
    return <Typography>Loading statistics...</Typography>;
  }

  return (
    <div>
      <FormControl fullWidth>
        <InputLabel>Select Stat</InputLabel>
        <Select value={selectedStat} onChange={handleChange}>
          {Object.keys(stats).map((key) => (
            <MenuItem key={key} value={key}>
              {key.replace('_', ' ').toUpperCase()}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <div>
        <Typography variant="h6">{selectedStat.replace('_', ' ').toUpperCase()}</Typography>
        {Object.entries(stats[selectedStat]).map(([timePeriod, rate]) => (
          <Typography key={timePeriod} variant="body1">
            {timePeriod.charAt(0).toUpperCase() + timePeriod.slice(1)}: {parseFloat(rate).toFixed(2)}
          </Typography>
        ))}
      </div>
    </div>
  );
}

export default ExchangeRateStats;

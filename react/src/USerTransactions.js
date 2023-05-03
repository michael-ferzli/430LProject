import React, { useState, useEffect } from 'react';
import { Button, TextField, Typography, List, ListItem, ListItemText, Alert, Snackbar } from '@mui/material';

function UserTransactions({ userToken }) {
  const [amount, setAmount] = useState('');
  const [lbpAmount, setLbpAmount] = useState('');
  const [conversionRate, setConversionRate] = useState('');
  const [offers, setOffers] = useState([]);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('success');
  const [openAlert, setOpenAlert] = useState(false);

  useEffect(() => {
    const fetchOffers = async () => {
      try {
        const response = await fetch('http://localhost:5000/getOffers', {
          headers: {
            'Authorization': `Bearer ${userToken}`,
          },
        });
        const data = await response.json();
        setOffers(data);
      } catch (error) {
        console.error('Error fetching offers:', error);
      }
    };

    fetchOffers();
  }, []);

  const postOffer = async () => {
    try {
      const response = await fetch('http://localhost:5000/postOffer', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${userToken}`,
        },
        body: JSON.stringify({
          usd_amount: amount,
          lbp_amount: lbpAmount,
          usd_to_lbp: true,
        }),
      });
      const data = await response.json();
      setOffers([...offers, data]);
      setAmount('');
      setLbpAmount('');
      setMessage('Offer posted successfully!');
      setMessageType('success');
      setOpenAlert(true);
    } catch (error) {
      console.error('Error posting offer:', error);
      setMessage('Error posting offer.');
      setMessageType('error');
      setOpenAlert(true);
    }
  };

  const acceptOffer = async (offerId) => {
    try {
      await fetch('http://localhost:5000/acceptOffer', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${userToken}`,
        },
        body: JSON.stringify({ offer_id: offerId }),
      });
      setOffers(offers.filter((offer) => offer.id !== offerId));
      setMessage('Offer accepted successfully!');
      setMessageType('success');
      setOpenAlert(true);
    } catch (error) {
      console.error('Error accepting offer:', error);
      setMessage('Error accepting offer.');
      setMessageType('error');
      setOpenAlert(true);
    }
  };

  const handleCloseAlert = () => {
    setOpenAlert(false);
  };

   return (
    <div>
      <Typography variant="h5">Perform Exchange Transaction</Typography>
      <div>
        <label htmlFor="amount">Amount (USD):</label>
        <TextField
          id="amount"
          type="number"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="lbpAmount">Amount (LBP):</label>
        <TextField
          id="lbpAmount"
          type="number"
          value={lbpAmount}
          onChange={(e) => setLbpAmount(e.target.value)}
        />
      </div>
      <Button onClick={postOffer} variant="contained" color="primary">
        Post Offer
      </Button>


<Typography variant="h6" style={{ marginTop: '2rem' }}>
  Available Offers
</Typography>
<List>
  {offers.map((offer) => (
    <ListItem key={offer.id}>
      <ListItemText primary={`Amount: ${offer.usd_amount} USD | ${offer.lbp_amount} LBP`} />
      {!offer.accepted && (
        <Button onClick={() => acceptOffer(offer.id)} variant="outlined" color="primary">
          Accept Offer
        </Button>
      )}
    </ListItem>
  ))}
</List>

<Snackbar
  open={openAlert}
  autoHideDuration={6000}
  onClose={handleCloseAlert}
  anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}
>
  <Alert onClose={handleCloseAlert} severity={messageType} sx={{ width: '100%' }}>
    {message}
  </Alert>
</Snackbar>
</div>
);
}

export default UserTransactions;
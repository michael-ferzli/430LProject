import React, { useState, useEffect } from 'react';
import { Button, TextField, Typography, List, ListItem, ListItemText, Box } from '@mui/material';
import './Forum.css';

function Forum({ userToken }) {
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);

  // Add a list of bad words or phrases here to be filtered out
  const badWords = ['fuck', 'bastard', 'putain', 'ahbal'];

  const isMessageAllowed = (message) => {
    return !badWords.some((badWord) => message.toLowerCase().includes(badWord));
  };

  const fetchUserFromId = async (userId) => {
    try {
      const response = await fetch('http://localhost:5000/getUserFromId', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ user_id: userId }),
      });
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error fetching user:', error);
    }
  };
  

  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const response = await fetch('http://localhost:5000/getMessages?limit=5');
        const data = await response.json();
        const messagesWithUsernames = await Promise.all(
          data.map(async (message) => {
            const user = await fetchUserFromId(message.user_id);
            return { ...message, user_name: user ? user.user_name : 'Anonymous' };
          }),
        );
        setMessages(messagesWithUsernames);
      } catch (error) {
        console.error('Error fetching messages:', error);
      }
    };

    fetchMessages();
  }, []);

  
  const postMessage = async () => {
    if (!isMessageAllowed(message)) {
      alert('Your message contains inappropriate content. Please remove any offensive language and try again.');
      return;
    }
    try {
      const response = await fetch('http://localhost:5000/postMessage', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${userToken}`,
        },
        body: JSON.stringify({
          message: message,
        }),
      });
      const data = await response.json();
      const user = await fetchUserFromId(data.user_id);
      const messageWithUserName = {
        ...data,
        user_name: user ? user.user_name : 'Anonymous',
      };
      setMessages([...messages, messageWithUserName]);
      setMessage('');
    } catch (error) {
      console.error('Error posting message:', error);
    }
  };
  

  return (
    <Box sx={{ maxWidth: '800px', margin: 'auto' }}>
    <Box mb={2}>
    <label htmlFor="message">Message:</label>
    <TextField
    id="message"
    value={message}
    onChange={(e) => setMessage(e.target.value)}
    multiline
    fullWidth
    rows={4}
    />
    </Box>
    <Button onClick={postMessage} variant="contained" color="primary" fullWidth>
    Post Message
    </Button>
      <Typography variant="h6" style={{ marginTop: '2rem' }} textAlign="center">
        Last 5 Messages
      </Typography>
      <List>
        {messages.slice(-5).map((msg) => (
          <ListItem key={msg.id} sx={{ alignSelf: userToken === msg.user_id ? 'flex-end' : 'flex-start' }}>
            <ListItemText
              primary={msg.message}
              secondary={msg.user_name}
              secondaryTypographyProps={{ fontStyle: 'italic' }}
            />
          </ListItem>
        ))}
      </List>
    </Box>
    );
    }
    
    export default Forum;
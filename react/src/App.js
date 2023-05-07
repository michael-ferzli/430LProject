import './App.css';
import './ExchangeRateGraph.css';
import './ExchangeRateStats.css';

import { useState, useEffect, useCallback } from "react";
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import UserCredentialsDialog from "./UserCredentialsDialog/UserCredentialsDialog";
import { getUserToken, saveUserToken, clearUserToken } from './localStorage';
import { DataGrid } from '@mui/x-data-grid';
import { MenuItem, TextField, Select } from '@mui/material';
import ExchangeRateGraph from './ExchangeRateGraph';
import ExchangeRateStats from './ExchangeRateStats';
import ExchangeRateHistory from './ExchangeRateHistory';
import ExchangeTransactions from './ExchangeTransactions';
import UserTransactions from './USerTransactions';
import Forum from './Forum';
import MiniGame from './MiniGame';
import { Tooltip } from '@reach/tooltip';
import { useTooltip, useTooltipTrigger } from '@react-aria/tooltip';
import '@reach/tooltip/styles.css';

var SERVER_URL = "http://127.0.0.1:5000";

function App() {

  const States = {
    PENDING: "PENDING",
    USER_CREATION: "USER_CREATION",
    USER_LOG_IN: "USER_LOG_IN",
    USER_AUTHENTICATED: "USER_AUTHENTICATED",
  };

  let [calculatorInput, setCalculatorInput] = useState("");
  let [calculatorOutput, setCalculatorOutput] = useState("");
  let [calculationType, setCalculationType] = useState("usd-to-lbp");
  let [buyUsdRate, setBuyUsdRate] = useState(null);
  let [sellUsdRate, setSellUsdRate] = useState(null);
  let [lbpInput, setLbpInput] = useState("");
  let [usdInput, setUsdInput] = useState("");
  let [transactionType, setTransactionType] = useState("usd-to-lbp");

  let [userToken, setUserToken] = useState(getUserToken());

  let [authState, setAuthState] = useState(States.PENDING);

  let [userTransactions, setUserTransactions] = useState([]);

  const columns = [
    { field: 'id', headerName: 'Transaction ID' },
    { field: 'usd_amount', headerName: 'USD Amount' },
    { field: 'lbp_amount', headerName: 'LBP Amount' },
    { field: 'usd_to_lbp', headerName: 'USD TO LBP' },
    { field: 'added_date', headerName: 'Transaction date' , width:250},

  ];


  const fetchUserTransactions = useCallback(() => {
    fetch(`${SERVER_URL}/transaction`, {
      headers: {
        Authorization: `bearer ${userToken}`,
      }, })
      .then((response) => response.json())
      .then((transactions) => setUserTransactions(transactions));
      }, [userToken]);
      useEffect(() => {
        if (userToken) {
          fetchUserTransactions();
        }
      }, [fetchUserTransactions, userToken]);
  function fetchRates() {
    fetch(`${SERVER_URL}/exchangeRate`)
    .then(response => response.json())
    .then(data => {
      //Update data from database
      setBuyUsdRate(data.lbp_to_usd);
      setSellUsdRate(data.usd_to_lbp);
   });
  }
  useEffect(fetchRates, []);
  function addItem() {
    if(lbpInput<=0 || usdInput<=0){
        return;
    }
    var type = transactionType==="usd-to-lbp" ? 1 : 0;
    
    const data = { usd_amount: usdInput, lbp_amount: lbpInput, usd_to_lbp: type };
    if (userToken === null) {
      fetch(`${SERVER_URL}/transaction`, {
        method: 'POST', // or 'PUT'
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      })
        .then((response) => response.json())
        .then((data) => {
          fetchRates();
        });
    }else{
    fetch(`${SERVER_URL}/transaction`, {
      method: 'POST', // or 'PUT'
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${userToken}`,
      },
      body: JSON.stringify(data),
    })
      .then((response) => response.json())
      .then((data) => {
        fetchRates();
      });
    }
  }

  function login(username, password) {
    return fetch(`${SERVER_URL}/authenticate`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        user_name: username,
        password: password,
      }),
    })
      .then((response) => response.json())
      .then((body) => {
        setAuthState(States.USER_AUTHENTICATED);
        setUserToken(body.token);
        saveUserToken(body.token);
      });
  }
  function createUser(username, password) {
    return fetch(`${SERVER_URL}/user`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        user_name: username,
        password: password,
      }),
    }).then((response) => login(username, password));
  }
  function logout() {
    setUserToken(null);
    clearUserToken();
  }
  function calculateRate() {
    if (calculationType === "usd-to-lbp") {
       setCalculatorOutput(calculatorInput*sellUsdRate);
    } else {
      setCalculatorOutput(calculatorInput/buyUsdRate);
    }
  }
  function AriaTooltip({ children, label }) {
    let { tooltipProps, triggerProps, isVisible } = useTooltip();

    return (
      <useTooltipTrigger {...triggerProps} aria-describedby="description">
        {children}
        {isVisible && (
          <Tooltip {...tooltipProps} id="description">
            {label}
          </Tooltip>
        )}
      </useTooltipTrigger>
    );
  }
  return (
    <body>
        <AppBar position="static">
        <Toolbar classes={{ root: "nav" }}>
          <Typography variant="h5">Exchange</Typography>
          {userToken !== null ? (
            <Button color="inherit" onClick={logout}>
              Logout
            </Button>
          ) :( <div>
              <Button
                color="inherit"
                onClick={() => setAuthState(States.USER_CREATION)}
          > Register
              </Button>
              <Button
                color="inherit"
                onClick={() => setAuthState(States.USER_LOG_IN)}
              >
                Login
              </Button>
          </div> )}
        </Toolbar>
      </AppBar>
      <UserCredentialsDialog
        open={authState === States.USER_CREATION} submitText="Register" title="Register" onClose= {() => setAuthState(States.PENDING)} onSubmit = {(username, password) => createUser(username, password)}
      />
      <UserCredentialsDialog
        open={authState === States.USER_LOG_IN} submitText="Log In" title="Log In" onClose= {() => setAuthState(States.PENDING)} onSubmit = {(username, password) => login(username, password)}
      />
      <Snackbar
        elevation={6}
        variant="filled"
        open={authState === States.USER_AUTHENTICATED}
        autoHideDuration={2000}
        onClose={() => setAuthState(States.PENDING)}
      >
        <Alert severity="success">Success</Alert>
      </Snackbar>
          <div className="wrapper">
              <Typography variant='h4'>Today's Exchange Rate</Typography>
              <p>LBP to USD Exchange Rate</p>
              {/* The handling of the option where there are no rates yet is done by the backend instead of here. */}
              <h3>Buy USD: <span id="buy-usd-rate">{buyUsdRate}</span> </h3>
              <h3>Sell USD: <span id="sell-usd-rate">{sellUsdRate}</span></h3>
              <hr />
              <Typography variant='h4'>Calculator</Typography>
              <br></br>
              <Typography variant='h6'>Conversion result: <span id="calculator-rate">{calculatorOutput}</span> </Typography>
              <br></br>
              <form name="calculator-entry">
                  <div className="amount-input">
                      <label htmlFor="amount">Amount</label>
                      <input id="amount" type="number" value={calculatorInput} onChange={e => setCalculatorInput(e.target.value)} min="0"/>
                  </div>
                  <Select id="calculation-type" defaultValue={"usd-to-lbp"} onChange={e => setCalculationType(e.target.value)}>
                      <option value="usd-to-lbp">USD to LBP</option>
                      <option value="lbp-to-usd">LBP to USD</option>
                  </Select>
                  <Button id="calculate-button" className="button" type="button" onClick={calculateRate}>
                      Add
                  </Button>
              </form>
          </div>
          <div className="wrapper">
            <Typography variant='h4'>Record a recent transaction</Typography>
            <br></br>
              <form name="transaction-entry">
                  <div className="amount-input">
                      <label htmlFor="lbp-amount">LBP Amount</label>
                      <TextField id="lbp-amount" type="number" value={lbpInput} onChange={e => setLbpInput(e.target.value)} min="0" aria-describedby="lbp-description"/>
                  </div>
                  <div className="amount-input">
                      <label htmlFor="usd-amount">USD Amount</label>
                      <TextField id="usd-amount" type="number" value={usdInput} onChange={e => setUsdInput(e.target.value)} min="0"/>
                  </div>
                  <Select id="transaction-type" defaultValue={"usd-to-lbp"} onChange={e => setTransactionType(e.target.value)}>
                      <MenuItem value="usd-to-lbp">USD to LBP</MenuItem>
                      <MenuItem value="lbp-to-usd">LBP to USD</MenuItem>
                  </Select>
                  <AriaTooltip label="This button adds a new transaction"></AriaTooltip>
                  <Button id="add-button" className="button" type="button" onClick={addItem}>
                      Add
                  </Button>
              </form>
          </div>
          <div className="wrapper">
            <Typography variant='h4'>Exchange Rate Graph</Typography>
            <ExchangeRateGraph></ExchangeRateGraph>
            </div>
            <div className="wrapper">
            <Typography variant='h5'>ExchangeRateStats</Typography>
            <ExchangeRateStats userToken={userToken}></ExchangeRateStats>
            </div>
            <div className="wrapper">
            <Typography variant='h6'>User Transactions</Typography>
            <UserTransactions userToken={userToken}></UserTransactions>
            </div>
            <div className="wrapper">
            <Typography variant='h6'>Forum</Typography>
            <Forum userToken={userToken}></Forum>
            </div>
            <div className="wrapper">
            <Typography variant='h6'>Game</Typography>
            <MiniGame ></MiniGame>
            </div>
          {userToken && (
        <div className="wrapper">
          <Typography variant="h5">Your Transactions</Typography>
          <DataGrid
            columns={columns}
            rows={userTransactions}
            autoHeight
/> </div>

)}
    </body>
  );
}

export default App;
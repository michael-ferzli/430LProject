import React, { useState, useEffect } from "react";
import "./MiniGame.css";

const MiniGame = () => {
  const [gameStarted, setGameStarted] = useState(false);
  const [gameEnded, setGameEnded] = useState(false);
  const [amount, setAmount] = useState(0);
  const [exchangeRate, setExchangeRate] = useState(0);
  const [transactionType, setTransactionType] = useState("USD to LBP");
  const [userAnswer, setUserAnswer] = useState("");
  const [score, setScore] = useState(0);
  const [resultMessage, setResultMessage] = useState("");

  useEffect(() => {
    if (resultMessage !== "") {
      setTimeout(() => {
        setResultMessage("");
      }, 3000);
    }
  }, [resultMessage]);

  const startGame = () => {
    setGameStarted(true);
    setGameEnded(false);
    setScore(0);
    generateQuestion();
  };

  const endGame = () => {
    setGameStarted(false);
    setGameEnded(true);
  };

  const generateQuestion = () => {
    const newAmount = Math.floor(Math.random() * 100) + 1;
    const newExchangeRate = Math.floor(Math.random() * 5000) + 1000;
    const newTransactionType = Math.random() > 0.5 ? "USD to LBP" : "LBP to USD";

    setAmount(newAmount);
    setExchangeRate(newExchangeRate);
    setTransactionType(newTransactionType);
    setResultMessage("");
  };

  const checkAnswer = () => {
    const correctAnswer =
      transactionType === "USD to LBP"
        ? amount * exchangeRate
        : amount / exchangeRate;

    if (parseFloat(userAnswer).toFixed(2) === correctAnswer.toFixed(2)) {
      setScore(score + 1);
      setResultMessage("Congratulations! Your answer is correct.");
    } else {
      setResultMessage("Incorrect. Please try again.");
    }

    setUserAnswer("");
    generateQuestion();
  };

  return (
    <div className="mini-game">
      {!gameStarted && !gameEnded && (
        <button onClick={startGame}>Start Game</button>
      )}

      {gameStarted && (
        <>
          <div>
            Convert {amount} {transactionType.split(" ")[0]} to{" "}
            {transactionType.split(" ")[2]} at the rate of {exchangeRate}.
          </div>
          <input
            type="number"
            value={userAnswer}
            onChange={(e) => setUserAnswer(e.target.value)}
            min="0"
          />
          <button onClick={checkAnswer}>Submit Answer</button>
          <button onClick={endGame}>End Game</button>
          <div>Score: {score}</div>
          {resultMessage && <div className="result-message">{resultMessage}</div>}
        </>
      )}

      {gameEnded && <div>Game Over. Your final score is {score}.</div>}
    </div>
  );
};

export default MiniGame;

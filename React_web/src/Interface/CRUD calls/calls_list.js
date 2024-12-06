import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode"; // Correct import
import './CallList.css';

const API_URL = "http://localhost:8084/calls/getcalls";
const REFRESH_URL = "http://localhost:8084/auth/refresh";

const CallList = () => {
  const navigate = useNavigate();
  const [calls, setCalls] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const user = JSON.parse(localStorage.getItem("user") || sessionStorage.getItem("user"));
  const role = user ? user.role : null;

  const getAccessToken = () => localStorage.getItem("accessToken") || sessionStorage.getItem("accessToken");
  const getRefreshToken = () => localStorage.getItem("refreshToken") || sessionStorage.getItem("refreshToken");

  const isTokenExpired = (token) => {
    if (!token) return true;
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000;
    return decoded.exp < currentTime;
  };

  const refreshAccessToken = async () => {
    const refreshToken = getRefreshToken();
    try {
      const response = await axios.post(REFRESH_URL, { refreshToken });
      const { accessToken } = response.data;
      localStorage.setItem("accessToken", accessToken);
      return accessToken;
    } catch (error) {
      console.error("Failed to refresh access token:", error);
      navigate("/sign-in");
      return null;
    }
  };

  const fetchCalls = useCallback(async () => {
    let token = getAccessToken();

    if (isTokenExpired(token)) {
      console.log("Access token expired, refreshing...");
      token = await refreshAccessToken();
      if (!token) return;
    }

    try {
      const response = await axios.get(API_URL, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setCalls(response.data);
    } catch (err) {
      setError("Failed to fetch calls");
    } finally {
      setLoading(false);
    }
  }, [navigate]);

  useEffect(() => {
    if (user && role === "EMPLOYEE") {
      fetchCalls();
    } else {
      navigate("/sign-in");
    }
  }, [user, role, navigate, fetchCalls]);

  const handleAddCall = () => {
    navigate("/add_call");
  };

  const handleEditCall = (id) => {
    navigate(`/edit_call/${id}`);
  };

  if (loading) {
    return <div className="loading-spinner">Loading calls...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="call-list-container">
      <header>
        <h2>Call List</h2>
        {role === "EMPLOYEE" && (
          <button className="add-call-button" onClick={handleAddCall}>
            Add New Call
          </button>
        )}
      </header>

      <div className="call-list">
        {calls.map((call) => (
          <div key={call.id} className="call-card">
            <h5>{call.description}</h5>
            <p><strong>Date:</strong> {new Date(call.date).toLocaleString()}</p>
            <p><strong>Duration:</strong> {call.duration} minutes</p>
            {role === "EMPLOYEE" && (
              <button onClick={() => handleEditCall(call.id)} className="edit-button">
                Edit
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default CallList;

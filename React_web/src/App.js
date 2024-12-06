import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import RegistrationForm from "./inscription/RegistrationForm";
import ConfirmationPage from "./inscription/ConfirmationPage";
import Homepage from "./home/HelloWorld";
import Login from "./login/Login";
import ResetPass from "./login/ResetPass";
import React, { useState, useEffect } from 'react';
import CompteValide from "./Interface/CRUD users/Tovalidate"; // Import AddForm here
import UserList from "./Interface/CRUD users/userlist";
import EditUserForm from "./Interface/CRUD users/EditUserForm";
import Dashboard from "./Interface/dashboard";
import AddForm from "./Interface/CRUD users/AddForm"; // Import AddForm here
import ComptenotValide from "./Interface/CRUD users/bloque";
import CallsList from "./Interface/CRUD calls/calls_list"
import AddCall from "./Interface/CRUD calls/add_call"
import EditCall from "./Interface/CRUD calls/edit_calls"
import ProtectedLayout from "./utils/ProtectedLayout";

const onLogout = () => {
  // Clear tokens from localStorage and sessionStorage
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  sessionStorage.removeItem("accessToken");
  sessionStorage.removeItem("refreshToken");
};

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/sign-in" element={<Login />} />
        <Route path="/" element={<Homepage />} />
        <Route path="/Registration" element={<RegistrationForm />} />
        <Route path="/confirmation" element={<ConfirmationPage />} />
        <Route path="/resetpassword" element={<ResetPass />} />
        <Route path='/dashboard' element={<Dashboard onLogout={onLogout}/>} />
        <Route path="/add" element={<AddForm/>} />
        <Route path="/bloque" element={<ComptenotValide/>}/>
        <Route path="/Tovalidate" element={<CompteValide onLogout={onLogout} />}/>
        <Route path="/users" element={<UserList />} />
        <Route path="/edit-user/:id" element={<EditUserForm />}/>
        <Route path="/call_list" element={<CallsList />} />
        <Route path="/add_call" element={<AddCall />} />
        <Route path="/edit_call/:id" element={<EditCall />} />
      </Routes>
    </Router>
  );
}

export default App;

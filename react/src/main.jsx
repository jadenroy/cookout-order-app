import React from "react";
import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import { BrowserRouter } from "react-router-dom";

import { UserProvider } from "./context/UserContext.jsx";

// axios defaults
import axios from "axios";
axios.defaults.baseURL = import.meta.env.VITE_REMOTE_API;

createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <UserProvider>
        <App />
      </UserProvider>
    </BrowserRouter>
  </React.StrictMode>
);

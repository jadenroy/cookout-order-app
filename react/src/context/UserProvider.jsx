import { createContext, useState, useContext } from "react";
import axios from "axios";

export const UserContext = createContext(null);

export function UserProvider({ children }) {
  const [user, setUser] = useState(() => {
    return JSON.parse(localStorage.getItem("user")) || null;
  });

  const [token, setToken] = useState(() => {
    const storedToken = localStorage.getItem("token");
    if (storedToken) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${storedToken}`;
    }
    return storedToken || null;
  });

  function updateUser(newUser) {
    setUser(newUser);
    if (newUser) {
      localStorage.setItem("user", JSON.stringify(newUser));
    } else {
      localStorage.removeItem("user");
    }
  }

  function updateToken(newToken) {
    setToken(newToken);
    if (newToken) {
      localStorage.setItem("token", newToken);
      axios.defaults.headers.common["Authorization"] = `Bearer ${newToken}`;
    } else {
      localStorage.removeItem("token");
      delete axios.defaults.headers.common["Authorization"];
    }
  }

  return (
    <UserContext.Provider
      value={{
        user,
        token,
        setUser: updateUser,
        setToken: updateToken,
        isAuthenticated: !!user && !!token
      }}
    >
      {children}
    </UserContext.Provider>
  );
}

export function useAuth() {
  return useContext(UserContext);
}

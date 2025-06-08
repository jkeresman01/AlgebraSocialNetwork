import { createContext, useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const navigate = useNavigate();

    const [isAuthenticated, setIsAuthenticated] = useState(() => {
        return localStorage.getItem("token") !== null;
    });

    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem("user");
        try {
            return stored ? JSON.parse(stored) : {};
        } catch (e) {
            console.warn("Invalid JSON in localStorage 'user':", stored);
            return {};
        }
    });

    const login = (token) => {
        try {
            const decoded = jwtDecode(token);
            const userPayload = decoded.user || decoded;
            setUser(userPayload);
            setIsAuthenticated(true);
            localStorage.setItem("token", token);
            localStorage.setItem("user", JSON.stringify(userPayload));
            navigate("/home");
        } catch (e) {
            console.error("Invalid token", e);
        }
    };

    const logout = () => {
        setIsAuthenticated(false);
        setUser({});
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        navigate("/");
    };

    useEffect(() => {
        const checkTokenExpired = () => {
            const token = localStorage.getItem("token");
            if (token) {
                try {
                    const decoded = jwtDecode(token);
                    const now = Date.now() / 1000;
                    if (decoded.exp < now) {
                        console.log("Token expired");
                        logout();
                    }
                } catch (err) {
                    console.error("Failed to decode token", err);
                    logout();
                }
            }
        };

        checkTokenExpired();
        const interval = setInterval(checkTokenExpired, 60 * 1000);

        return () => clearInterval(interval);
    }, [isAuthenticated]);

    const value = {
        isAuthenticated,
        user,
        login,
        logout,
        token: localStorage.getItem("token")
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export default AuthContext;
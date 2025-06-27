import { useContext } from "react";
import { Navigate } from "react-router-dom";
import AuthContext from "../context/AuthContext.jsx";

const PrivateRoute = ({ children }) => {
  const { isAuthenticated } = useContext(AuthContext);

  return isAuthenticated ? children : <Navigate to="/" replace />;
};

export default PrivateRoute;

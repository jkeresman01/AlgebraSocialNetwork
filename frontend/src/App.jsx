import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import HomePage from "./pages/HomePage.jsx";
import FriendRequestsPage from "./pages/FriendRequestsPage.jsx";
import StudentsPage from "./pages/StudentsPage.jsx";
import ProfilePage from "./pages/ProfilePage.jsx";
import PrivateRoute from "./routes/PrivateRoute.jsx";
import { AuthProvider } from "./context/AuthContext";
import "./styles/App.css";
import EditProfile from "./pages/EditProfilePage.jsx";

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route index element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/home"
            element={
              <PrivateRoute>
                <HomePage />
              </PrivateRoute>
            }
          />
          <Route
            path="/requests"
            element={
              <PrivateRoute>
                <FriendRequestsPage />
              </PrivateRoute>
            }
          />
          <Route
            path="/students"
            element={
              <PrivateRoute>
                <StudentsPage />
              </PrivateRoute>
            }
          />
          <Route
            path="/profile"
            element={
              <PrivateRoute>
                <ProfilePage />
              </PrivateRoute>
            }
          />
          <Route
            path="/edit-profile"
            element={
              <PrivateRoute>
                <EditProfile />
              </PrivateRoute>
            }
          />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;

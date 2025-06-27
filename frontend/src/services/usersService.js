import axios from "axios";

const API_BASE = import.meta.env.VITE_ALGEBRA_SOCIAL_NETWORK_BASE_URL;

const getAuthConfig = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});

export const getAllUsers = async (page = 0, size = 10, sort = "id,asc") => {
  try {
    return await axios.get(
      `${API_BASE}/api/v1/users?page=${page}&size=${size}&sort=${sort}`,
      getAuthConfig(),
    );
  } catch (err) {
    console.error("Failed to fetch users:", err);
  }
};

export const updateUser = async (userId, userData) => {
  try {
    return await axios.put(
      `${API_BASE}/api/v1/users/${userId}`,
      userData,
      getAuthConfig(),
    );
  } catch (e) {
    console.log(`Failed to update user: ${e}`);
  }
};

export const getUserById = async (userId) => {
  try {
    return await axios.delete(
      `${API_BASE}/api/v1/users/${userId}`,
      getAuthConfig(),
    );
  } catch (e) {
    console.log(`Failed to get user with id: ${userId}, ex: ${e}`);
  }
};

export const uploadProfileImage = (userId, formData) => {
  return axios.post(
    `${API_BASE}/api/v1/users/${userId}/profile-image`,
    formData,
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
        "Content-Type": "multipart/form-data",
      },
    },
  );
};

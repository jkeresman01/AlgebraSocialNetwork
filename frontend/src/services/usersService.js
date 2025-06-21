import axios from 'axios';

const API_BASE = import.meta.env.VITE_ALGEBRA_SOCIAL_NETWORK_BASE_URL;

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
    }
});

export const getAllUsers = async () => {
    try {
        return await axios.get(`${API_BASE}/api/v1/users`, getAuthConfig());
    } catch {
        console.error(`Error getting all users from API: ${API_BASE}`);
    }
};

export const updateUser = async (userId, userData) => {
    try {
        return await axios.put(`${API_BASE}/api/v1/users/${userId}`, userData, getAuthConfig());
    } catch (e) {
        console.log(`Failed to update user: ${e}`);
    }
};

export const getUserById = async (userId) => {
    try {
        return await axios.delete(`${API_BASE}/api/v1/users/${userId}`, getAuthConfig());
    } catch (e) {
        console.log(`Failed to get user with id: ${userId}, ex: ${e}`);
    }
};
